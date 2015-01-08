package net.orekyuu.javatter.api.models;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import net.orekyuu.javatter.api.twitter.ClientUser;
import org.junit.Before;
import org.junit.Test;
import twitter4j.User;
import java.util.Date;

import static org.junit.Assert.*;

public class UserModelTest {

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
    }

    @Test
    public void testCreate() {
        UserModel model = UserModel.Builder.build(user);
        assertNotNull(model);
        assertEquals(model.getId(), user.getId());
        assertEquals(model.getScreenName(), user.getScreenName());
        assertEquals(model.getDescription(), user.getDescription());
    }

    @Test
    public void testCache(@Mocked ClientUser clientUser) {
        UserModel model = UserModel.Builder.build(user);
        UserModel cache = UserModel.Builder.build(1, clientUser);
        assertEquals(model.getId(), cache.getId());
    }
}