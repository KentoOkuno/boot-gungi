package gungi.domain.piece;

import java.util.List;

public class Shinobi extends Piece {
    Shinobi() {
        super(
                "忍",
                List.of(
                        new MoveRange(Direct.DOWN_RIGHT, 2),
                        new MoveRange(Direct.DOWN_LEFT, 2),
                        new MoveRange(Direct.UP_RIGHT, 2),
                        new MoveRange(Direct.UP_LEFT, 2)
                ),
                List.of(
                        new MoveRange(Direct.DOWN_RIGHT, 3),
                        new MoveRange(Direct.DOWN_LEFT, 3),
                        new MoveRange(Direct.UP_RIGHT, 3),
                        new MoveRange(Direct.UP_LEFT, 3)
                ),
                List.of(
                        new MoveRange(Direct.DOWN_RIGHT, 4),
                        new MoveRange(Direct.DOWN_LEFT, 4),
                        new MoveRange(Direct.UP_RIGHT, 4),
                        new MoveRange(Direct.UP_LEFT, 4)
                )
        );
    }
}
