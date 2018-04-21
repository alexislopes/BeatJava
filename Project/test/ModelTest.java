import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.instanceOf;

public class ModelTest {
    private static Model model;
    private View view;
    private long userID = 319078279;
    private String keyword = "ponei";
    private String userName = "alexislopes";




    @Before
    public void setUp(){
        model = Model.getInstance();
        model.getKeyWords();
    }


    @Test
    public void filterStringTest(){
        assertEquals("ola tudo bom", model.filterString("Olá, Tudo bom?"));
    }

    @Test
    public void myDeckTest(){
        assertThat(model.myDeck(userID), instanceOf(ArrayList.class));
    }

    @Test
    public void findKeyWordTest() {
        assertEquals("gelido", model.findKeyWord("você é muito gélido."));
    }

    @Test
    public void userHasCardTest(){
        assertTrue(model.userHasCard(keyword, userID));
    }

    @Test
    public void getCardPathTest() throws SQLException {
        String cardName = "IceGolem.png";
        //assertEquals(cardName, model.getCardPath(keyword));
    }





}