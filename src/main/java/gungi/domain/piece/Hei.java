package gungi.domain.piece;

import java.util.List;

public class Hei extends Piece {
    Hei() {
        super(
                "兵",
                List.of(
                        new MoveRange(Direct.DOWN, 1),
                        new MoveRange(Direct.UP, 1)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 2),
                        new MoveRange(Direct.UP, 2)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 3),
                        new MoveRange(Direct.UP, 3)
                )
        );
    }
}
