package net.orekyuu.javatter.core;

import java.io.File;

import javafx.application.Platform;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import net.orekyuu.javatter.api.Tweet;
import net.orekyuu.javatter.api.twitter.ClientUser;

public class TweetUtil implements Tweet {

	@Override
	public void sendTweet(ClientUser user,String text){
		Platform.runLater(() -> {
			try {
				user.getTwitter().updateStatus(text);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		});
	}
	
	@Override
	public void sendReply(ClientUser user,long destinationId,String text,String destinationName){
		Platform.runLater(() -> {
			try {
				user.getTwitter().updateStatus(new StatusUpdate(text).inReplyToStatusId(destinationId));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void sendImageTweet(ClientUser user, String text, File file) {
		Platform.runLater(() -> {
			try {
				user.getTwitter().updateStatus(new StatusUpdate(text).media(file));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void sendImageReply(ClientUser user, long destinationId,
			String text, String destinationName, File file) {
		Platform.runLater(() -> {
			try {
				user.getTwitter().updateStatus(new StatusUpdate(text).inReplyToStatusId(destinationId).media(file));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		});
	}
}
