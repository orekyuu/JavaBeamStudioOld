package net.orekyuu.javatter.api.twitter;

import twitter4j.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * ツイートを行うためのクラスです。<br>
 * ツイートを行った後は再利用できません。
 */
public final class TweetBuilder {
    private List<File> files = new ArrayList<>();
    private ClientUser user;
    private String text;
    private long replyStatusID = -1;
    private Optional<TweetFailed> failed = Optional.empty();
    private Optional<TweetSuccess> success = Optional.empty();
    private boolean isAsync;
    private boolean isTweeted;

    TweetBuilder(ClientUser user) {
        if (user == null) {
            throw new NullPointerException();
        }
        this.user = user;
    }

    /**
     * ツイートを行います。<br>
     * tweetを行った後は再利用できません。<br>
     * @since 1.0.0
     * @exception IllegalStateException このメソッドが複数回呼び出された時
     */
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
        Twitter twitter = user.getTwitter();
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

    /**
     * ツイートにイメージを添付します。<br>
     *
     * @param mediaFile 添付するファイル
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    public TweetBuilder addMedia(File mediaFile) {
        files.add(mediaFile);
        return this;
    }

    /**
     * リプライ先を設定します
     *
     * @param id リプライ先のStatusID
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    public TweetBuilder setReplyTo(long id) {
        this.replyStatusID = id;
        return this;
    }

    /**
     * ツイートの内容を設定します
     *
     * @param text 発言内容
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    public TweetBuilder setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * ツイート失敗時のイベントを設定します
     *
     * @param callback ツイート失敗時の処理
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    public TweetBuilder setOnTweetFailed(TweetFailed callback) {
        failed = Optional.of(callback);
        return this;
    }

    /**
     * ツイート成功時のイベントを設定する
     *
     * @param callback ツイート成功時の処理
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    public TweetBuilder setOnTweetSuccess(TweetSuccess callback) {
        success = Optional.of(callback);
        return this;
    }

    /**
     * ツイートを非同期で行います
     *
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    public TweetBuilder setAsync() {
        isAsync = true;
        return this;
    }
}
