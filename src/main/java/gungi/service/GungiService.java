package gungi.service;

import gungi.domain.piece.MoveRange;
import gungi.domain.piece.Piece;
import gungi.domain.piece.PieceEnum;
import gungi.dto.BoardDto;
import gungi.dto.FromPositionDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * ゲームのロジックを実装するクラス
 */
@Service
public class GungiService {
    // 空の時の文字列
    private static final String EMPTY = " ";
    // ツケるときの記号
    private static final String OVERLAP = "-";

    /**
     * 移動時のアクション
     */
    private static final int ACTION_MOVE = 0;
    private static final int ACTION_TAKE = 1;
    private static final int ACTION_TSUKE = 2;

    /**
     * 移動可能範囲情報
     */
    private static final int MOVE_RANGE_NOT_MOVE = 0;
    private static final int MOVE_RANGE_MOVE_ONLY = 1;
    private static final int MOVE_RANGE_CAN_TAKE = 2;
    private static final int MOVE_RANGE_CAN_TSUKE = 3;
    private static final int MOVE_RANGE_CAN_TAKE_TSUKE = 4;

    // 初期配置‗盤面
    public BoardDto setup() {
        String[][] board = new String[9][9];
        Arrays.stream(board).forEach(row -> Arrays.fill(row, EMPTY));
        String[] row0 = {EMPTY, EMPTY, EMPTY, PieceEnum.CHU.getGoteId(), PieceEnum.SUI.getGoteId(), PieceEnum.DAI.getGoteId(), EMPTY, EMPTY, EMPTY};
        String[] row1 = {EMPTY, PieceEnum.UMA.getGoteId(), PieceEnum.YUMI.getGoteId(), EMPTY, PieceEnum.YARI.getGoteId(), EMPTY, PieceEnum.YUMI.getGoteId(), PieceEnum.SHINOBI.getGoteId(), EMPTY};
        String[] row2 = {PieceEnum.HEI.getGoteId(), EMPTY, PieceEnum.TORIDE.getGoteId(), PieceEnum.SAMURAI.getGoteId(), PieceEnum.HEI.getGoteId(), PieceEnum.SAMURAI.getGoteId(), PieceEnum.TORIDE.getGoteId(), EMPTY, PieceEnum.HEI.getGoteId()};
        String[] row6 = {PieceEnum.HEI.getSenteId(), EMPTY, PieceEnum.TORIDE.getSenteId(), PieceEnum.SAMURAI.getSenteId(), PieceEnum.HEI.getSenteId(), PieceEnum.SAMURAI.getSenteId(), PieceEnum.TORIDE.getSenteId(), EMPTY, PieceEnum.HEI.getSenteId()};
        String[] row7 = {EMPTY, PieceEnum.SHINOBI.getSenteId(), PieceEnum.YUMI.getSenteId(), EMPTY, PieceEnum.YARI.getSenteId(), EMPTY, PieceEnum.YUMI.getSenteId(), PieceEnum.UMA.getSenteId(), EMPTY};
        String[] row8 = {EMPTY, EMPTY, EMPTY, PieceEnum.DAI.getSenteId(), PieceEnum.SUI.getSenteId(), PieceEnum.CHU.getSenteId(), EMPTY, EMPTY, EMPTY};
        String[] rowEmpty = {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY};
        board[0] = row0;
        board[1] = row1;
        board[2] = row2;
        board[3] = rowEmpty;
        board[4] = rowEmpty;
        board[5] = rowEmpty;
        board[6] = row6;
        board[7] = row7;
        board[8] = row8;

        String[] hand = {PieceEnum.YARI.getGoteId(), PieceEnum.YARI.getSenteId(), PieceEnum.SAMURAI.getGoteId(), PieceEnum.SAMURAI.getSenteId()};

        return new BoardDto(board, hand);
    }

    public boolean isCheckMate(BoardDto boardDto, boolean playerTurn) {
        // TODO 詰みがあるかチェック
        // 盤面の相手の駒の移動可能範囲がないこと
        // 手持ちの駒の移動可能範囲がないこと
        return false;
    }

