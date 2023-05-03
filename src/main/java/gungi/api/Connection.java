package gungi.api;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Connection {
    private Connection() {
    }

    // マッチング中のルームIDとユーザーIDリスト
    public static Map<Long, List<Integer>> matchingMap = new HashMap<>();
    // マッチング待ちルームIDとユーザーID
    public static Deque<Integer> waitingDeque = new LinkedList<>();
    // 再戦待ちユーザーIDリスト
    public static List<Integer> rematchList = new ArrayList<>();

    public static void addMatchingMap(Long roomId, List<Integer> userIdList) {
        matchingMap.put(roomId, userIdList);
    }

    public static void addRematchList(Integer userId) {
        rematchList.add(userId);
    }

    // 再戦希望かどうかを確認する
    public static boolean checkRematchUser(Integer userId) {
        return rematchList.stream().anyMatch(e -> e.equals(userId));
    }

    public static void removeRematchUser(Integer userId) {
        rematchList.removeIf(e -> e.equals(userId));
    }

    public static void deleteConnection(int userId) {
        // マッチング中ルーム削除
        Long roomId = matchingMap.entrySet().stream()
                .filter(e -> e.getValue().contains(userId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        if (Objects.nonNull(roomId)) {
            matchingMap.remove(roomId);
        }
        // マッチング待ちユーザー削除
        waitingDeque.remove(userId);
    }

    public static Integer getOpponentUserId(int userId) {
        List<Integer> matchingUserIdList = matchingMap.values().stream()
                .filter(integers -> integers.contains(userId))
                .findFirst().orElse(List.of());
        if (matchingUserIdList.isEmpty()) {
            return null;
        }
        return matchingUserIdList.stream()
                .filter(integer -> userId != integer)
                .findFirst().orElse(null);
    }

    /**
     * マッチング待ちユーザーとマッチングさせる
     *
     * @return 対戦相手のユーザーID
     */
    public static Integer matching(Integer userId) {
        if (waitingDeque.isEmpty()) {
            waitingDeque.add(userId);
            return null;
        }
        int waitingUserId = waitingDeque.poll();
        addMatchingMap(createRoomId(), Arrays.asList(waitingUserId, userId));
        return waitingUserId;
    }

    private static long createRoomId() {
        return (long) (Math.random() * 100000);
    }
}
