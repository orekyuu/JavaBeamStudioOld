package net.orekyuu.javatter.api.models;

import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by orekyuu on 2015/05/05.
 */
public interface StatusModel {
    long getStatusId();

    String getText();

    LocalDateTime getCreatedAt();

    long getReplyStatusId();

    String getViaName();

    String getViaLink();

    StatusModel getRetweetFrom();

    UserModel getOwner();

    boolean isRetweeted();

    boolean isFavorited();

    List<UserMentionEntity> getMentions();

    List<URLEntity> getUrls();

    List<HashtagEntity> getHashtags();

    List<MediaEntity> getMedias();
}
