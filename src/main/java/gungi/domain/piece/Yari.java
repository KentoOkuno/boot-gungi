package gungi.domain.piece;

import java.util.List;

public class Yari extends Piece {
    Yari() {
        super(
                "槍",
                List.of(
                        new MoveRange(Direct.DOWN, 1),
                        new MoveRange(Direct.UP, 2),
                        new MoveRange(Direct.UP_RIGHT, 1),
                        new MoveRange(Direct.UP_LEFT, 1)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 2),
                        new MoveRange(Direct.UP, 3),
                        new MoveRange(Direct.UP_RIGHT, 2),
                        new MoveRange(Direct.UP_LEFT, 2)
                ),
                List.of(
                        new MoveRange(Direct.DOWN, 3),
                        new MoveRange(Direct.UP, 4),
                        new MoveRange(Direct.UP_RIGHT, 3),
                        new MoveRange(Direct.UP_LEFT, 3)
                )
        );
    }
}
