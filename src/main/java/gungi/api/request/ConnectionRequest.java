package gungi.api.request;


public record ConnectionRequest(
        int userId,
        boolean playerTurn) {
}