    public int[][] getCanMoveRangeExcludeOte(FromPositionDto fromDto, String[][] board, boolean playerTurn) {
        int[][] moveRange = null;
        if (Objects.isNull(fromDto.pieceIdForHand)) {
            moveRange = getCanMoveRange(fromDto.x, fromDto.y, board, playerTurn);
        } else {
            moveRange = getCanMoveRangeForHand(board, playerTurn);
        }

        // 王手になる手を除く
        for (int toY = 0; toY < moveRange.length; toY++) {
            for (int toX = 0; toX < moveRange[toY].length; toX++) {
                if (moveRange[toY][toX] == MOVE_RANGE_MOVE_ONLY) {
                    // 移動のみのパターン
                    if (checkOteAfterMove(board, fromDto, toX, toY, playerTurn, ACTION_MOVE)) {
                        moveRange[toY][toX] = MOVE_RANGE_NOT_MOVE;
                    }
                } else if (moveRange[toY][toX] == MOVE_RANGE_CAN_TAKE) {
                    // 取るパターン
                    if (checkOteAfterMove(board, fromDto, toX, toY, playerTurn, ACTION_TAKE)) {
                        moveRange[toY][toX] = MOVE_RANGE_NOT_MOVE;
                    }
                } else if (moveRange[toY][toX] == MOVE_RANGE_CAN_TSUKE) {
                    // ツケるパターン
                    if (checkOteAfterMove(board, fromDto, toX, toY, playerTurn, ACTION_TSUKE)) {
                        moveRange[toY][toX] = MOVE_RANGE_NOT_MOVE;
                    }
                } else if (moveRange[toY][toX] == MOVE_RANGE_CAN_TAKE_TSUKE) {
                    // 取るかツケるパターン
                    boolean canTake = !checkOteAfterMove(board, fromDto, toX, toY, playerTurn, ACTION_TAKE);
                    boolean canTsuke = !checkOteAfterMove(board, fromDto, toX, toY, playerTurn, ACTION_TSUKE);
                    if (!canTake && !canTsuke) {
                        // どちらのアクションでも王手になるので移動不可
                        moveRange[toY][toX] = MOVE_RANGE_NOT_MOVE;
                    } else if (canTake && canTsuke) {
                        moveRange[toY][toX] = MOVE_RANGE_CAN_TAKE_TSUKE;
                    } else if (canTake) {
                        moveRange[toY][toX] = MOVE_RANGE_CAN_TAKE;
                    } else {
                        moveRange[toY][toX] = MOVE_RANGE_CAN_TSUKE;
                    }
                }
            }
        }
        return moveRange;
    }

