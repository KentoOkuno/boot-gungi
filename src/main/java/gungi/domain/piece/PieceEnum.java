package gungi.domain.piece;

import java.util.Objects;

public enum PieceEnum {
    HEI(new Hei(), "he"),
    TORIDE(new Toride(), "to"),
    UMA(new Uma(), "um"),
    SHINOBI(new Shinobi(), "sn"),
    SAMURAI(new Samurai(), "sa"),
    YARI(new Yari(), "ya"),
    YUMI(new Yumi(), "yu"),
    SHO(new Sho(), "so"),
    CHU(new Chu(), "ch"),
    DAI(new Dai(), "da"),
    SUI(new Sui(), "su");
    private Piece piece;
    private String id;

    PieceEnum(Piece piece, String id) {
        this.piece = piece;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static PieceEnum getById(String id) {
        if (Objects.isNull(id)) {
            return null;
        }
        for (PieceEnum pieceEnum : PieceEnum.values()) {
            if (pieceEnum.getId().equals(id.toLowerCase())) {
                return pieceEnum;
            }
        }
        return null;
    }

    public static Piece getPieceById(String id) {
        PieceEnum pieceEnum = getById(id);
        return Objects.isNull(pieceEnum) ? null : pieceEnum.piece;
    }


    /**
     * 先手番の駒IDを返す
     *
     * @return
     */
    public String getSenteId() {
        return getId().toUpperCase();
    }

    /**
     * 後手番の駒IDを返す
     *
     * @return
     */
    public String getGoteId() {
        return getId().toLowerCase();
    }

    /**
     * 先手番の駒かどうかを返す
     *
     * @param id
     * @return
     */
    public static boolean checkSentePiece(String id) {
        if (Objects.isNull(id) || Objects.isNull(getById(id))) {
            return false;
        }
        return Objects.requireNonNull(getById(id)).getId().toUpperCase().equals(id);
    }
}
