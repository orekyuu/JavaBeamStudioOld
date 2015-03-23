package net.orekyuu.javatter.api.entity;

import java.time.LocalDateTime;

/**
 * 永続化されるユーザーの情報を持ったPOJO
 */
public class UserEntity {
    private LocalDateTime createdAt;
    private String description;
    private int favCount;
    private int followersCount;
    private int friendsCount;
    private int tweetCount;
    private long id;
    private int listedCount;
    private String location;
    private String webSite;
    private String name;
    private String screenName;
    private String profileImageURL;
}