    // 動作可能範囲を返す(手持ちを打つ場合)
    public int[][] getCanMoveRangeForHand(String[][] board, boolean playerTurn) {
        // 一番前にいる自分の駒を探す。
        int[][] result = new int[9][9];
        int minYExitsMyPiece = 8;
        int maxYExitsMyPiece = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (!(EMPTY).equals(board[y][x]) && checkMyself(x, y, board, playerTurn)) {
                    minYExitsMyPiece = Math.min(minYExitsMyPiece, y);
                    maxYExitsMyPiece = Math.max(maxYExitsMyPiece, y);
                    break;
                }
            }
        }
        int myPieceLine = playerTurn ? minYExitsMyPiece : maxYExitsMyPiece;
        if (playerTurn) {
            for (int y = 8; y >= myPieceLine; y--) {
                for (int x = 0; x < board[y].length; x++) {
                    if (!board[y][x].equals(EMPTY)) {
                        // 師でない
                        if (PieceEnum.SUI.getId().equals(Objects.requireNonNull(PieceEnum.getById(getTopPiece(board[y][x]))).getId())) {
                            continue;
                        }
                        // 相手駒があるところには置けない、自分の駒がある場合ツケる
                        result[y][x] = checkMyself(x, y, board, playerTurn) ? MOVE_RANGE_CAN_TSUKE : MOVE_RANGE_NOT_MOVE;
                    } else {
                        result[y][x] = MOVE_RANGE_MOVE_ONLY;
                    }
                }
            }
        } else {
            for (int y = 0; y <= myPieceLine; y++) {
                for (int x = 0; x < board[y].length; x++) {
                    if (!board[y][x].equals(EMPTY)) {
                        // 師でない
                        if (PieceEnum.SUI.getId().equals(Objects.requireNonNull(PieceEnum.getById(getTopPiece(board[y][x]))).getId())) {
                            continue;
                        }
                        // 相手駒があるところには置けない、自分の駒がある場合ツケる
                        result[y][x] = checkMyself(x, y, board, playerTurn) ? MOVE_RANGE_CAN_TSUKE : MOVE_RANGE_NOT_MOVE;
                    } else {
                        result[y][x] = MOVE_RANGE_MOVE_ONLY;
                    }
                }
            }
        }
        return result;
    }

    // 動作可能範囲を返す
    public int[][] getCanMoveRange(int x, int y, String[][] board, boolean palyerTurn) {
        String pieceId = board[y][x];
        int level = getLevel(pieceId);
        String topPieceId = getTopPiece(pieceId);

        int[][] result = new int[9][9];
        Piece piece = PieceEnum.getPieceById(topPieceId);
        if (Objects.isNull(piece)) {
            return result;
        }
        List<MoveRange> moveRangeList = piece.moveRangeList;
        if (level == 2) {
            moveRangeList = piece.moveRangeList2;
        } else if (level == 3) {
            moveRangeList = piece.moveRangeList3;
        }
        // 通常範囲
        if (Objects.nonNull(moveRangeList)) {
            moveRangeList.forEach(moveRange -> {
                int toX = x;
                int toY = y;
                for (int i = 0; i < moveRange.distance; i++) {
                    toX -= moveRange.direct.x;
                    toY -= moveRange.direct.y;
                    if (toX >= 0 && toX < 9 && toY >= 0 && toY < 9) {
                        // 駒がある場合は駒によってアクションが変わる
                        if (!board[toY][toX].equals(EMPTY)) {
//                            // 相手の師である場合、処理の都合上移動可能とする
                            if (PieceEnum.SUI.equals(PieceEnum.getById(board[toY][toX]))
                                    && !checkMyself(toX, toY, board, palyerTurn)) {
                                result[toY][toX] = MOVE_RANGE_CAN_TAKE;
                                break;
                            }
                            // 取れるかどうかの判定
                            // レベルが自分以下である && 自分の駒でない
                            boolean canTake = getLevel(board[toY][toX]) <= level
                                    && !checkMyself(toX, toY, board, palyerTurn);
                            // ツケが可能かの判定
                            // レベルが自分以下である && 駒レベルが最大未満であること
                            // && 移動先が自分の師でないこと && 移動元が師ではないこと
                            boolean canTsuke = getLevel(board[toY][toX]) <= level
                                    && getLevel(board[toY][toX]) < 2
//                                    && (!PieceEnum.SUI.equals(PieceEnum.getById(board[toY][toX])))
                                    && !PieceEnum.SUI.equals(PieceEnum.getById(pieceId));

                            if (canTake && canTsuke) {
                                result[toY][toX] = MOVE_RANGE_CAN_TAKE_TSUKE;
                            } else if (canTake) {
                                result[toY][toX] = MOVE_RANGE_CAN_TAKE;
                            } else if (canTsuke) {
                                result[toY][toX] = MOVE_RANGE_CAN_TSUKE;
                            }
                            // 障害物があるとそれ以上進めない
                            break;
                        } else {
                            // 駒がない時は移動可能
                            result[toY][toX] = MOVE_RANGE_MOVE_ONLY;
                        }
                    }
                }
            });
        }
        return result;
    }

    private boolean checkOteAfterMove(String[][] board
            , FromPositionDto fromDto
            , int toX
            , int toY
            , boolean playerTurn
            , int action // 0: 移動 1: 取る 2: ツケる
    ) {
        // 移動後の盤面を作成する
        BoardDto boardDto = move(fromDto, toX, toY, board, new String[0], action, playerTurn);
        if (Objects.isNull(boardDto)) {
            // イレギュラーケース
            return false;
        }
        String[][] afterMoveBoard = boardDto.getBoard();
        // 王手状態かどうかを判断する
        return checkOte(afterMoveBoard, playerTurn);
    }

    /**
     * 王手状態かどうかを判断する
     *
     * @param board
     * @param playerTurn 　先手か後手か
     * @return
     */
    private boolean checkOte(String[][] board, boolean playerTurn) {
        // 自分の師の位置を取得する
        int[] mySuiPosition = getMySuiPosition(board, playerTurn);
        // 自分の師が見つからない場合はイレギュラー
        if (mySuiPosition[0] == -1) return false;
        // 相手駒の移動可能範囲に自分の師がいるかを判定
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (!EMPTY.equals(board[y][x]) && !checkMyself(x, y, board, playerTurn)) {
                    // 相手の駒の移動可能範囲に自分の師がいるかどうか
                    int[][] canMoveRange = getCanMoveRange(x, y, board, playerTurn);
                    if (canMoveRange[mySuiPosition[1]][mySuiPosition[0]] != MOVE_RANGE_NOT_MOVE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int[] getMySuiPosition(String[][] board, boolean playerTurn) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (!EMPTY.equals(board[y][x]) && checkMyself(x, y, board, playerTurn)) {
                    if (PieceEnum.SUI.getId().equals(Objects.requireNonNull(PieceEnum.getById(getTopPiece(board[y][x]))).getId())) {
                        return new int[]{x, y};
                    }
                }
            }
        }
//        throw new RuntimeException("師が見つかりません！！");
        return new int[]{-1, -1};
    }

    private boolean checkMyself(String topPiece, boolean playerTurn) {
        if (playerTurn) {
            return PieceEnum.checkSentePiece(topPiece);
        } else {
            return !PieceEnum.checkSentePiece(topPiece);
        }
    }

    // 自分の駒かどうかを判断する
    private boolean checkMyself(int x, int y, String[][] board, boolean playerTurn) {
        if (playerTurn) {
            return PieceEnum.checkSentePiece(getTopPiece(board[y][x]));
        } else {
            return !PieceEnum.checkSentePiece(getTopPiece(board[y][x]));
        }
    }

    /**
     * ある地点から駒をある地点に移動する
     *
     * @param action 1: 取る, 2: 重ねる
     */
    public BoardDto move(FromPositionDto fromDto, int toX, int toY, String[][] board, String[] hand, int action, boolean playerTurn) {
        // 持ち駒から打ったか、盤面からの移動か
        boolean isFromHand = Objects.nonNull(fromDto.pieceIdForHand);
        String[][] boardResult = Arrays.stream(board).map(String[]::clone).toArray(String[][]::new);
        List<String> handResult = new ArrayList<>(Arrays.stream(hand).toList());
        String from = isFromHand ? fromDto.pieceIdForHand : getTopPiece(board[fromDto.y][fromDto.x]);
        String to = getTopPiece(boardResult[toY][toX]);
        // 移動先を上書く
        if (to.equals(EMPTY)) {
            // 移動先が空の時は単純な移動
            boardResult[toY][toX] = from;
        } else {
            // 相手の師が取れてしまった場合、ゲーム終了（通常はありえない)
            if (!checkMyself(to, playerTurn)
                    && PieceEnum.SUI.getId().equals(Objects.requireNonNull(PieceEnum.getById(to)).getId())) {
                return null;
            }
            // 移動先に駒があれば取るor重ねる
            if (action == ACTION_TAKE) {
                // 取る
                boardResult[toY][toX] = from;
                // ハンドに追加する
                if (PieceEnum.checkSentePiece(to)) {
                    handResult.add(Objects.requireNonNull(PieceEnum.getById(to)).getGoteId());
                } else {
                    handResult.add(Objects.requireNonNull(PieceEnum.getById(to)).getSenteId());
                }
            } else if (action == ACTION_TSUKE) {
                // 重ねる
                boardResult[toY][toX] = from + OVERLAP + to;
            }
        }
        // 移動元の駒を空にする
        if (isFromHand) {
            // 手持ちから削除
            handResult.stream().filter(pieceId -> pieceId.equals(fromDto.pieceIdForHand)).findFirst().ifPresent(handResult::remove);
        } else {
            // 盤面から削除
            String fromUnder = getUnderPiece(board[fromDto.y][fromDto.x]);
            boardResult[fromDto.y][fromDto.x] = fromUnder;
        }
        return new BoardDto(boardResult, handResult.toArray(new String[0]));
    }

    private int getLevel(String pieceId) {
        switch (pieceId.length()) {
            case 2 -> {
                return 1;
            }
            case 5 -> {
                return 2;
            }
            case 8 -> {
                return 3;
            }
            default -> {
                return 0;
            }
        }
    }

    private String getTopPiece(String pieceId) {
        // SU-he-so -> so
        // he-so -> so
        // so -> so
        // " " -> " "
        if (EMPTY.equals(pieceId)) return EMPTY;
        return pieceId.substring(0, 2);
    }

    private String getUnderPiece(String pieceId) {
        // SU-he-so -> he-so
        // he-so -> so
        // so -> " "
        // " " -> " "
        int level = getLevel(pieceId);
        return switch (level) {
            case 2, 3 -> pieceId.substring(pieceId.indexOf(OVERLAP) + 1);
            default -> EMPTY;
        };
    }
}