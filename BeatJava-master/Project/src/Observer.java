
import java.util.Date;

public interface Observer {

    public void update(long chatId, String name, String nick, Date joinday);
}
