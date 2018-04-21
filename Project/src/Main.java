
import com.mysql.jdbc.ConnectionImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Main {

    private static Model model;

    public static void main(String[] args) throws SQLException {

        model = Model.getInstance();

        //initializeModel(model);
        View view = new View(model);
        model.registerObserver(view); //connection Model -> View
        view.receiveUsersMessages();

    }
}
