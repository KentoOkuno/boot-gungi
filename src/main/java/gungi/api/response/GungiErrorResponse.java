package gungi.api.response;

public class GungiErrorResponse extends GungiBaseResponse {
    public String errorMessage;

    public GungiErrorResponse(String error) {
        super("error");
        this.errorMessage = error;
    }
}
