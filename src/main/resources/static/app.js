var stompClient = null;
var MODE = "select";// select or moveRange
var MOVE_RANGE = []; // 0: 移動不可 1: 移動可能 2: 取るのみ 3: ツケるのみ 4: 取るとツケる選択可能
var BOARD = [];
var HAND = [];
var SELECTED_POSITION = {};// {x: 0, y: 0} //x: col, y: row 盤の駒を選択した場合
var SELECTED_HAND = ""; // 手持ちの駒を選択した場合、PieceIDを持つ
var PLAYER_TURN = false;
var me = null;// sente or gote
var USER_ID = "";
var LAST_MOVED_POSITION = {}// {x: 0, y: 0} //x: col, y: row

function connect() {
    // 一意のユーザーIDを採番
    const userId = Math.floor(Math.random() * 1000000000);
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/client/update/' + userId, function (response) {
            var response = JSON.parse(response.body);
            console.log("response", response);
            if (response.mode === "waiting") {
                $("#board").text("対戦相手を待っています...");
            }
            if (response.mode === "matched") {
                PLAYER_TURN = response.playerTurn;
                if (PLAYER_TURN) {
                    me = "sente";
                    $("#message").text("あなたの手番です");
                    $("#game").addClass("my-turn");
                    $("#game").addClass("sente");
                } else {
                    me = "gote";
                    $("#message").text("相手の手番です");
                    $("#game").addClass("opponent-turn");
                    $("#game").addClass("gote");
                }
                 USER_ID = response.userId;
                 changeMode("select");
                 $('#surrender').show();
                 showPlayerName();
                 showBoard(userId, response.setup.board, response.setup.hand);
            }
            if (response.mode === "disconnect") {
                disconnect();
                return;
            }
            if (response.mode === "finished") {
                gameFinished(response.winnerUser);
            }
            if (response.mode === "select") {
                 showBoard(userId, response.board, response.hand, {x: response.lastMoveFromX, y: response.lastMoveFromY});
                 changeTurn();
            } else if(response.mode === "moveRange") {
                 showMoveRange(response.moveRange);
            }
            if (response.mode === "error") {
                console.log(response.message);
            }
        });
        setup(userId);
    });
}

function setConnected(connected) {
    if (connected) {
        $("#connect").hide();
        $("#disconnect").show();
        $("#info").show();
    } else {
        $("#connect").show();
        $("#disconnect").hide();
        $("#info").hide();
    }
    $("#board").html("");
    $("#hand").html("");
    $("#opponentHand").html("");
}

