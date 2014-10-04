package net.orekyuu.javatter.core.twitter.stream;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Before;
import org.junit.Test;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserList;

import static org.junit.Assert.assertEquals;

public class JavatterStreamImplTest {

    private User user1;
    private User user2;
    private Status status1;
    private Status status2;
    private DirectMessage directMessage;
    private UserList userList;
    private int callCount;

    @Before
    public void before() {
        callCount = 0;
        user1 = new MockUp<User>() {
            @Mock
            public long getId() {
                return 10;
            }

            @Mock
            public String getScreenName() {
                return "test";
            }
        }.getMockInstance();

        user2 = new MockUp<User>() {
            @Mock
            public long getId() {
                return 11;
            }

            @Mock
            public String getScreenName() {
                return "test2";
            }
        }.getMockInstance();

        status1 = new MockUp<Status>() {
            @Mock
            public long getId() {
                return 10;
            }

            @Mock
            public String getText() {
                return "test";
            }

        }.getMockInstance();

        status2 = new MockUp<Status>() {
            @Mock
            public long getId() {
                return 20;
            }

            @Mock
            public String getText() {
                return "test2";
            }

        }.getMockInstance();

        directMessage = new MockUp<DirectMessage>() {
            @Mock
            public String getText() {
                return "test";
            }
        }.getMockInstance();

        userList = new MockUp<UserList>() {
            @Mock
            public String getName() {
                return "AAA";
            }
        }.getMockInstance();
    }

    @Test
    public void testOnStatus() throws Exception {
        JavatterStreamImpl streamA = new JavatterStreamImpl();
        streamA.addOnStatus(s -> {
            assertEquals(s.getId(), 10);
            assertEquals(s.getText(), "test");
            callCount++;
        }).addOnStatus(s -> callCount++);
        streamA.onStatus(status1);

        JavatterStreamImpl streamB = new JavatterStreamImpl();
        streamB.addOnStatus(s -> {
            assertEquals(s.getId(), 10);
            assertEquals(s.getText(), "test");
            callCount++;
        });
        streamB.onStatus(status2);

        assertEquals(callCount, 3);
    }

    @Test
    public void testOnException() throws Exception {
        Exception e1 = new NullPointerException();
        Exception e2 = new IllegalAccessException();

        JavatterStreamImpl streamA = new JavatterStreamImpl();
        streamA.addOnException(e -> {
            assertEquals(e.getClass().getName(), NullPointerException.class.getName());
            callCount++;
        }).addOnException(e -> callCount++);
        streamA.onException(e1);

        JavatterStreamImpl streamB = new JavatterStreamImpl();
        streamB.addOnException(e -> {
            assertEquals(e.getClass().getName(), IllegalAccessException.class.getName());
            callCount++;
        });
        streamB.onException(e2);
        assertEquals(callCount, 3);
    }

    @Test
    public void testOnScrubGeo() throws Exception {
        JavatterStreamImpl streamA = new JavatterStreamImpl();
        streamA.addOnScrubGeo((userID, statusID) -> {
            assertEquals(userID, Long.valueOf(1));
            assertEquals(statusID, Long.valueOf(2));
            callCount++;
        }).addOnScrubGeo((a, b) -> callCount++);
        streamA.onScrubGeo(1, 2);

        JavatterStreamImpl streamB = new JavatterStreamImpl();
        streamB.addOnScrubGeo((userID, statusID) -> {
            assertEquals(userID, Long.valueOf(3));
            assertEquals(statusID, Long.valueOf(4));
            callCount++;
        });
        streamB.onScrubGeo(3, 4);
        assertEquals(callCount, 3);
    }

    @Test
    public void testOnProfileUpdate() throws Exception {
        JavatterStreamImpl streamA = new JavatterStreamImpl();
        streamA.addOnProfileUpdate(s -> {
            assertEquals(s.getId(), 10);
            assertEquals(s.getScreenName(), "test");
            callCount++;
        }).addOnProfileUpdate(a -> callCount++);
        streamA.onUserProfileUpdate(user1);

        JavatterStreamImpl streamB = new JavatterStreamImpl();
        streamB.addOnProfileUpdate(s -> {
            assertEquals(s.getId(), 20);
            assertEquals(s.getScreenName(), "test2");
            callCount++;
        });
        streamB.onUserProfileUpdate(user2);

        assertEquals(callCount, 3);
    }

    @Test
    public void testOnDeletionNotice() throws Exception {
        JavatterStreamImpl streamA = new JavatterStreamImpl();
        streamA.addOnDeletionNotice((userID, statusID) -> {
            assertEquals(userID, Long.valueOf(1));
            assertEquals(statusID, Long.valueOf(2));
            callCount++;
        }).addOnDeletionNotice((a, b) -> callCount++);
        streamA.onDeletionNotice(1, 2);

        JavatterStreamImpl streamB = new JavatterStreamImpl();
        streamB.addOnDeletionNotice((userID, statusID) -> {
            assertEquals(userID, Long.valueOf(3));
            assertEquals(statusID, Long.valueOf(4));
            callCount++;
        });
        streamB.onDeletionNotice(3, 4);
        assertEquals(callCount, 3);
    }

