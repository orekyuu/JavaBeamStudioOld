package net.orekyuu.javatter.api.notification;

import org.junit.Test;

import static org.junit.Assert.*;

public class NotificationBuilderTest {

    @Test
    public void testCreate() {
        Notification notification = new NotificationBuilder(NotificationTypes.FOLLOW)
                .setMessage("message").setSubTitle("subTitle").build();
        assertEquals(notification.getTitle().get(), "フォローされました");
        assertEquals(notification.getMessage().get(), "message");
        assertEquals(notification.getSubTitle().get(), "subTitle");
    }

    @Test(expected = NullPointerException.class)
    public void testException() throws Exception {
        NotificationBuilder builder = new NotificationBuilder(null);
    }
}