
import com.pengrad.telegrambot.model.Update;

import java.sql.SQLException;
import java.util.ArrayList;

public class CardController implements Controller {

    private Model model;
    private View view;

    public CardController(Model model, View view) {
        this.model = model;
        this.view = view;
    }


    @Override
    public ArrayList<Card> search(long userID) {
       return model.myDeck(userID);
    }

    @Override
    public void get(Update update) {

    }

    @Override
    public String getPath(String message){
        return model.getCardFile(model.getCardId(model.findKeyWord(message)));
    }

    @Override
    public void getUsersId() {

    }

    @Override
    public void getKeyWords() {
        model.getKeyWords();
    }

    @Override
    public void set(Update update) {

    }

    @Override
    public int getCardId(String keyword) {
       return model.getCardId(keyword);
    }

    @Override
    public String getKeyword(String message) {
        return model.findKeyWord(message);
    }

    @Override
    public void storeCard(long userid, int cardid){
        model.storeCard(userid, cardid);
    }

    @Override
    public boolean userHasCard(String keyword, long userID) {
        return model.userHasCard(keyword, userID);
    }

    @Override
    public boolean hasWord(String message) {
        return model.hasWord(message);
    }

    @Override
    public void validateUser(User user) { }


}
