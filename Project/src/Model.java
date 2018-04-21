
import com.mysql.jdbc.Statement;
import com.pengrad.telegrambot.model.Update;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Model implements Subject {

    String red = "\033[31m";
    String yellow = "\033[32m";
    String lyellow = "\033[33m";
    String blue = "\033[34m";
    String purple = "\033[35m";
    String clear = "\033[0;0m";


    //private String keyword;

    Locale locale = new Locale("pt", "BR");
    GregorianCalendar calendar = new GregorianCalendar();

    public static Model uniqueInstance;

    public Model() {
        new Connector();
    }

    public ArrayList<Long> userID = new ArrayList<>();
    public ArrayList<String> keyWords = new ArrayList<>();
    public ArrayList<Integer> cardId = new ArrayList<>();

    public static Model getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Model();
        }
        return uniqueInstance;
    }


    public void registerUser(long id, String name, String nick) throws SQLException {
        System.out.println(yellow + "\nI'm in registerUser" + clear);
        String query = "INSERT INTO users (user_id, user_name, user_nick, join_day)" + " values (?, ?, ?, ?)";
        PreparedStatement ps = Connector.conn.prepareStatement(query);
        ps.setLong(1, id);
        ps.setString(2, name);
        ps.setString(3, nick);
        ps.setString(4, currentDate());

        ps.execute();

    }

    public void validateUser(Update update) throws SQLException {
        System.out.println(yellow + "\nI'm in validateUser()" + clear + "\n");
        long id = update.message().from().id();
        String name = update.message().from().firstName();
        String nick = update.message().from().username();

        if (userID.contains(id)) {
            System.out.println("\t" + blue + name + " is an user." + clear + "\n");
        } else {
            System.out.println("\t" + red + name + " is not an user, registrating..." + clear + "\n");
            registerUser(id, name, nick);
        }

    }

    public String filterString(String message) {
        System.out.println(yellow + "\nIm in filterString()" + clear);
        String word = message.toLowerCase();
        String str = Normalizer.normalize(word, Normalizer.Form.NFD).replaceAll("[^a-zZ-Z1-9 ]", "");
        System.out.println(purple + "\tMessage: " + str + clear);
        return str;
    }

    public void getKeyWords() {
        System.out.println(yellow + "\nI'm in getKeyWords()" + clear);

        java.sql.Statement stmt;
        ResultSet rs;
        String query = "select card_keyword from cards";


        try {
            stmt = Connector.conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                keyWords.add(rs.getString("card_keyword"));
            }

            //for (String wrd : keyWords) {
            //    System.out.println(wrd);

            // }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public void getUsersId() {
        System.out.println(yellow + "\nI'm in getUsersId()" + clear);
        java.sql.Statement stmt;
        ResultSet rs;
        String query = "select user_id from users";

        try {
            stmt = Connector.conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                userID.add(rs.getLong("user_id"));
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }


    public ArrayList<Card> myDeck(long userID) {
        System.out.println(yellow + "I'm in myDeck() " + clear);
        ArrayList<Card> deck = new ArrayList<>();
        String unlocktime, unlockday;
        int cardid;
        Card card;


        java.sql.Statement stmt;
        ResultSet rs;
        String query = "select card_id, unlock_day, unlock_time from decks where user_id = " + userID;

        try {
            stmt = Connector.conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                cardid = rs.getInt("card_id");
                unlockday = rs.getString("unlock_day");
                unlocktime = rs.getString("unlock_time");
                card = new Card(getCardName(cardid),getCardPath(cardid), unlockday, unlocktime);
                deck.add(card);

            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return deck;
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

    public boolean hasWord(String message) {
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
        java.sql.Statement stmt;
        ResultSet rs;
        String query1 = "select card_id from decks where user_id = " + userID;
        String query2 = "select card_id from cards where card_keyword = " + "'" + keyword + "'";
        ArrayList<Integer> cidfu = new ArrayList<>();
        int cardID = 0;

        try {

            stmt = Connector.conn.createStatement();
            rs = stmt.executeQuery(query1);
            while (rs.next()) {
                cidfu.add(rs.getInt("card_id"));
            }


            stmt = Connector.conn.createStatement();
            rs = stmt.executeQuery(query2);
            while (rs.next()) {
                cardID = rs.getInt("card_id");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(purple + "\n\tcardID: " + cardID + clear);

        return cidfu.contains(cardID);
    }

    public int getCardId(String keyword){
        findKeyWord(keyword);
        int cardid = 0;
        java.sql.Statement stmt;
        ResultSet rs;

        String query = "select card_id from cards where card_keyword = " + "'" + keyword + "'";

        try {

            stmt = Connector.conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                cardid = rs.getInt("card_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cardid;
    }



    public String getCardPath(int cardid){

        System.out.println(yellow + "\nI'm in getCardName()" + clear);
        String file = "";
        String cardName = "";

        System.out.println("\n\t" + blue + "Nice, user does't has the card" + clear);
        java.sql.Statement stmt;
        ResultSet rs;
        String query = "select card_path from cards where card_id = " + cardid;

        try {

            stmt = Connector.conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                file = rs.getString("card_path");
            }
            System.out.println(purple + "\n\tFile name: " + file + clear);


        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return file;
    }


    public void storeCard(long userID, int cardID) throws SQLException {
        System.out.println(yellow + "\nI'm in storeCard()" + clear);

        String query = "insert into decks (user_id, card_id, unlock_day, unlock_time)" + "values (?, ?, ?, ?)";

        PreparedStatement ps = Connector.conn.prepareStatement(query);
        ps.setLong(1, userID);
        ps.setInt(2, cardID);
        ps.setString(3, currentDate());
        ps.setString(4, currentTime());

        ps.execute();
    }

    public String getCardName(int cardid){
        String cardName = "";

        java.sql.Statement stmt;
        ResultSet rs;
        String query = "select card_name from cards where card_id = " + cardid;

        try {

            stmt = Connector.conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                cardName = rs.getString("card_name");
            }


        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return cardName;
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

    public class Card {
        public String name;
        public String path;
        public String unlockDay;
        public String unlockTime;

        public Card(String name, String path, String unlockDay, String unlockTime) {
            this.name = name;
            this.path = path;
            this.unlockDay = unlockDay;
            this.unlockTime = unlockTime;
        }

    }
}