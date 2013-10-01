package sourcetalk.continuousdelivery;

import java.util.ArrayList;
import java.util.List;

public class InMemoryFriendRepository implements FriendRepository {
    private final List<Friend> friends = new ArrayList<>();

    public InMemoryFriendRepository() {
        friends.add(new Friend("Homer", "Simpson"));
        friends.add(new Friend("Ned", "Flanders"));
        friends.add(new Friend("Montgomery", "Burns"));
    }

    @Override
    public List<Friend> getFriends() {
        return friends;
    }

    @Override
    public void addFriend(Friend newFriend) {
        friends.add(newFriend);
    }
}
