package net.orekyuu.javatter.core.twitter.stream;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserList;

import static org.junit.Assert.assertEquals;

public class JavatterStreamImplTest {

    @Test
    public void testOnStatus(@Mocked Status status1, @Mocked Status status2) throws Exception {
        new Expectations(status1) {{
            status1.getId();
            result = 10;
            status1.getText();
            result = "test";
        }};
        new Expectations(status2) {{
            status2.getId();
            result = 11;
            status2.getText();
            result = "test2";
        }};


        JavatterStreamImpl streamA = new JavatterStreamImpl();
        streamA.addOnStatus(s -> {
            assertEquals(s.getId(), 10);
            assertEquals(s.getText(), "test");
        });
        streamA.onStatus(status1);

        JavatterStreamImpl streamB = new JavatterStreamImpl();
        streamB.addOnStatus(s -> {
            assertEquals(s.getId(), 20);
            assertEquals(s.getText(), "test2");
        });
        streamB.onStatus(status2);
    }

    @Test
    public void testOnException() throws Exception {
        Exception e1 = new NullPointerException();
        Exception e2 = new IllegalAccessException();

        JavatterStreamImpl streamA = new JavatterStreamImpl();
        streamA.addOnException(e -> assertEquals(e.getClass().getName(), NullPointerException.class.getName()));
        streamA.onException(e1);

        JavatterStreamImpl streamB = new JavatterStreamImpl();
        streamB.addOnException(e -> assertEquals(e.getClass().getName(), IllegalAccessException.class.getName()));
        streamB.onException(e2);
    }

    @Test
    public void testOnScrubGeo() throws Exception {
        JavatterStreamImpl streamA = new JavatterStreamImpl();
        streamA.addOnScrubGeo((userID, statusID) -> {
            assertEquals(userID, Long.valueOf(1));
            assertEquals(statusID, Long.valueOf(2));
        });
        streamA.onScrubGeo(1, 2);

        JavatterStreamImpl streamB = new JavatterStreamImpl();
        streamB.addOnScrubGeo((userID, statusID) -> {
            assertEquals(userID, Long.valueOf(3));
            assertEquals(statusID, Long.valueOf(4));
        });
        streamB.onScrubGeo(3, 4);
    }

    @Test
    public void testOnProfileUpdate(@Mocked User user1, @Mocked User user2) throws Exception {
        new Expectations(user1) {{
            user1.getId();
            result = 10;
            user1.getScreenName();
            result = "test";
        }};
        new Expectations(user2) {{
            user2.getId();
            result = 11;
            user2.getScreenName();
            result = "test2";
        }};

        JavatterStreamImpl streamA = new JavatterStreamImpl();
        streamA.addOnProfileUpdate(s -> {
            assertEquals(s.getId(), 10);
            assertEquals(s.getScreenName(), "test");
        });
        streamA.onUserProfileUpdate(user1);

        JavatterStreamImpl streamB = new JavatterStreamImpl();
        streamB.addOnProfileUpdate(s -> {
            assertEquals(s.getId(), 20);
            assertEquals(s.getScreenName(), "test2");
        });
        streamB.onUserProfileUpdate(user2);
    }

    @Test
    public void testOnDeletionNotice() throws Exception {
        JavatterStreamImpl streamA = new JavatterStreamImpl();
        streamA.addOnDeletionNotice((userID, statusID) -> {
            assertEquals(userID, Long.valueOf(1));
            assertEquals(statusID, Long.valueOf(2));
        });
        streamA.onDeletionNotice(1, 2);

        JavatterStreamImpl streamB = new JavatterStreamImpl();
        streamB.addOnDeletionNotice((userID, statusID) -> {
            assertEquals(userID, Long.valueOf(3));
            assertEquals(statusID, Long.valueOf(4));
        });
        streamB.onDeletionNotice(3, 4);
    }

