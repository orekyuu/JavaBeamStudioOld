package net.orekyuu.javatter.api.models;

import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface Status {
    /**
     * @return ツイートのID
     * @since 1.0.0
     */
    long getStatusId();

    /**
     * @return ツイートの発言内容
     * @since 1.0.0
     */
    String getText();

    /**
     * @return ツイートされた時間
     * @since 1.0.0
     */
    LocalDateTime getCreatedAt();

    /**
     * @return リプライ先のツイートID
     * @since 1.0.0
     */
    long getReplyStatusId();

    /**
     * @return ツイートされたクライアント名
     * @since 1.0.0
     */
    String getViaName();

    /**
     * @return クライアントのリンク
     * @since 1.0.0
     */
    String getViaLink();

    /**
     * @return リツイート元のツイート
     * @since 1.0.0
     */
    Status getRetweetFrom();

    /**
     * @return ツイートしたユーザー
     * @since 1.0.0
     */
    User getOwner();

    /**
     * @return このツイートをリツイートしたか
     * @since 1.0.0
     */
    boolean isRetweeted();

    /**
     * @return このツイートをお気に入りしたか
     * @since 1.0.0
     */
    boolean isFavorited();

    /**
     * @return ツイートに含まれるメンション
     * @since 1.0.0
     */
    List<UserMentionEntity> getMentions();

    /**
     * @return ツイートに含まれるURL
     * @since 1.0.0
     */
    List<URLEntity> getUrls();

    /**
     * @return ツイートに含まれるハッシュタグ
     * @since 1.0.0
     */
    List<HashtagEntity> getHashtags();

    /**
     * @return ツイートに含まれるメディア
     * @since 1.0.0
     */
    List<MediaEntity> getMedias();
}
