package gungi.api.response;

public class GungiResultResponse extends GungiBaseResponse {
    public Integer winnerUser;

    public GungiResultResponse(Integer winnerUser) {
        super("finished");
        this.winnerUser = winnerUser;
    }
}