function onClickDisconnect() {
    if (!USER_ID) return false;
    stompClient.send("/app/disconnect", {}, JSON.stringify({"userId": USER_ID}));
}
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}
function gameFinished(winnerUser) {
    $("#message").text(winnerUser === USER_ID ? "あなたの勝ちです" : "あなたの負けです");
    changeMode("finish");
    $("#reMatch").show();
    $("#surrender").hide();
}
function showPlayerName() {
        $("#player1Id").text(USER_ID);
}
function showBoard(userId, board, hand, lastMovedPosition) {
    console.log("showBoard", board, hand);
    BOARD = board;
    $("#board").html("");
    $("#board").append("<table id='table' height='300px'></table>");
    board.forEach(function (row, rowIndex) {
        $("#table").append("<tr id='row" + rowIndex + "'></tr>");
        row.forEach(function (cell, colIndex) {
              const topPiece = getTopPiece(cell);
              const level = getLevel(cell);
              $("#row" + rowIndex).append(
                  "<td "
                  + "rowIndex='" + rowIndex + "' "
                  + "colIndex='" + colIndex + "' "
                  + "cell='" + cell + "' "
                  + "onclick=clickCell(" + userId + "," + rowIndex + "," + colIndex + ")"
                  + ">"
                  + "</td>"
                  + "</tr>"
              );
              // 駒の画像表示
              if (cell !== " ") {
                 $("[rowIndex='" + rowIndex + "'][colIndex='" + colIndex + "']").append(
                   "<img src='../img/" + topPiece + ".png'/>"
                 );
                 // 駒のレベル
                 $("[rowIndex='" + rowIndex + "'][colIndex='" + colIndex + "']").addClass("level" + level);
                  // 自分の駒と相手の駒の区別
                  if (checkMySelf(topPiece)) {
                     $("[rowIndex='" + rowIndex + "'][colIndex='" + colIndex + "']").addClass("myself");
                  } else {
                     $("[rowIndex='" + rowIndex + "'][colIndex='" + colIndex + "']").addClass("opponent");
                  }
              } else {
                 $("[rowIndex='" + rowIndex + "'][colIndex='" + colIndex + "']").addClass("empty");
              }
              // 最後に移動された場所のスタイルセット
                if (lastMovedPosition !== undefined && lastMovedPosition.x === colIndex && lastMovedPosition.y === rowIndex) {
                    $("[rowIndex='" + rowIndex + "'][colIndex='" + colIndex + "']").addClass("last-moved");
                }
        })
    })
    // 手持ちのセット
    HAND = hand;
    $("#myHand").html("");
    $("#opponentHand").html("");
    if (hand) {
        hand.forEach(function (piece) {
          const handArea =  checkMySelf(piece) ? "#myHand" : "#opponentHand";
          if ($(handArea).find("[piece='" + piece + "']").length === 0) {
            $(handArea).append(
              "<div hand-num='1' piece=" + piece
              + " onclick=clickHand('" + userId + "','" + piece + "')"
              + " class='hand-piece'"
              + ">"
              + "<img src='../img/" + piece + ".png'/>"
              + "</div>"
            );
          } else {
            const handNum = $(handArea).find("[piece='" + piece + "']").attr("hand-num");
            $(handArea).find("[piece='" + piece + "']").attr("hand-num", Number(handNum) + 1);
          }
        });
    }
    // 選択モードへ
    changeMode("select");
}
function checkMySelf(piece) {
    const topPiece = getTopPiece(piece);
    return me === "sente" ? topPiece.toUpperCase() === topPiece : topPiece.toLowerCase() === topPiece;
}
function showMoveRange(moveRange) {
    console.log("showMoveRange", moveRange);
    MOVE_RANGE = moveRange;
    let canMove = false;
    // 移動可能範囲を表示
    moveRange.forEach(function (row, rowIndex) {
         row.forEach(function (cell, colIndex) {
            if (cell !== 0) {
                canMove = true;
                $("[rowIndex='" + rowIndex + "'][colIndex='" + colIndex + "']").addClass("canMove");
            } else {
                $("[rowIndex='" + rowIndex + "'][colIndex='" + colIndex + "']").addClass("notMove");
            }
         })
    });
    // 移動可能箇所があれば、移動モードへ
    if (canMove) {
        changeMode("moveRange");
        // クリックセルの色変更
        if (SELECTED_POSITION.x !== undefined) {
            $("[rowIndex='" + SELECTED_POSITION.y + "'][colIndex='" + SELECTED_POSITION.x + "']").addClass("clicked-cell");
        } else if(SELECTED_HAND) {
            $("#myHand").find("[piece='" + SELECTED_HAND + "']").addClass("clicked-cell");
        }
    } else {
        console.log("移動可能範囲がありません");
    }
}
function resetMoveRange() {
    MOVE_RANGE.forEach(function (row, rowIndex) {
        row.forEach(function (cell, colIndex) {
            $("[rowIndex='" + rowIndex + "'][colIndex='" + colIndex + "']").removeClass("canMove");
            $("[rowIndex='" + rowIndex + "'][colIndex='" + colIndex + "']").removeClass("notMove");
        })
    });
    $(".clicked-cell").removeClass("clicked-cell");
}
function changeMode(mode) {
    if (mode === "select") {
        MODE = "select";
        $("#game").addClass("mode-select");
        $("#game").removeClass("mode-move");
    } else if (mode === "moveRange") {
        MODE = "moveRange";
        $("#game").addClass("mode-move");
        $("#game").removeClass("mode-select");
    } else if (mode === "finish") {
        MODE = "finish";
        $("#game").addClass("mode-finish");
        $("#game").removeClass("mode-select");
        $("#game").removeClass("mode-move");
    }
}
function changeTurn() {
    PLAYER_TURN = !PLAYER_TURN;
    if (PLAYER_TURN) {
        $("#message").text("あなたの手番です");
        $("#game").addClass("my-turn");
        $("#game").removeClass("opponent-turn");
    } else {
        $("#message").text("相手の手番です");
        $("#game").addClass("opponent-turn");
        $("#game").removeClass("my-turn");
    }
}
function clickCell(userId, rowIndex, colIndex) {
    console.log("clickCell(rowIndex, colIndex)", rowIndex, colIndex, PLAYER_TURN, SELECTED_POSITION);
    // 手番でなければクリックできない
    if (!PLAYER_TURN) {
        console.log("手番でないのでクリックできません");
        return false;
    }
    // 選択モードか否か
    if (MODE === "select") {
        if (!checkMySelf(BOARD[rowIndex][colIndex])) {
            console.log("自分の駒ではないのでクリックできません");
            return false;
        }
        // クリック位置を保存
        SELECTED_POSITION = {x: colIndex, y: rowIndex};
        // 選択モードの場合、選択した駒の移動可能範囲を取得
        stompClient.send("/app/getMoveRange", {}, JSON.stringify({'userId': userId, 'x': colIndex, 'y': rowIndex, 'board': BOARD, "playerTurn": me === "sente"}));
    } else {
        // クリック元が取れなければ移動不可
        if (SELECTED_POSITION.x === undefined && SELECTED_HAND == null) {
            console.log('クリック元が取れないので移動できません');
            // 移動不可の場合、移動可能範囲をリセット
            resetMoveRange();
            // modeを選択モードに戻す
            changeMode("select");
            return false;
        }
        // 移動モードの場合、移動可能であれば実行
        const moveRangeNum = MOVE_RANGE[rowIndex][colIndex];
        if (moveRangeNum === 0) {
            console.log("移動可能な範囲がないので移動できません");
            // 移動不可の場合、移動可能範囲をリセット
            resetMoveRange();
            // modeを選択モードに戻す
            changeMode("select");
            return false;
        }
        switch (moveRangeNum) {
            case 1:
                exeMove(userId, colIndex, rowIndex, 0);
                break;
            case 2: // 取るのみ
                exeMove(userId, colIndex, rowIndex, 1);
                break;
            case 3: // ツケるのみ
                exeMove(userId, colIndex, rowIndex, 2);
                break;
            case 4: // 取るorツケる
              // モーダルを表示して取るorツケるのアクションを決める
                $('#exe-take-button').off('click');
                $('#exe-take-button').on('click', function () {
                    console.log("取る");
                    exeMove(userId, colIndex, rowIndex, 1);
                    $('#exampleModal').modal('hide');
                });
                $('#exe-tsuke-button').off('click');
                $('#exe-tsuke-button').on('click', function () {
                console.log("ツケる", SELECTED_POSITION, rowIndex, colIndex);
                  exeMove(userId, colIndex, rowIndex, 2);
                  SELECTED_POSITION = {};
                  $('#exampleModal').modal('hide');
                });
                $('#exampleModal').modal('show');
                break;
            }
    }
}
function clickHand(userId, piece) {
   if (!checkMySelf(piece)) {
     console.log('自分の駒ではないのでクリックできません');
     return false;
   }
    // 手番でなければクリックできない
    if (!PLAYER_TURN) {
        console.log("手番でないのでクリックできません");
        return false;
    }
    if (MODE === "select") {
       // 選択モードであれば、移動可能範囲を返す。
       // 一番前にいる自分の駒以下でかつ自分の駒か空いている場所であれば打てる
        // クリック位置を保存
        SELECTED_HAND = piece;
        // 選択モードの場合、選択した駒の移動可能範囲を取得
        stompClient.send("/app/getMoveRangeForHand", {}, JSON.stringify({'userId': userId, 'pieceId': piece, 'board': BOARD, "hand": HAND, "playerTurn": me === "sente"}));
    } else {
        // 移動モードであればリセット
        SELECTED_HAND = "";
        // 移動不可の場合、移動可能範囲をリセット
        resetMoveRange();
        // modeを選択モードに戻す
        changeMode("select");
    }
}
function exeMove(userId, colIndex, rowIndex, action) {
      if (SELECTED_HAND === "") {
          // 通常の移動
          stompClient.send("/app/move", {}, JSON.stringify({'userId': userId, 'toX': colIndex, 'toY': rowIndex, 'fromX': SELECTED_POSITION.x, 'fromY': SELECTED_POSITION.y
          , 'action': action, 'board': BOARD, 'hand': HAND, "playerTurn": me === "sente"}));
      } else {
          // 手持ちの移動
          stompClient.send("/app/move", {}, JSON.stringify({'userId': userId, 'toX': colIndex, 'toY': rowIndex,'toPieceIdForHand': SELECTED_HAND
          , 'action': action, 'board': BOARD, 'hand': HAND, "playerTurn": me === "sente"}));
      }
      SELECTED_POSITION = {};
      SELECTED_HAND = "";

}
function getTopPiece(pieceStr) {
    const overLapIndex = pieceStr.indexOf("-");
    if (overLapIndex === -1) {
       return pieceStr;
    }
    // SU-so-he -> SU
    return pieceStr.substring(0, 2);
}
function getLevel(pieceStr) {
    // SU-so-he -> 3 SU-so -> 2  SU -> 1 " " -> 0
    switch (pieceStr.length) {
        case 1: return 0;
        case 2: return 1;
        case 5: return 2;
        case 8: return 3;
    }
    return 0;
}
// 軍議セットアップ
function setup(userId) {
    stompClient.send("/app/setup", {}, JSON.stringify({"userId": userId}));
}
function onClickReMatch() {
    $('#reMatch').hide();
    stompClient.send("/app/reMatch", {}, JSON.stringify({"userId": USER_ID}));
}
function onClickSurrender() {
    $('#surrender').hide();
    stompClient.send("/app/", {}, JSON.stringify({"userId": USER_ID}));
}
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { onClickDisconnect(); });
    $( "#reMatch" ).click(function() { onClickReMatch(); });
    $( "#surrender" ).click(function() { onClickSurrender(); });
});
