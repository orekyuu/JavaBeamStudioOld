package net.orekyuu.javatter.api.models;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import net.orekyuu.javatter.api.twitter.ClientUser;
import twitter4j.TwitterException;
import twitter4j.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * ユーザーの情報を格納したクラス
 */
public class UserModel {
    private final LocalDateTime createdAt;
    private final String description;
    private final int favCount;
    private final int followersCount;
    private final int friendsCount;
    private final int tweetCount;
    private final long id;
    private final int listedCount;
    private final String location;
    private final String webSite;
    private final String name;
    private final String screenName;
    private final String profileImageURL;

    private UserModel(User user) {
        createdAt = LocalDateTime.ofInstant(user.getCreatedAt().toInstant(), ZoneId.systemDefault());
        description = user.getDescription();
        favCount = user.getFavouritesCount();
        followersCount = user.getFollowersCount();
        friendsCount = user.getFriendsCount();
        tweetCount = user.getStatusesCount();
        id = user.getId();
        listedCount = user.getListedCount();
        location = user.getLocation();
        webSite = user.getURL();
        name = user.getName();
        screenName = user.getScreenName();
        profileImageURL = user.getProfileImageURL();
    }

    /**
     * @return ユーザーが作成された日付
     * @since 1.0.0
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @return ユーザーの説明文
     * @since 1.0.0
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return ユーザーのお気に入り数
     * @since 1.0.0
     */
    public int getFavCount() {
        return favCount;
    }

    /**
     * @return ユーザーのフォロワー数
     * @since 1.0.0
     */
    public int getFollowersCount() {
        return followersCount;
    }

    /**
     * @return ユーザーのフォロー数
     * @since 1.0.0
     */
    public int getFriendsCount() {
        return friendsCount;
    }

    /**
     * @return ユーザーID
     * @since 1.0.0
     */
    public long getId() {
        return id;
    }

    /**
     * @return 追加されているリストの数
     * @since 1.0.0
     */
    public int getListedCount() {
        return listedCount;
    }

    /**
     * @return ユーザーのプロフィールに設定されている場所
     * @since 1.0.0
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return ユーザーの名前
     * @since 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * @return ユーザーのスクリーンネーム
     * @since 1.0.0
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * @return ユーザーアイコンのURL
     * @since 1.0.0
     */
    public String getProfileImageURL() {
        return profileImageURL;
    }

    /**
     * @return ユーザーのツイート数
     * @since 1.0.0
     */
    public int getTweetCount() {
        return tweetCount;
    }

    /**
     * @return ユーザーのWebサイト
     * @since 1.0.0
     */
    public String getWebSite() {
        return webSite;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserModel{");
        sb.append("createdAt=").append(createdAt);
        sb.append(", description='").append(description).append('\'');
        sb.append(", favCount=").append(favCount);
        sb.append(", followersCount=").append(followersCount);
        sb.append(", friendsCount=").append(friendsCount);
        sb.append(", tweetCount=").append(tweetCount);
        sb.append(", id=").append(id);
        sb.append(", listedCount=").append(listedCount);
        sb.append(", location='").append(location).append('\'');
        sb.append(", webSite='").append(webSite).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", screenName='").append(screenName).append('\'');
        sb.append(", profileImageURL='").append(profileImageURL).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * {@link net.orekyuu.javatter.api.models.UserModel}を作成するビルダーです
     * @since 1.0.0
     */
    public static class Builder {
        private static LoadingCache<User, UserModel> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .softValues()
                .maximumSize(100)
                .build(new CacheLoader<User, UserModel>() {
                    @Override
                    public UserModel load(User key) throws Exception {
                        return new UserModel(key);
                    }
                });

        /**
         * {@link net.orekyuu.javatter.api.models.UserModel}を作成します
         * @param user 情報元
         * @return 引数のユーザーを元に作られた{@link UserModel}
         * @since 1.0.0
         * @exception com.google.common.util.concurrent.UncheckedExecutionException キャッシュに失敗した時
         */
        public static UserModel build(User user) {
            try {
                return cache.get(user);
            } catch (ExecutionException e) {
                throw new UncheckedExecutionException(e);
            }
        }

        /**
         * [@link ClientUser}のAPIを使用してidに対応するユーザーの{@link UserModel}を作成します。<br>
         * TwitterAPIを使用するため、呼び出し回数と処理時間に気をつけてください。
         * @param id ユーザーID
         * @param clientUser 使用するユーザー
         * @return 引数のユーザーを元に作られた{@link UserModel}
         * @since 1.0.0
         * @exception com.google.common.util.concurrent.UncheckedExecutionException キャッシュに失敗した時
         */
        public static UserModel build(long id, ClientUser clientUser) {
            Optional<UserModel> first = cache.asMap().values().stream()
                    .filter(user -> user.getId() == id).findFirst();
            if (first.isPresent()) {
                return first.get();
            }

            try {
                User user = clientUser.getTwitter().showUser(id);
                return build(user);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
