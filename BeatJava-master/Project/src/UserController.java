
import com.pengrad.telegrambot.model.Update;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserController implements Controller {

    private Model model;
    private View view;

    public UserController(Model model, View view){
        this.model = model;
        this.view = view;

    }

    @Override
    public ArrayList<Card> search(long userID){ return null; }

    @Override
    public void get(Update update) { }

    @Override
    public String getPath(String message){ return null; }

    @Override
    public void set(Update update) { }

    @Override
    public int getCardId(String keyword) { return 0; }

    @Override
    public String getKeyword(String message) { return null; }

    @Override
    public void storeCard(long userid, int cardid){ }

    @Override
    public boolean userHasCard(String keyword, long userID) { return false; }

    @Override
    public boolean hasWord(String message) {
        return false;
    }

    @Override
    public void validateUser(User user){
        model.validateUser(user);
    }

    public void getUsersId(){ model.getUsersId(); }

    @Override
    public void getKeyWords() {}


}
