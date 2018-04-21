
import com.pengrad.telegrambot.model.Update;

import java.sql.SQLException;
import java.util.ArrayList;

public interface Controller {
    ArrayList<Model.Card> search(long userID) throws SQLException;

    void get(Update update);

    String getPath(String message) throws SQLException;

    void getUsersId();

    void getKeyWords();

    void set(Update update);

    int getCardId(String keyword);

    String getKeyword(String message);

    void storeCard(long userid, int cardid) throws SQLException;

    boolean userHasCard(String keyword, long userID);

    boolean hasWord(String message);

    void validateUser(Update update) throws SQLException;


}
