package sourcetalk.continuousdelivery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseFriendRepository implements FriendRepository {
    private Driver driver = new org.h2.Driver();

    @Override
    public List<Friend> getFriends() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM friends")) {
                    List<Friend> friends = new ArrayList<>();

                    while (resultSet.next()) {
                        friends.add(new Friend(resultSet.getString("FIRSTNAME"), resultSet.getString("LASTNAME")));
                    }

                    return friends;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to query database", e);
        }
    }

    private Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "SA");
        properties.setProperty("password", "");
        return driver.connect(Environment.getConfiguration().getDbUrl(), properties);
    }

    @Override
    public void addFriend(Friend newFriend) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 "INSERT INTO friends (firstname, lastname) VALUES (?, ?)")) {
                statement.setString(1, newFriend.getFirstName());
                statement.setString(2, newFriend.getLastName());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to insert into database", e);
        }
    }

    @Override
    public int getFriendCount() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM friends")) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to query database", e);
        }
    }
}
