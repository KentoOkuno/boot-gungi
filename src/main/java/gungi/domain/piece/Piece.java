package gungi.domain.piece;

import java.util.List;

/**
 * 駒抽象クラス
 */
public abstract class Piece {
    public String display;
    public List<MoveRange> moveRangeList;
    public List<MoveRange> moveRangeList2;
    public List<MoveRange> moveRangeList3;

    Piece(String display
            , List<MoveRange> moveRangeList
            , List<MoveRange> moveRangeList2
            , List<MoveRange> moveRangeList3
    ) {
        this.display = display;
        this.moveRangeList = moveRangeList;
        this.moveRangeList2 = moveRangeList2;
        this.moveRangeList3 = moveRangeList3;
    }
}
