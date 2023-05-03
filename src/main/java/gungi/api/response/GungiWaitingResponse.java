package gungi.api.response;

public class GungiWaitingResponse extends GungiBaseResponse {
    public int userId;

    public GungiWaitingResponse(int userId) {
        super("waiting");
        this.userId = userId;
    }
}
