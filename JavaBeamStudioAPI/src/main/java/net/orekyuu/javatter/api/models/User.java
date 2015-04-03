package net.orekyuu.javatter.api.models;

import java.time.LocalDateTime;

public interface User {
    /**
     * @return ユーザーが作成された日付
     * @since 1.0.0
     */
    LocalDateTime getCreatedAt();

    /**
     * @return ユーザーの説明文
     * @since 1.0.0
     */
    String getDescription();

    /**
     * @return ユーザーのお気に入り数
     * @since 1.0.0
     */
    int getFavCount();

    /**
     * @return ユーザーのフォロワー数
     * @since 1.0.0
     */
    int getFollowersCount();

    /**
     * @return ユーザーのフォロー数
     * @since 1.0.0
     */
    int getFriendsCount();

    /**
     * @return ユーザーID
     * @since 1.0.0
     */
    long getId();

    /**
     * @return 追加されているリストの数
     * @since 1.0.0
     */
    int getListedCount();

    /**
     * @return ユーザーのプロフィールに設定されている場所
     * @since 1.0.0
     */
    String getLocation();

    /**
     * @return ユーザーの名前
     * @since 1.0.0
     */
    String getName();

    /**
     * @return ユーザーのスクリーンネーム
     * @since 1.0.0
     */
    String getScreenName();

    /**
     * @return ユーザーアイコンのURL
     * @since 1.0.0
     */
    String getProfileImageURL();

    /**
     * @return ユーザーのツイート数
     * @since 1.0.0
     */
    int getTweetCount();

    /**
     * @return ユーザーのWebサイト
     * @since 1.0.0
     */
    String getWebSite();
}
