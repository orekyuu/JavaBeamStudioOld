package net.orekyuu.javatter.api.models;

import java.time.LocalDateTime;

/**
 * Created by orekyuu on 2015/05/05.
 */
public interface UserModel {
    LocalDateTime getCreatedAt();

    String getDescription();

    int getFavCount();

    int getFollowersCount();

    int getFriendsCount();

    long getId();

    int getListedCount();

    String getLocation();

    String getName();

    String getScreenName();

    String getProfileImageURL();

    int getTweetCount();

    String getWebSite();
}
