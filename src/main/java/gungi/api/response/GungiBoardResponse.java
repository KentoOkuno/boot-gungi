package gungi.api.response;

public class GungiBoardResponse extends GungiBaseResponse {
    public String[][] board;
    public String[] hand;
    public Integer lastMoveFromX;
    public Integer lastMoveFromY;

    public GungiBoardResponse(String[][] board, String[] hand, Integer lastMoveFromX, Integer lastMoveFromY) {
        super("select");
        this.board = board;
        this.hand = hand;
        this.lastMoveFromX = lastMoveFromX;
        this.lastMoveFromY = lastMoveFromY;
    }
}
