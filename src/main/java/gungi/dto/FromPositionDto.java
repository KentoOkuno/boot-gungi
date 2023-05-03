package gungi.dto;

public class FromPositionDto {
    public Integer x;
    public Integer y;
    public String pieceIdForHand;

    public FromPositionDto(Integer x, Integer y, String pieceIdForHand) {
        this.x = x;
        this.y = y;
        this.pieceIdForHand = pieceIdForHand;
    }
}
