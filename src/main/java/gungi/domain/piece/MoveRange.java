package gungi.domain.piece;

public class MoveRange {
    public Direct direct;
    public int distance;
    public int jump;

    MoveRange(Direct direct, int distance) {
        this.direct = direct;
        this.distance = distance;
        this.jump = 0;
    }

    MoveRange(Direct direct, int distance, int jump) {
        this.direct = direct;
        this.distance = distance;
        this.jump = jump;
    }
}
