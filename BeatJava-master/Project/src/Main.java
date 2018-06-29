import java.util.Scanner;

public class Main {

    private static Model model;

    public static void main(String[] args) {
        model = Model.getInstance();
        //initializeModel(model);
        View view = new View(model);
        model.registerObserver(view); //connection Model -> View
        view.receiveUsersMessages();
    }
}
