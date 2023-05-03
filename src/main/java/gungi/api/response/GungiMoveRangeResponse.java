package gungi.api.response;

public class GungiMoveRangeResponse extends GungiBaseResponse {
    public int[][] moveRange; // 0: can't move, 1: can move, 2: can capture only 3: can tsuke only

    public GungiMoveRangeResponse(int[][] moveRange) {
        super("moveRange");
        this.moveRange = moveRange;
    }
}
