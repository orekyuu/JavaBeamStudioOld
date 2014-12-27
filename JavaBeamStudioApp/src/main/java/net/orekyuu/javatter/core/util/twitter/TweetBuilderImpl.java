package net.orekyuu.javatter.core.util.twitter;

import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.twitter.TweetBuilder;
import net.orekyuu.javatter.api.twitter.TweetFailed;
import net.orekyuu.javatter.api.twitter.TweetSuccess;
import twitter4j.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * ツイートに関するクラス
 */
public class TweetBuilderImpl implements TweetBuilder {
    private List<File> files = new ArrayList<>();
    private ClientUser user;
    private String text;
    private long replyStatusID = -1;
    private Optional<TweetFailed> failed = Optional.empty();
    private Optional<TweetSuccess> success = Optional.empty();
    private boolean isAsync;
    private boolean isTweeted;

    @Override
    public void tweet() {
        if (isTweeted)
            throw new IllegalStateException("called tweet method.");

        isTweeted = true;
        if (isAsync)
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

        long[] medias = new long[files.size()];
        for (int i = 0; i < files.size(); i++) {
            try {
                UploadedMedia uploadedMedia = user.getTwitter().uploadMedia(files.get(i));
                medias[i] = uploadedMedia.getMediaId();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        statusUpdate.setMediaIds(medias);

        try {
            Status status = twitter.updateStatus(statusUpdate);
            success.ifPresent(s -> s.success(status));
        } catch (TwitterException e) {
            failed.ifPresent(s -> s.failed(e));
            e.printStackTrace();
        }
    }

    @Override
    public TweetBuilder addMedia(File mediaFile) {
        System.out.println("add media: " + mediaFile.getName());
        files.add(mediaFile);
        return this;
    }

    @Override
    public TweetBuilder setClientUser(ClientUser user) {
        this.user = user;
        return this;
    }

    @Override
    public TweetBuilder setReplyTo(long id) {
        this.replyStatusID = id;
        return this;
    }

    @Override
    public TweetBuilder setText(String s) {
        text = s;
        return this;
    }

    @Override
    public TweetBuilder setOnTweetFailed(TweetFailed callback) {
        failed = Optional.of(callback);
        return this;
    }

    @Override
    public TweetBuilder setOnTweetSuccess(TweetSuccess callback) {
        success = Optional.of(callback);
        return this;
    }

    @Override
    public TweetBuilder setAsync() {
        isAsync = true;
        return this;
    }
}
