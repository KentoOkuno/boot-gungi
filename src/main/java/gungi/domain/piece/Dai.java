package gungi.domain.piece;

import java.util.List;

public class Dai extends Piece {
    Dai() {
        super(
                "大",
                List.of(
                        new MoveRange(Direct.DOWN, 8),
                        new MoveRange(Direct.UP, 8),
                        new MoveRange(Direct.LEFT, 8),
                        new MoveRange(Direct.RIGHT, 8),
                        new MoveRange(Direct.DOWN_RIGHT, 1),
                        new MoveRange(Direct.DOWN_LEFT, 1),
                        new MoveRange(Direct.UP_RIGHT, 1),
                        new MoveRange(Direct.UP_LEFT, 1)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 8),
                        new MoveRange(Direct.UP, 8),
                        new MoveRange(Direct.LEFT, 8),
                        new MoveRange(Direct.RIGHT, 8),
                        new MoveRange(Direct.DOWN_RIGHT, 2),
                        new MoveRange(Direct.DOWN_LEFT, 2),
                        new MoveRange(Direct.UP_RIGHT, 2),
                        new MoveRange(Direct.UP_LEFT, 2)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 8),
                        new MoveRange(Direct.UP, 8),
                        new MoveRange(Direct.LEFT, 8),
                        new MoveRange(Direct.RIGHT, 8),
                        new MoveRange(Direct.DOWN_RIGHT, 3),
                        new MoveRange(Direct.DOWN_LEFT, 3),
                        new MoveRange(Direct.UP_RIGHT, 3),
                        new MoveRange(Direct.UP_LEFT, 3)
                )
        );
    }
}