    @Test
    public void testBlockAndUnBlock(@Mocked User user1, @Mocked User user2) throws Exception {
        new Expectations(user1) {{
            user1.getScreenName();
            result = "test";
        }};
        new Expectations(user2) {{
            user2.getScreenName();
            result = "test2";
        }};

        JavatterStreamImpl stream = new JavatterStreamImpl();
        stream.addOnBlockUser((source, target) -> {
            assertEquals(source.getScreenName(), "test");
            assertEquals(target.getScreenName(), "test2");
        }).addOnUnBlockUser((source, target) -> {
            assertEquals(source.getScreenName(), "test");
            assertEquals(target.getScreenName(), "test2");
        });

        stream.onBlock(user1, user2);
        stream.onUnblock(user1, user2);
    }

    @Test
    public void testFavoriteAndUnFavorite(@Mocked User user1, @Mocked User user2, @Mocked Status status) throws Exception {
        new Expectations(status) {{
            status.getId();
            result = 10;
            status.getText();
            result = "test";
        }};
        new Expectations(user1) {{
            user1.getScreenName();
            result = "test";
        }};
        new Expectations(user2) {{
            user2.getScreenName();
            result = "test2";
        }};

        JavatterStreamImpl stream = new JavatterStreamImpl();
        stream.addOnFavorite((u1, u2, s) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
            assertEquals(s.getId(), 10);
        }).addOnUnFavorite((u1, u2, s) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
            assertEquals(s.getId(), 10);
        });

        stream.onFavorite(user1, user2, status);
        stream.onUnfavorite(user1, user2, status);
    }

    @Test
    public void testFollowAndUnFollow(@Mocked User user1, @Mocked User user2) throws Exception {
        new Expectations(user1) {{
            user1.getScreenName();
            result = "test";
        }};
        new Expectations(user2) {{
            user2.getScreenName();
            result = "test2";
        }};

        JavatterStreamImpl stream = new JavatterStreamImpl();
        stream.addOnFollow((u1, u2) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
        }).addOnUnFollow((u1, u2) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
        });

        stream.onFollow(user1, user2);
        stream.onUnfollow(user1, user2);
    }

    @Test
    public void testDirectMessage(@Mocked DirectMessage message) throws Exception {
        new Expectations() {{
            message.getText();
            result = "test";
        }};

        JavatterStreamImpl stream = new JavatterStreamImpl();
        stream.addOnDirectMessage(m -> assertEquals(m.getText(), "test"));

        stream.onDirectMessage(message);
    }

    @Test
    public void testUserList(@Mocked User user1, @Mocked User user2, @Mocked UserList userList) throws Exception {
        new Expectations(user1) {{
            user1.getScreenName();
            result = "test";
        }};
        new Expectations(user2) {{
            user1.getScreenName();
            result = "test2";
        }};
        new Expectations(userList) {{
            userList.getName();
            result = "AAA";
        }};

        JavatterStreamImpl stream = new JavatterStreamImpl();
        stream.addOnUserListCreation((u1, list) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(list.getName(), "AAA");
        }).addOnUserListDeletion((u1, list) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(list.getName(), "AAA");
        }).addOnUserListUpdate((u1, u2) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
        }).addOnUserListSubscripton((u1, u2, list) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
            assertEquals(list.getName(), "AAA");
        }).addOnUserListUnsubscripton((u1, u2, list) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
            assertEquals(list.getName(), "AAA");
        }).addOnUserMemberAdditon((u1, u2, list) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
            assertEquals(list.getName(), "AAA");
        }).addOnUserMemberDeletion((u1, u2, list) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
            assertEquals(list.getName(), "AAA");
        });

        stream.onUserListCreation(user1, userList);
        stream.onUserListDeletion(user1, userList);
        stream.onUserListUpdate(user1, userList);
        stream.onUserListSubscription(user1, user2, userList);
        stream.onUserListUnsubscription(user1, user2, userList);
        stream.onUserListMemberAddition(user1, user2, userList);
        stream.onUserListMemberDeletion(user1, user2, userList);
    }
}