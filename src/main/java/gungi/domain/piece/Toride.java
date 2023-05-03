package gungi.domain.piece;

import java.util.List;

public class Toride extends Piece {
    Toride() {
        super(
                "砦",
                List.of(
                        new MoveRange(Direct.UP, 1),
                        new MoveRange(Direct.LEFT, 1),
                        new MoveRange(Direct.RIGHT, 1),
                        new MoveRange(Direct.DOWN_RIGHT, 1),
                        new MoveRange(Direct.DOWN_LEFT, 1)
                ),
                List.of(
                        new MoveRange(Direct.UP, 2),
                        new MoveRange(Direct.LEFT, 2),
                        new MoveRange(Direct.RIGHT, 2),
                        new MoveRange(Direct.DOWN_RIGHT, 2),
                        new MoveRange(Direct.DOWN_LEFT, 2)
                ),
                List.of(
                        new MoveRange(Direct.UP, 3),
                        new MoveRange(Direct.LEFT, 3),
                        new MoveRange(Direct.RIGHT, 3),
                        new MoveRange(Direct.DOWN_RIGHT, 3),
                        new MoveRange(Direct.DOWN_LEFT, 3)
                )
        );
    }
}
