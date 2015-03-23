package net.orekyuu.javatter.api.entity;

import net.orekyuu.javatter.api.models.UserModel;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import java.time.LocalDateTime;

/**
 * 永続化されるツイートの情報を持ったPOJO
 */
public class StatusEntity {

    private long statusId;
    private String text;
    private LocalDateTime createdAt;
    private long replyStatusId;
    private String viaName;
    private String viaLink;
    private StatusEntity retweetFrom;
    private UserEntity owner;
    private boolean isRetweeted;
    private boolean isFavorited;
    private UserMentionEntity[] mentions;
    private URLEntity[] urls;
    private HashtagEntity[] hashtags;
    private MediaEntity[] medias;
}
