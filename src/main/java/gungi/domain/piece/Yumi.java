package gungi.domain.piece;

import java.util.List;

public class Yumi extends Piece {
    Yumi() {
        super(
                "弓",
                List.of(
                        new MoveRange(Direct.DOWN, 1),
                        new MoveRange(Direct.UP, 2, 1),
                        new MoveRange(Direct.UP_RIGHT, 2, 1),
                        new MoveRange(Direct.UP_LEFT, 2, 1)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 2),
                        new MoveRange(Direct.UP, 3, 1),
                        new MoveRange(Direct.UP_RIGHT, 3, 1),
                        new MoveRange(Direct.UP_LEFT, 3, 1)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 3),
                        new MoveRange(Direct.UP, 4, 1),
                        new MoveRange(Direct.UP_RIGHT, 4, 1),
                        new MoveRange(Direct.UP_LEFT, 4, 1)
                )
        );
    }
}