    @Test
    public void testBlockAndUnBlock() throws Exception {
        JavatterStreamImpl stream = new JavatterStreamImpl();
        stream.addOnBlockUser((source, target) -> {
            assertEquals(source.getScreenName(), "test");
            assertEquals(target.getScreenName(), "test2");
            callCount++;
        }).addOnUnBlockUser((source, target) -> {
            assertEquals(source.getScreenName(), "test");
            assertEquals(target.getScreenName(), "test2");
            callCount++;
        }).addOnBlockUser((a, b) -> callCount++).addOnUnBlockUser((a, b) -> callCount++);

        stream.onBlock(user1, user2);
        stream.onUnblock(user1, user2);
        assertEquals(callCount, 4);
    }

    @Test
    public void testFavoriteAndUnFavorite() throws Exception {
        JavatterStreamImpl stream = new JavatterStreamImpl();
        stream.addOnFavorite((u1, u2, s) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
            assertEquals(s.getId(), 10);
            callCount++;
        }).addOnUnFavorite((u1, u2, s) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
            assertEquals(s.getId(), 10);
            callCount++;
        }).addOnFavorite((a, b, c) -> callCount++).addOnUnFavorite((a, b, c) -> callCount++);

        stream.onFavorite(user1, user2, status1);
        stream.onUnfavorite(user1, user2, status1);
        assertEquals(callCount, 4);
    }

    @Test
    public void testFollowAndUnFollow() throws Exception {
        JavatterStreamImpl stream = new JavatterStreamImpl();
        stream.addOnFollow((u1, u2) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
            callCount++;
        }).addOnUnFollow((u1, u2) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(u2.getScreenName(), "test2");
            callCount++;
        }).addOnFollow((a, b) -> callCount++).addOnUnFollow((a, b) -> callCount++);

        stream.onFollow(user1, user2);
        stream.onUnfollow(user1, user2);
        assertEquals(callCount, 4);
    }

    @Test
    public void testDirectMessage() throws Exception {
        JavatterStreamImpl stream = new JavatterStreamImpl();
        stream.addOnDirectMessage(m -> {
            assertEquals(m.getText(), "test");
            callCount++;
        }).addOnDirectMessage(a -> callCount++);

        stream.onDirectMessage(directMessage);
        assertEquals(callCount, 2);
    }

    @Test
    public void testUserList() throws Exception {
        JavatterStreamImpl stream = new JavatterStreamImpl();
        stream.addOnUserListCreation((u1, list) -> {
            assertEquals(u1.getScreenName(), "test");
            assertEquals(list.getName(), "AAA");
            callCount++;
        })
                .addOnUserListDeletion((u1, list) -> {
                    assertEquals(u1.getScreenName(), "test");
                    assertEquals(list.getName(), "AAA");
                    callCount++;
                })
                .addOnUserListUpdate((u1, u2) -> {
                    assertEquals(u1.getScreenName(), "test");
                    assertEquals(u2.getScreenName(), "test2");
                    callCount++;
                })
                .addOnUserListSubscripton((u1, u2, list) -> {
                    assertEquals(u1.getScreenName(), "test");
                    assertEquals(u2.getScreenName(), "test2");
                    assertEquals(list.getName(), "AAA");
                    callCount++;
                })
                .addOnUserListUnsubscripton((u1, u2, list) -> {
                    assertEquals(u1.getScreenName(), "test");
                    assertEquals(u2.getScreenName(), "test2");
                    assertEquals(list.getName(), "AAA");
                    callCount++;
                })
                .addOnUserMemberAdditon((u1, u2, list) -> {
                    assertEquals(u1.getScreenName(), "test");
                    assertEquals(u2.getScreenName(), "test2");
                    assertEquals(list.getName(), "AAA");
                    callCount++;
                })
                .addOnUserMemberDeletion((u1, u2, list) -> {
                    assertEquals(u1.getScreenName(), "test");
                    assertEquals(u2.getScreenName(), "test2");
                    assertEquals(list.getName(), "AAA");
                    callCount++;
                })
                .addOnUserListCreation((u1, list) -> callCount++)
                .addOnUserListDeletion((u1, list) -> callCount++)
                .addOnUserListUpdate((u1, u2) -> callCount++)
                .addOnUserListSubscripton((u1, u2, list) -> callCount++)
                .addOnUserListUnsubscripton((u1, u2, list) -> callCount++)
                .addOnUserMemberAdditon((u1, u2, list) -> callCount++)
                .addOnUserMemberDeletion((u1, u2, list) -> callCount++);

        stream.onUserListCreation(user1, userList);
        stream.onUserListDeletion(user1, userList);
        stream.onUserListUpdate(user1, userList);
        stream.onUserListSubscription(user1, user2, userList);
        stream.onUserListUnsubscription(user1, user2, userList);
        stream.onUserListMemberAddition(user1, user2, userList);
        stream.onUserListMemberDeletion(user1, user2, userList);
        assertEquals(callCount, 14);
    }
}