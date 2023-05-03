package gungi.api.request;

public record MoveRangeForHandRequest(
        int userId,
        String pieceId,
        String[][] board,
        String[] hand,
        boolean playerTurn) {
}