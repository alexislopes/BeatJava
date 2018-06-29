
import com.pengrad.telegrambot.model.Update;

import java.sql.SQLException;
import java.util.ArrayList;

public interface Controller {
    ArrayList<Card> search(long userID);

    void get(Update update);

    String getPath(String message);

    void getUsersId();

    void getKeyWords();

    void set(Update update);

    int getCardId(String keyword);

    String getKeyword(String message);

    void storeCard(long userid, int cardid);

    boolean userHasCard(String keyword, long userID);

    boolean hasWord(String message);

    void validateUser(User user);


}
