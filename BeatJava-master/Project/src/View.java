import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Chat;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class View implements Observer {

    TelegramBot bot = TelegramBotAdapter.build("474203343:AAG6ZAADnP_RUdSy9DL6bpb2QOglrhLnRjs");

    //Object that receives messages
    GetUpdatesResponse updatesResponse;
    //Object that send responses
    SendResponse sendResponse;
    //Object that manage chat actions like "typing action"
    BaseResponse baseResponse;

    private long userID;
    private long chatID;
    private String message;
    private String userName;

    private String path = "C:\\Users\\alexi\\Desktop\\cards\\";

    private boolean start = false;

    int i = 0;
    private int queuesIndex = 0;

    private Controller controller; //Strategy Pattern -- connection View -> Controller

    private Model model;


    public View(Model model) {
        this.model = model;
    }

    public void setController(Controller controller) { //Strategy Pattern
        this.controller = controller;
    }

    public void receiveUsersMessages() {

        int cont = 0;
        getKeyWords();
        getUsersId();

        //infinity loop
        while (true) {

            //taking the Queue of Messages
            updatesResponse = bot.execute(new GetUpdates().limit(100).offset(queuesIndex));

            //Queue of messages
            List<Update> updates = updatesResponse.updates();


            //taking each message in the Queue
            for (Update update : updates) {

                /*if(!update.callbackQuery().data().isEmpty()){
                    if(update.callbackQuery().data().equals("change_to_plus")){
                        i++;
                    } else if(update.callbackQuery().data().equals("change_to_minus")){
                        i--;
                    }

                }*/

                //System.out.println(update.callbackQuery().data());
                this.userName = update.message().from().firstName();
                this.message = update.message().text();
                this.chatID = update.message().chat().id();
                this.userID = update.message().from().id();
                User us = new User(userID, userName);


                //updating queue's index
                queuesIndex = update.updateId() + 1;


                validateUser(us);

                setController(new UserController(model, this));
                this.callController(update);

                if (message.equals("/start")) {
                    this.start = true;
                }


                if (start) {
                    message = model.filterString(message);


                    if (update.message().chat().type().equals(Chat.Type.Private)) {
                        sendTypingMessage(update);
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Quanto mais melhor!").replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton[]{new InlineKeyboardButton("Adicione-me a grupos.").url("https://telegram.me/PrototypeBot?startgroup=true")})));


                    } else {

                        if (cont == 0) {
                            cont++;
                            sendResponse = bot.execute(new SendMessage(chatID, "Eai! vamos colecionar cartas?"));
                        }
                        if (hasWord(message)) {
                            if (!userHasCard(getKeyword(message))) {
                                setController(new CardController(model, this));
                                String cardPath = callControllerGet(message);
                                File card = new File(this.path + cardPath);

                                System.out.println(model.purple + "\n\tSending " + cardPath + " to " + userName + ".");
                                storeCard();
                                sendCard(card);
                            }
                        }

                        if (message.equals("mydeck")) {
                            setController(new CardController(model, this));
                            ArrayList<Card> deck;
                            deck = callControllerGetDeck(userID);


                            //File cards = new File(this.path + deck.get(i).getFile());
                                /*sendResponse = bot
                                        .execute(new SendPhoto(chatID, cards).caption(deck.get(i).getCardName())
                                                .replyMarkup(new InlineKeyboardMarkup(
                                                        new InlineKeyboardButton[]{new InlineKeyboardButton("<").callbackData("change_to_minus"),new InlineKeyboardButton( i +"/" + deck.size()).callbackData("does_nothing"), new InlineKeyboardButton(">").callbackData("change_to_plus")
                                                                })));*/
                            for (Card card : deck) {
                                File cards = new File(this.path + card.getFile());
                                sendResponse = bot.execute(new SendPhoto(chatID, cards).caption(card.getCardName() + "\n\n" + card.getDescricao() + "\n\n foi desbloqueada em " + card.getUnlockdate() + " as " + card.getUnlocktime()));
                            }
                        }


                    }

                } else {
                    sendTypingMessage(update);
                    sendResponse = bot.execute(new SendMessage(chatID, "Você precisa utilizar o comando /start"));
                }
            }
        }
    }

    public void imaismais() {
        this.i++;
    }

    public void validateUser(User user) {
        setController(new UserController(this.model, this));
        controller.validateUser(user);

    }


    public void update(long chatId, String studentsData) {
        sendResponse = bot.execute(new SendMessage(chatId, studentsData));

    }

    public void getUsersId() {
        setController(new UserController(this.model, this));
        controller.getUsersId();
    }

    public void getKeyWords() {
        setController(new CardController(this.model, this));
        controller.getKeyWords();
    }

    public void storeCard() {
        setController(new CardController(this.model, this));
        controller.storeCard(this.userID, controller.getCardId(message));
    }

    public void callController(Update update) {
        this.controller.search(this.userID);
    }

    public String getKeyword(String message) {
        setController(new CardController(this.model, this));
        return controller.getKeyword(message);

    }

    public ArrayList<Card> callControllerGetDeck(long userID) {
        return this.controller.search(userID);
    }

    public String callControllerGet(String message) {
        return this.controller.getPath(message);
    }

    public void sendCard(File card) {
        sendResponse = bot.execute(new SendPhoto(this.chatID, card).caption("Opa " + this.userName + ", você desbloqueou uma carta!"));
    }

    public void sendTypingMessage(Update update) {
        baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
    }

    public boolean userHasCard(String keyword) {
        setController(new CardController(this.model, this));
        return controller.userHasCard(getKeyword(message), userID);
    }

    public boolean hasWord(String message) {
        setController(new CardController(this.model, this));
        return controller.hasWord(message);
    }

    @Override
    public void update(long chatId, String name, String nick, Date joinday) {
    }

    public long getUserID() {
        return this.userID;
    }
}
