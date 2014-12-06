package net.orekyuu.javatter.api.models;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import twitter4j.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final long id;
    private final int listedCount;
    private final String location;
    private final String name;
    private final String screenName;
    private final String profileImageURL;

    private UserModel(User user) {
        createdAt = LocalDateTime.ofInstant(user.getCreatedAt().toInstant(), ZoneId.systemDefault());
        description = user.getDescription();
        favCount = user.getFavouritesCount();
        followersCount = user.getFollowersCount();
        friendsCount = user.getFriendsCount();
        id = user.getId();
        listedCount = user.getListedCount();
        location = user.getLocation();
        name = user.getName();
        screenName = user.getScreenName();
        profileImageURL = user.getProfileImageURL();
    }

    /**
     * @return ユーザーが作成された日付
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @return ユーザーの説明文
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return ユーザーのお気に入り数
     */
    public int getFavCount() {
        return favCount;
    }

    /**
     * @return ユーザーのフォロワー数
     */
    public int getFollowersCount() {
        return followersCount;
    }

    /**
     * @return ユーザーのフォロー数
     */
    public int getFriendsCount() {
        return friendsCount;
    }

    /**
     * @return ユーザーID
     */
    public long getId() {
        return id;
    }

    /**
     * @return 追加されているリストの数
     */
    public int getListedCount() {
        return listedCount;
    }

    /**
     * @return ユーザーのプロフィールに設定されている場所
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return ユーザーの名前
     */
    public String getName() {
        return name;
    }

    /**
     * @return ユーザーのスクリーンネーム
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * @return ユーザーアイコンのURL
     */
    public String getProfileImageURL() {
        return profileImageURL;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserModel{");
        sb.append("createdAt=").append(createdAt);
        sb.append(", description='").append(description).append('\'');
        sb.append(", favCount=").append(favCount);
        sb.append(", followersCount=").append(followersCount);
        sb.append(", friendsCount=").append(friendsCount);
        sb.append(", id=").append(id);
        sb.append(", listedCount=").append(listedCount);
        sb.append(", location='").append(location).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", screenName='").append(screenName).append('\'');
        sb.append(", profileImageURL='").append(profileImageURL).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * UserModelを作成するビルダーです
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
         * UserModelを作成します
         * @param user 情報元
         * @return 引数のユーザーを元に作られたUserModel
         */
        public static UserModel build(User user) {
            try {
                return cache.get(user);
            } catch (ExecutionException e) {
                throw new UncheckedExecutionException(e);
            }
        }
    }
}
