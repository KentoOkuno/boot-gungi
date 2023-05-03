package gungi.exception;

public class GungiException extends RuntimeException {
    public Integer userId;
    public Integer opponentUserId;

    public GungiException(Integer userId, Integer opponentUserId, Throwable e) {
        super(e);
        this.userId = userId;
        this.opponentUserId = opponentUserId;
    }
}
