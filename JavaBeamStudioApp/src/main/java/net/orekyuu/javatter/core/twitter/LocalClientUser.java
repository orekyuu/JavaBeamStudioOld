package net.orekyuu.javatter.core.twitter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.twitter.stream.JavatterStream;
import net.orekyuu.javatter.core.twitter.stream.JavatterStreamImpl;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamAdapter;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

/**
 * ローカルに保存されているユーザー
 */
public class LocalClientUser implements ClientUser {

    private AccessToken token;
    private Twitter twitter;
    private JavatterStreamImpl stream;
    private static final String DATABASE_NAME = "users.db";
    private String name;

    public LocalClientUser(AccessToken token) {
        this.token = token;
        authentication();
    }

    @Override
    public AccessToken getAccessToken() {
        return token;
    }

    @Override
    public String getName() {
        try {
            if (name == null) {
                name = twitter.getScreenName();
            }
            return name;
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Twitter getTwitter() {
        return twitter;
    }

    @Override
    public JavatterStream getStream() {
        return stream;
    }

    private void authentication() {
        ConfigurationBuilder conf = new ConfigurationBuilder().setOAuthConsumerKey("rMvLmU5qMgbZwg92Is5g").setOAuthConsumerSecret("RD28Uuu44KeMOs90UuqXAAoVTWXRTmD4H8xYKZSgBk").setOAuthAccessToken(token.getToken()).setOAuthAccessTokenSecret(token.getTokenSecret());
        twitter = new TwitterFactory(conf.build()).getInstance();

        stream = new JavatterStreamImpl();
        UserStreamAdapter adapter = new UserStreamAdapter() {
            @Override
            public void onException(Exception ex) {
                stream.onException(ex);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                stream.onScrubGeo(userId, upToStatusId);
            }

            @Override
            public void onUserProfileUpdate(User updatedUser) {
                stream.onUserProfileUpdate(updatedUser);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                stream.onDeletionNotice(statusDeletionNotice);
            }

            @Override
            public void onStatus(Status status) {
                stream.onStatus(status);
            }

            @Override
            public void onBlock(User source, User blockedUser) {
                stream.onBlock(source, blockedUser);
            }

            @Override
            public void onFavorite(User source, User target, Status favoritedStatus) {
                stream.onFavorite(source, target, favoritedStatus);
            }

            @Override
            public void onFollow(User source, User followedUser) {
                stream.onFollow(source, followedUser);
            }

            @Override
            public void onUnblock(User source, User unblockedUser) {
                stream.onUnblock(source, unblockedUser);
            }

            @Override
            public void onUnfollow(User source, User unfollowedUser) {
                stream.onUnfollow(source, unfollowedUser);
            }

            @Override
            public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
                stream.onUnfavorite(source, target, unfavoritedStatus);
            }

            @Override
            public void onDirectMessage(DirectMessage directMessage) {
                stream.onDirectMessage(directMessage);
            }

            @Override
            public void onUserListUpdate(User listOwner, UserList list) {
                stream.onUserListUpdate(listOwner, list);
            }

            @Override
            public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
                stream.onUserListSubscription(subscriber, listOwner, list);
            }

            @Override
            public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
                stream.onUserListUnsubscription(subscriber, listOwner, list);
            }

            @Override
            public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
                stream.onUserListMemberAddition(addedMember, listOwner, list);
            }

            @Override
            public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
                stream.onUserListMemberDeletion(deletedMember, listOwner, list);
            }

            @Override
            public void onUserListDeletion(User listOwner, UserList list) {
                stream.onUserListDeletion(listOwner, list);
            }

            @Override
            public void onUserListCreation(User listOwner, UserList list) {
                stream.onUserListCreation(listOwner, list);
            }
        };
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(adapter);
        twitterStream.setOAuthConsumer("rMvLmU5qMgbZwg92Is5g", "RD28Uuu44KeMOs90UuqXAAoVTWXRTmD4H8xYKZSgBk");
        twitterStream.setOAuthAccessToken(getAccessToken());
        twitterStream.sample();
    }

    /**
     * ローカルのデータベースへユーザーを保存します。
     */
    public void save() {
        ConnectionSource connectionSource = null;
        TokenTable table = new TokenTable();
        table.token = token.getToken();
        table.tokenSecret = token.getTokenSecret();
        try {
            makeFile(DATABASE_NAME);
            connectionSource = makeConnectionSource();
            Dao<TokenTable, ?> dao = setUpDataBase(connectionSource);
            dao.create(table);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * データベース上からユーザーの認証情報を削除します。
     */
    public void delete() {
        ConnectionSource connectionSource = null;
        TokenTable table = new TokenTable();
        table.token = token.getToken();
        table.tokenSecret = token.getTokenSecret();
        try {
            makeFile(DATABASE_NAME);
            connectionSource = makeConnectionSource();
            Dao<TokenTable, ?> dao = setUpDataBase(connectionSource);
            DeleteBuilder<TokenTable, ?> tokenTableDeleteBuilder = dao.deleteBuilder();
            tokenTableDeleteBuilder.where().eq("token", token.getToken())
                    .and().eq("tokenSecret", token.getTokenSecret());
            tokenTableDeleteBuilder.delete();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ローカルのデータベースからユーザーを読み込みます。
     *
     * @return データベースから読み込んだユーザーのリスト
     */
    public static List<ClientUser> loadClientUsers() {
        LinkedList<ClientUser> users = new LinkedList<>();
        ConnectionSource connectionSource = null;
        try {
            connectionSource = makeConnectionSource();
            Dao<TokenTable, ?> dao = setUpDataBase(connectionSource);
            dao.queryForAll().stream()
                    .map(t -> new LocalClientUser(new AccessToken(t.token, t.tokenSecret)))
                    .forEach(users::add);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return users;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LocalClientUser{");
        sb.append("token=").append(token);
        sb.append('}');
        return sb.toString();
    }

    private static void makeFile(String s) throws IOException {
        File file = new File(s);
        if (!file.exists()) {
            file.createNewFile();
        }

    }

    private static ConnectionSource makeConnectionSource() throws SQLException {
        return new JdbcPooledConnectionSource("jdbc:sqlite:" + DATABASE_NAME);
    }

    private static Dao<TokenTable, ?> setUpDataBase(ConnectionSource connctionSource) throws SQLException {
        Dao<TokenTable, ?> dao = DaoManager.createDao(connctionSource, TokenTable.class);
        TableUtils.createTableIfNotExists(connctionSource, TokenTable.class);
        return dao;
    }

    @DatabaseTable(tableName = "tokenData")
    private static class TokenTable {

        @DatabaseField(columnName = "token", canBeNull = false, unique = true)
        String token;
        @DatabaseField(columnName = "tokenSecret", canBeNull = false, unique = true)
        String tokenSecret;

    }

}
