package net.orekyuu.javatter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;
import net.orekyuu.javatter.api.Tweet;
import net.orekyuu.javatter.api.TweetCallBack;
import net.orekyuu.javatter.api.twitter.ClientUser;
/**
 * ツイートに関するクラス
 * @author seroriKETC
 *
 */
public class TweetBuilder implements Tweet {
    private List<File> files = new ArrayList<>();
    private ClientUser user;
    private String text;
    private long id = -1;
    private Twitter twitter;
    TweetCallBack tweetCallBack = null;

    @Override
    public void tweet() {
        CompletableFuture.runAsync(() -> {
            StatusUpdate statusUpdate = new StatusUpdate(text);
            if (null != user) {
                twitter = user.getTwitter();
            }
            if (-1 != id) {
                statusUpdate.setInReplyToStatusId(id);
            }
            if (files.size() != 0 && files.size() <= 4 && user != null) {
                int fileSize = files.size();
                List<UploadedMedia> media = new ArrayList<>();
                long[] mediaId = new long[files.size()];
                try {
                    for (int i = 0; i < fileSize; i++) {
                        media.add(twitter.uploadMedia(files.get(i)));
                        mediaId[i] = media.get(i).getMediaId();
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                if (mediaId != null) {
                    statusUpdate.setMediaIds(mediaId);
                }
            }
            if (null != user) {
                try {
                    twitter.updateStatus(statusUpdate);
                    if (null != tweetCallBack) {
                        tweetCallBack.SuccessCallBack(true);
                    }
                } catch (TwitterException e) {
                    if (null != tweetCallBack) {
                        tweetCallBack.SuccessCallBack(false);
                    }
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Tweet addMedia(File mediaFile) {
        files.add(mediaFile);
        return this;
    }

    @Override
    public Tweet setClientUser(ClientUser user) {
        this.user = user;
        return this;
    }

    @Override
    public Tweet setReplyto(long id) {
        this.id = id;
        return this;
    }

    @Override
    public Tweet setText(String s) {
        text = s;
        return this;
    }

    @Override
    public Tweet setTweetCallBack(TweetCallBack callBack) {
        tweetCallBack = callBack;
        return this;
    }
}
