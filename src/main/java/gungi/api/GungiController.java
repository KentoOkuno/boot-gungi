package gungi.api;

import gungi.api.request.ConnectionRequest;
import gungi.api.request.MoveRangeForHandRequest;
import gungi.api.request.MoveRequest;
import gungi.api.response.*;
import gungi.dto.BoardDto;
import gungi.dto.FromPositionDto;
import gungi.exception.GungiException;
import gungi.service.GungiService;
import gungi.api.request.MoveRangeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Controller
public class GungiController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private GungiService gungiService;

    /**
     * 開始(対局マッチング）
     *
     * @return
     */
    @MessageMapping("/setup")
//    @SendTo("/client/update")
    public void boardSetup(ConnectionRequest request) {
        Integer opponentUserId = Connection.matching(request.userId());
        // 先攻後攻はランダム
        boolean playerTurn = Math.random() % 2 == 0;
        if (Objects.isNull(opponentUserId)) {
            send(request.userId(), new GungiWaitingResponse(request.userId()));
        } else {
            // TODO ボード情報を渡す
            BoardDto boardDto = gungiService.setup();
            GungiBoardResponse setup = new GungiBoardResponse(boardDto.getBoard(), boardDto.getHand(), null, null);
            send(request.userId(), new GungiGameResponse(request.userId(), opponentUserId, playerTurn, setup));
            send(opponentUserId, new GungiGameResponse(opponentUserId, request.userId(), !playerTurn, setup));
        }
    }

    /**
     * 再戦
     */
    @MessageMapping("/reMatch")
    public void reMatch(ConnectionRequest request) {
        Integer opponentUserId = Connection.getOpponentUserId(request.userId());
        boolean playerTurn = Math.random() % 2 == 0;
        if (Objects.isNull(opponentUserId)) {
            // 異常系　ルームが存在しない or 対戦相手がいない
            Connection.deleteConnection(request.userId());
            send(request.userId(), new GungiDisconnectResponse());
        }
        // 相手が再戦OKか
        if (Connection.checkRematchUser(opponentUserId)) {
            BoardDto boardDto = gungiService.setup();
            GungiBoardResponse setup = new GungiBoardResponse(boardDto.getBoard(), boardDto.getHand(), null, null);
            send(request.userId(), new GungiGameResponse(request.userId(), opponentUserId, playerTurn, setup));
            send(opponentUserId, new GungiGameResponse(opponentUserId, request.userId(), !playerTurn, setup));
        } else {
            // 相手の許可待ち
            send(request.userId(), new GungiWaitingResponse(request.userId()));
        }
    }

    /**
     * 接続終了
     */
    @MessageMapping("/disconnect")
    public void disconnect(ConnectionRequest request) {
        // 対戦相手特定
        Integer opponentUserId = Connection.getOpponentUserId(request.userId());
        // コネクション削除
        Connection.deleteConnection(request.userId());

        send(request.userId(), new GungiDisconnectResponse());
        if (Objects.nonNull(opponentUserId)) {
            send(opponentUserId, new GungiDisconnectResponse());
        }
    }

    /**
     * 降参
     */
    @MessageMapping("/surrender")
    public void surrender(ConnectionRequest request) {
        Integer opponentUserId = Connection.getOpponentUserId(request.userId());
        if (Objects.isNull(opponentUserId)) {
            // 異常系　ルームが存在しない or 対戦相手がいない
            Connection.deleteConnection(request.userId());
            send(request.userId(), new GungiDisconnectResponse());
        }
        // 試合結果を返す
        send(request.userId(), new GungiResultResponse(opponentUserId));
        send(opponentUserId, new GungiResultResponse(opponentUserId));
    }

    /**
     * 移動可能範囲を返す
     *
     * @param request
     * @return
     */
    @MessageMapping("/getMoveRange")
    public void getMoveRange(MoveRangeRequest request) {
        Integer opponentUserId = Connection.getOpponentUserId(request.userId());
        if (Objects.isNull(opponentUserId)) {
            // 異常系　ルームが存在しない or 対戦相手がいない
            Connection.deleteConnection(request.userId());
            send(request.userId(), new GungiDisconnectResponse());
        }
        if (request.board()[request.y()][request.x()].equals(" ")) {
            // 空白の時は何もしない
            send(request.userId(), new GungiErrorResponse("バリエーションエラー: 空白のマスを選択しています"));
        } else {
            int[][] moveRange = gungiService.getCanMoveRangeExcludeOte(new FromPositionDto(request.x(), request.y(), null)
                    , request.board()
                    , request.playerTurn());
            send(request.userId(), new GungiMoveRangeResponse(moveRange));
        }
    }

    /**
     * 移動可能範囲を返す(手持ちを打つ場合）
     *
     * @param request
     * @return
     */
    @MessageMapping("/getMoveRangeForHand")
    public void getMoveRangeForHand(MoveRangeForHandRequest request) {
        Integer opponentUserId = Connection.getOpponentUserId(request.userId());
        if (Objects.isNull(opponentUserId)) {
            // 異常系　ルームが存在しない or 対戦相手がいない
            Connection.deleteConnection(request.userId());
            send(request.userId(), new GungiDisconnectResponse());
        }
        if (Objects.isNull(request.pieceId())) {
            // 空白の時は何もしない
            send(request.userId(), new GungiErrorResponse("バリエーションエラー: 空白のマスを選択しています"));
        } else {
            int[][] moveRange = gungiService.getCanMoveRangeExcludeOte(new FromPositionDto(null, null, request.pieceId())
                    , request.board()
                    , request.playerTurn());
            send(request.userId(), new GungiMoveRangeResponse(moveRange));
        }
    }

    /**
     * 移動後を返す
     *
     * @param request
     * @return
     */
    @MessageMapping("/move")
    public void move(MoveRequest request) {
        Integer opponentUserId = Connection.getOpponentUserId(request.userId());
        if (Objects.isNull(opponentUserId)) {
            // 異常系　ルームが存在しない or 対戦相手がいない
            Connection.deleteConnection(request.userId());
            send(request.userId(), new GungiDisconnectResponse());
        } else {
            FromPositionDto fromDto = new FromPositionDto(request.fromX(), request.fromY(), request.toPieceIdForHand());
            BoardDto boardDto = gungiService.move(fromDto, request.toX(), request.toY(), request.board(), request.hand(), request.action(), request.playerTurn());
            // 詰みがあるかチェックする
            if (gungiService.isCheckMate(boardDto, request.playerTurn())) {
                // 試合結果を返す
                send(request.userId(), new GungiResultResponse(request.userId()));
                send(opponentUserId, new GungiResultResponse(request.userId()));
            }
            send(request.userId(), new GungiBoardResponse(boardDto.getBoard(), boardDto.getHand(), request.toX(), request.toY()));
            send(opponentUserId, new GungiBoardResponse(boardDto.getBoard(), boardDto.getHand(), request.toX(), request.toY()));
        }
    }

    /**
     * TODO 多分動いてないので後で確認する
     * 異常系
     *
     * @param e
     * @return
     */
    @ExceptionHandler(GungiException.class)
    public ResponseEntity<ErrorResponse> handleException(GungiException e) {
        // エラー内容を送信(脆弱性だから普通はダメ)
        if (Objects.nonNull(e.userId)) {
            send(e.userId, new GungiErrorResponse(e.getMessage()));
        }
        if (Objects.nonNull(e.opponentUserId)) {
            send(e.opponentUserId, new GungiErrorResponse(e.getMessage()));
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * WebSocket通信を実行する
     */
    public void send(int userId, Object response) {
        final String url = "/client/update/" + userId;
        simpMessagingTemplate.convertAndSend(url, response);
    }

}
