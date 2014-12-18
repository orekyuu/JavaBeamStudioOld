package net.orekyuu.javatter.core.twitter;

import net.orekyuu.javatter.api.twitter.ClientUser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import twitter4j.auth.AccessToken;

public class LocalClientUserTest{
	@Test
	public void saveAndLoadTest() {
		AccessToken token = new AccessToken("token", "tokenSecret");
		LocalClientUser localClientUser = new LocalClientUser(token);
		localClientUser.save();
		ClientUser clientUser = LocalClientUser.loadClientUsers().get(0);
		assertEquals(token.getToken(),clientUser.getAccessToken().getToken());
		assertEquals(token.getTokenSecret(),clientUser.getAccessToken().getTokenSecret());
	}
}
