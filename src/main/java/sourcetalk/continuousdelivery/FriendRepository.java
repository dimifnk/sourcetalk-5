package sourcetalk.continuousdelivery;

import java.util.List;

public interface FriendRepository {
    List<Friend> getFriends();

    void addFriend(Friend newFriend);

    int getFriendCount();
}
