
import com.pengrad.telegrambot.model.Update;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserController implements Controller {

    private Model model;
    private View view;

    public UserController(Model model, View view) throws SQLException {
        this.model = model;
        this.view = view;

    }

    @Override
    public ArrayList<Model.Card> search(long userID) throws SQLException { return null; }

    @Override
    public void get(Update update) { }

    @Override
    public String getPath(String message) throws SQLException { return null; }

    @Override
    public void set(Update update) { }

    @Override
    public int getCardId(String keyword) { return 0; }

    @Override
    public String getKeyword(String message) { return null; }

    @Override
    public void storeCard(long userid, int cardid) throws SQLException { }

    @Override
    public boolean userHasCard(String keyword, long userID) { return false; }

    @Override
    public boolean hasWord(String message) {
        return false;
    }

    @Override
    public void validateUser(Update update) throws SQLException {
        model.validateUser(update);
    }

    public void getUsersId(){ model.getUsersId(); }

    @Override
    public void getKeyWords() {}


}
