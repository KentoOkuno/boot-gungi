package gungi.api.response;

public class GungiGameResponse extends GungiBaseResponse {
    public int userId;
    public Integer opponentId;
    public boolean playerTurn; // true: 先手, false: 後手
    public GungiBoardResponse setup;

    public GungiGameResponse(int userId, Integer opponentId, boolean playerTurn, GungiBoardResponse setup) {
        super("matched");
        this.userId = userId;
        this.opponentId = opponentId;
        this.playerTurn = playerTurn;
        this.setup = setup;
    }

}
