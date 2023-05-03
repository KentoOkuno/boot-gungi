package gungi.dto;

public class BoardDto {
    private String[][] board;
    private String[] hand;

    public BoardDto(String[][] board, String[] hand) {
        this.board = board;
        this.hand = hand;
    }

    public String[] getHand() {
        return hand;
    }

    public void setHand(String[] hand) {
        this.hand = hand;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }
}
