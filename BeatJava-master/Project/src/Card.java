import java.util.Date;

public class Card {

    private long userID;
    private int cardID;
    private String cardName;
    private String descricao;
    private String file;
    private String keyword;
    private String unlockdate;
    private String unlocktime;

    public Card(int cardID, String cardName, String descricao, String file, String keyword){
        this.cardID = cardID;
        this.cardName = cardName;
        this.descricao = descricao;
        this.file = file;
        this.keyword = keyword;
    }

    public Card(long userID, int cardID, String cardName, String descricao, String file, String keyword, String unlockdate, String unlocktime) {
        this.userID = userID;
        this.cardID = cardID;
        this.cardName = cardName;
        this.descricao = descricao;
        this.file = file;
        this.keyword = keyword;
        this.unlockdate = unlockdate;
        this.unlocktime = unlocktime;
    }

    public long getUserID() {
        return userID;
    }

    public int getCardID() {
        return cardID;
    }

    public String getCardName() {
        return cardName;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getFile() {
        return file;
    }

    public String getUnlockdate() {
        return unlockdate;
    }

    public String getUnlocktime() {
        return unlocktime;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardID=" + cardID +
                ", cardName='" + cardName + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
