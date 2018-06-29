

import java.io.File;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class Model implements Subject {

    String red = "\033[31m";
    String yellow = "\033[32m";
    String lyellow = "\033[33m";
    String blue = "\033[34m";
    String purple = "\033[35m";
    String clear = "\033[0;0m";

    public static Model uniqueInstance;

    Locale locale = new Locale("pt", "BR");
    GregorianCalendar calendar = new GregorianCalendar();

    public ArrayList<Long> userID = new ArrayList<>();
    public ArrayList<String> keyWords = new ArrayList<>();


    File file = new File("E:\\Fatec\\Engenharia de Software III\\BeatJava-master\\Project\\bd\\cards.db4o");

    ObjectContainer cards = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "E:\\Fatec\\Engenharia de Software III\\BeatJava-master\\Project\\bd\\cards.db4o" );
    ObjectContainer users = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "E:\\Fatec\\Engenharia de Software III\\BeatJava-master\\Project\\bd\\users.db4o");
    ObjectContainer decks = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "E:\\Fatec\\Engenharia de Software III\\BeatJava-master\\Project\\bd\\decks.db4o");

    public Model() {
        file.deleteOnExit(); // comment if want to stay
        populate();

        /*Query query = cards.query();
        query.constrain(Card.class);
        ObjectSet<Card> allCards = query.execute();

        for (Card c : allCards){
            System.out.println(c);
        }

        Query querie = decks.query();
        query.constrain(Card.class);
        ObjectSet<Card> alldecks = querie.execute();

        for(Card c : alldecks){
            System.out.println("cid: " + c.getCardID());
            System.out.println("uid: " + c.getUserID());
            System.out.println("ud: " + c.getUnlockdate());
            System.out.println("ut: " + c.getUnlocktime());

        }*/

    }

    public static Model getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Model();
        }
        return uniqueInstance;
    }

    public void registerUser(User user) {
        System.out.println(yellow + "\nI'm in registerUser" + clear);
        users.store(user);
        users.commit();
    }
    
    public void validateUser(User user) {
        System.out.println(yellow + "\nI'm in validateUser()" + clear + "\n");

        Query query = users.query();
        query.constrain(User.class);
        ObjectSet<User> allUsers = query.execute();
        ArrayList<Long> userIds = new ArrayList<>();

        for (User u : allUsers) {
            userIds.add(u.getId());
        }

        if (!userIds.contains(user.getId())) {
            registerUser(user);
            System.out.println("\t" + red + user.getName() + " is not an user, registrating..." + clear + "\n");
        } else {
            System.out.println("\t" + blue + user.getName() + " is an user." + clear + "\n");

        }
    }

    public String filterString(String message) {
        System.out.println(yellow + "\nIm in filterString()" + clear);
        String word = message.toLowerCase();
        String str = Normalizer.normalize(word, Normalizer.Form.NFD).replaceAll("[^a-zZ-Z1-9 ]", "");
        System.out.println(purple + "\tMessage: " + str + clear);
        return str;
    }

    public String findKeyWord(String message) {
        System.out.println(yellow + "I'm in findKeyWord()" + clear);
        message = filterString(message);
        String[] messvet = message.split(" ");
        String kw = "";

        for (String aMessvet : messvet) {
            if (keyWords.contains(aMessvet)) {
                kw = aMessvet;
                break;
            }
        }
        System.out.println(purple + "Keyword: " + kw + clear);
        return kw;
    }

    public void storeCard(long userID, int cardID) {
        System.out.println(yellow + "\nI'm in storeCard()" + clear);
        Card aux = null;

        Query query = cards.query();
        query.constrain(Card.class);
        ObjectSet<Card> allCards = query.execute();

        for(Card c : allCards){
            if(c.getCardID() == cardID){
                aux = c;
            }
        }

        decks.store(new Card(userID, aux.getCardID(), aux.getCardName(), aux.getDescricao(), aux.getFile(), aux.getKeyword(), currentDate(), currentTime()));
        decks.commit();
    }

    public void getKeyWords() {
        System.out.println(yellow + "\nI'm in getKeyWords()" + clear);

        Query query = cards.query();
        query.constrain(Card.class);
        ObjectSet<Card> allCards = query.execute();
        System.out.println("Tamanho allCards: " + allCards.size());

        for (Card c : allCards) {
            System.out.println("chave: " + c.getKeyword());
            System.out.println("nome: " + c.getCardName());
            System.out.println("ID: " + c.getCardID());
            System.out.println();
            keyWords.add(c.getKeyword());
        }

        System.out.println(keyWords.isEmpty());
        for (String k : keyWords) {
            System.out.println(k);
        }
        System.out.println("Tamanho keywords: " + keyWords.size());
        System.out.println(keyWords.get(keyWords.size() - 1));
        System.out.println("Tamanho keywords depois: " + keyWords.size());

    }

    public void getUsersId() {
        System.out.println(yellow + "\nI'm in getUsersId()" + clear);

        Query query = users.query();
        query.constrain(User.class);
        ObjectSet<User> allUser = query.execute();

        for (User c : allUser) {
            userID.add(c.getId());
        }
    }


    public ArrayList<Card> myDeck(long userID) {
        System.out.println(yellow + "I'm in myDeck() " + clear);
        ArrayList<Card> deck = new ArrayList<>();

        Query query = decks.query();
        query.constrain(Card.class);
        ObjectSet<Card> allCards = query.execute();

        for (Card c : allCards) {
            if (c.getUserID() == userID) {
                deck.add(c);
            }
        }

        return deck;
    }




    public boolean hasWord(String message) {
        System.out.println(message);
        boolean flag = false;
        for (String w : keyWords) {
            if (message.contains(w)) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    public boolean userHasCard(String keyword, long userID) {
        System.out.println(yellow + "\nI'm in userHasCard()" + clear);
        ArrayList<Integer> cidfu = new ArrayList<>();
        int cardID = 0;

        Query query = decks.query();
        query.constrain(Card.class);
        ObjectSet<Card> allCards = query.execute();

        Query query2 = cards.query();
        query.constrain(Card.class);
        ObjectSet<Card> allCards2 = query2.execute();

        for (Card c : allCards) {
            if (c.getUserID() == userID) {
                cidfu.add(c.getCardID());
            }
        }

        for (Card c : allCards2) {
            if (c.getKeyword().equals(keyword)) {
                cardID = c.getCardID();
            }
        }

        return cidfu.contains(cardID);
    }

    public int getCardId(String keyword) {
        findKeyWord(keyword);
        int cardid = 0;

        Query query = cards.query();
        query.constrain(Card.class);
        ObjectSet<Card> allCards = query.execute();

        for (Card c : allCards) {
            System.out.println(c);
            if (c.getKeyword().equals(keyword)) {
                cardid = c.getCardID();
            }
        }

        return cardid;
    }


    public String getCardFile(int cardid) {
        System.out.println(yellow + "\nI'm in getCardName()" + clear);
        String file = "";

        Query query = cards.query();
        query.constrain(Card.class);
        ObjectSet<Card> allCards = query.execute();

        for (Card c : allCards) {
            if (c.getCardID() == cardid) {
                file = c.getFile();
            }
        }

        return file;
    }




    public String getCardName(int cardid) {
        String cardName = "";

        Query query = cards.query();
        query.constrain(Card.class);
        ObjectSet<Card> allCards = query.execute();

        for (Card c : allCards) {
            if (c.getCardID() == cardid) {
                cardName = c.getCardName();
            }
        }

        return cardName;
    }

    public void populate() {
        cards.store(new Card(1, "Arqueiras", "Um par de atacantes à distância com armadura leve. Elas ajudam na batalha contra unidades terrestres e aéreas, mas colorir seu cabelo é por sua própria conta", "Archers.png", "arco"));
        cards.store(new Card(2, "Bola de Fogo", "Ééééééé… a Bola de Fogo!!! Ela incinera uma pequena área, causando um alto dano. Mas este dano é reduzido em Torres Coroa", "Fireball.png", "fogo"));
        cards.store(new Card(3, "Golem de Gelo", "Durão, mira em construções e explode quando é destruído, atrasando inimigos próximos. Usa um colar estiloso de carvão para combinar com seu nariz e unhas.", "IceGolem.png", "gelo"));
        cards.store(new Card(4, "Mago de Gelo", "Este mago vem do Polo Norte e atira gelo nos inimigos para reduzir a velocidade de movimento e ataque deles", "IceWizard.png", "gelido"));
        cards.store(new Card(5, "Dragão Infernal", "Dispara um raio de fogo concentrado com dano que aumenta com o tempo. Usa um capacete porque voar tem seus perigos", "InfernoDragon.jpg", "voa"));
        cards.store(new Card(6, "Príncipe", "Não se engane com o carinha do pequeno pônei. Quando o príncipe começa a correr, você será atropelado. Causa o dobro de dano ao atacar correndo", "Prince.png", "ponei"));
        cards.store(new Card(7, "O Tronco", "Uma garrafa de Fúria se quebrou e transformou um tronco inocente no “O Tronco”. Agora, ele busca a vingança destruindo tudo no caminho!", "TheLog.png", "madeira"));
        cards.store(new Card(8, "Zap", "Uma garrafa de Fúria se quebrou e transformou um tronco inocente no “O Tronco”. Agora, ele busca a vingança destruindo tudo no caminho!", "Zap.png", "raio"));
        cards.commit();
    }


    public String currentTime() {
        SimpleDateFormat time = new SimpleDateFormat("HH:mm", locale);
        return time.format(calendar.getTime());
    }

    public String currentDate() {
        SimpleDateFormat day = new SimpleDateFormat("dd/MM/yy", locale);
        return day.format(calendar.getTime());
    }

    @Override
    public void registerObserver(Observer observer) {

    }

    @Override
    public void notifyObservers(long chatId, String data) {

    }


}