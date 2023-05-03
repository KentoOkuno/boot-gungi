package gungi.domain.piece;

import java.util.List;

public class Chu extends Piece {
    Chu() {
        super(
                "中",
                List.of(
                        new MoveRange(Direct.DOWN, 1),
                        new MoveRange(Direct.UP, 1),
                        new MoveRange(Direct.LEFT, 1),
                        new MoveRange(Direct.RIGHT, 1),
                        new MoveRange(Direct.DOWN_RIGHT, 8),
                        new MoveRange(Direct.DOWN_LEFT, 8),
                        new MoveRange(Direct.UP_RIGHT, 8),
                        new MoveRange(Direct.UP_LEFT, 8)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 2),
                        new MoveRange(Direct.UP, 2),
                        new MoveRange(Direct.LEFT, 2),
                        new MoveRange(Direct.RIGHT, 2),
                        new MoveRange(Direct.DOWN_RIGHT, 8),
                        new MoveRange(Direct.DOWN_LEFT, 8),
                        new MoveRange(Direct.UP_RIGHT, 8),
                        new MoveRange(Direct.UP_LEFT, 8)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 3),
                        new MoveRange(Direct.UP, 3),
                        new MoveRange(Direct.LEFT, 3),
                        new MoveRange(Direct.RIGHT, 3),
                        new MoveRange(Direct.DOWN_RIGHT, 8),
                        new MoveRange(Direct.DOWN_LEFT, 8),
                        new MoveRange(Direct.UP_RIGHT, 8),
                        new MoveRange(Direct.UP_LEFT, 8)
                )
        );
    }
}
