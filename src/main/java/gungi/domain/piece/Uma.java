package gungi.domain.piece;

import java.util.List;

public class Uma extends Piece {
    Uma() {
        super(
                "馬",
                List.of(
                        new MoveRange(Direct.DOWN, 2),
                        new MoveRange(Direct.UP, 2),
                        new MoveRange(Direct.LEFT, 1),
                        new MoveRange(Direct.RIGHT, 1)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 3),
                        new MoveRange(Direct.UP, 3),
                        new MoveRange(Direct.LEFT, 2),
                        new MoveRange(Direct.RIGHT, 2)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 4),
                        new MoveRange(Direct.UP, 4),
                        new MoveRange(Direct.LEFT, 3),
                        new MoveRange(Direct.RIGHT, 3)
                )
        );
    }
}
