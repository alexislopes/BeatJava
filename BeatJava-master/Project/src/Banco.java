

import com.db4o.ObjectSet;
import com.db4o.query.Query;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Banco {
    Model m = new Model();
    Scanner reader = new Scanner(System.in);

    public void testetable(){
        Query query = m.cards.query();
        query.constrain(Card.class);
        ObjectSet<Card> allCards = query.execute();

        for(Card c : allCards){
            System.out.println("id: " + c.getCardID());
            System.out.println("nome: " + c.getCardName());
            System.out.println("chave: " + c.getKeyword());
            System.out.println("file: " + c.getFile());
        }
    }

    public void popular() {
        m.cards.store(new Card(1, "Arqueiras", null, "Archers.png", "arco"));
        m.cards.store(new Card(2, "Bola de Fogo", null, "Fireball.png", "fogo"));
        m.cards.store(new Card(3, "Golem de Gelo", null, "IceGolem.png", "gelo"));
        m.cards.store(new Card(4, "Mago de Gelo", null, "IceWizard.png", "gelido"));
        m.cards.store(new Card(5, "Dragão Infernal", null, "InfernoDragon.jpg", "voa"));
        m.cards.store(new Card(6, "Príncipe", null, "Prince.png", "ponei"));
        m.cards.store(new Card(7, "O Tronco", null, "TheLog.png", "madeira"));
        m.cards.store(new Card(8, "Zap", null, "Zap.png", "raio"));
        m.cards.commit();
    }
}
