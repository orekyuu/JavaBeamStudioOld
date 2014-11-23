package net.orekyuu.javatter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import twitter4j.*;
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
    private long replyStatusID = -1;
    private TweetCallBack tweetCallBack = null;
    private boolean isAsync;
    private boolean isTweeted;

    @Override
    public void tweet() {
        if(isTweeted)
            throw new IllegalStateException("called tweet method.");

        isTweeted = true;
        if(isAsync)
            CompletableFuture.runAsync(this::runTweetAction);
        else
            runTweetAction();
    }

    private void runTweetAction() {
        StatusUpdate statusUpdate = new StatusUpdate(text);
        Twitter twitter = null;
        if (user == null)
            return;
        twitter = user.getTwitter();
        if (-1 != replyStatusID)
            statusUpdate.setInReplyToStatusId(replyStatusID);

        files.forEach(statusUpdate::media);
        try {
            Status status = twitter.updateStatus(statusUpdate);
            if (null != tweetCallBack)
                tweetCallBack.successCallBack(status);
        } catch (TwitterException e) {
            if (null != tweetCallBack)
                tweetCallBack.failureCallBack(e);
            e.printStackTrace();
        }
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
    public Tweet setReplyTo(long id) {
        this.replyStatusID = id;
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

    @Override
    public Tweet setAsync() {
        isAsync = true;
        return this;
    }
}
