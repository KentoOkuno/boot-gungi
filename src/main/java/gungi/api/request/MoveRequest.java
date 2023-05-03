package gungi.api.request;


public record MoveRequest(
        int userId,
        int toX,
        int toY,
        String toPieceIdForHand,
        int fromX,
        int fromY,
        int action,
        String[][] board,
        String[] hand,
        /** true: 先手, false: 後手 */
        boolean playerTurn) {
}
