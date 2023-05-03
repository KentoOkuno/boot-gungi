package gungi.domain.piece;

import java.util.List;

public class Samurai extends Piece {
    Samurai() {
        super(
                "侍",
                List.of(
                        new MoveRange(Direct.DOWN, 1),
                        new MoveRange(Direct.UP, 1),
                        new MoveRange(Direct.UP_RIGHT, 1),
                        new MoveRange(Direct.UP_LEFT, 1)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 2),
                        new MoveRange(Direct.UP, 2),
                        new MoveRange(Direct.UP_RIGHT, 2),
                        new MoveRange(Direct.UP_LEFT, 2)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 3),
                        new MoveRange(Direct.UP, 3),
                        new MoveRange(Direct.UP_RIGHT, 3),
                        new MoveRange(Direct.UP_LEFT, 3)
                )
        );
    }
}
