package gungi.api.request;

public record MoveRangeRequest(
        int userId,
        int x,
        int y,
        String[][] board,
        boolean playerTurn) {
}