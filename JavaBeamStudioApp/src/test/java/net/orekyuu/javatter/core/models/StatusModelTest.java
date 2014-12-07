package net.orekyuu.javatter.core.models;

import mockit.Mock;
import mockit.MockUp;
import net.orekyuu.javatter.api.models.StatusModel;
import org.junit.Before;
import org.junit.Test;
import twitter4j.Status;
import twitter4j.User;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class StatusModelTest {

    private Status status1;
    private User user;

    @Before
    public void before() {
        user = new MockUp<User>() {
            @Mock
            public long getId() {
                return 1;
            }

            @Mock
            public String getScreenName() {
                return "test user";
            }

            @Mock
            public Date getCreatedAt() {
                return new Date(2014, 0, 1);
            }

            @Mock
            public String getDescription() {
                return "description";
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

            @Mock
            public Date getCreatedAt() {
                return new Date(2014, 0, 1);
            }

            @Mock
            public long getInReplyToStatusId() {
                return 1;
            }

            @Mock
            public String getSource() {
                return "<a href=\"http://twitter.com\" rel=\"nofollow\">Twitter Web Client</a>";
            }

            @Mock
            public User getUser() {
                return user;
            }

        }.getMockInstance();


    }

    @Test
    public void testCreate() {
        StatusModel model1 = StatusModel.Builder.build(status1);
        assertEquals("Twitter Web Client", model1.getViaName());
        assertEquals("http://twitter.com", model1.getViaLink());
        assertEquals(10, model1.getStatusId());
        assertEquals("test", model1.getText());
    }
}