package net.orekyuu.javatter.core.models;

import net.orekyuu.javatter.core.entity.StatusEntity;
import net.orekyuu.javatter.api.models.User;
import net.orekyuu.javatter.core.service.StatusServiceImpl;
import net.orekyuu.javatter.core.service.UserServiceImpl;
import twitter4j.*;
import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ツイートの情報を保持したクラス
 *
 * @since 1.0.0
 */
public final class StatusModel implements net.orekyuu.javatter.api.models.Status {
    private final long statusId;
    private final String text;
    private final LocalDateTime createdAt;
    private final long replyStatusId;
    private final String viaName;
    private final String viaLink;
    private final net.orekyuu.javatter.api.models.Status retweetFrom;
    private final User owner;
    private final boolean isRetweeted;
    private final boolean isFavorited;
    private final UserMentionEntity[] mentions;
    private final URLEntity[] urls;
    private final HashtagEntity[] hashtags;
    private final MediaEntity[] medias;

    private static final Pattern viaNamePattern = Pattern.compile("^<a.*>(.*)</a>$");
    private static final Pattern viaLinkPattern = Pattern.compile("^<a.*href=\"(.*?)\".*>.*</a>$");

    public StatusModel(Status status) {
        statusId = status.getId();
        text = status.getText();
        createdAt = LocalDateTime.ofInstant(status.getCreatedAt().toInstant(), ZoneId.systemDefault());
        replyStatusId = status.getInReplyToStatusId();
        String via = status.getSource();
        Matcher matcher = viaNamePattern.matcher(via);
        mentions = status.getUserMentionEntities();
        urls = status.getURLEntities();
        hashtags = status.getHashtagEntities();
        medias = status.getExtendedMediaEntities();
        if (matcher.find()) {
            viaName = matcher.group(1);
        } else {
            viaName = "error";
        }
        Matcher matcher2 = viaLinkPattern.matcher(via);
        if (matcher2.find()) {
            viaLink = matcher2.group(1);
        } else {
            viaLink = "error";
        }
        if (status.getRetweetedStatus() != null) {
            retweetFrom = new StatusModel(status.getRetweetedStatus());
        } else {
            retweetFrom = null;
        }
        owner = new UserModel(status.getUser());
        isRetweeted = status.isRetweeted();
        isFavorited = status.isFavorited();
    }

    public StatusModel(StatusEntity entity) {
        statusId = entity.getStatusId();
        text = entity.getText();
        createdAt = entity.getCreatedAt().toLocalDateTime();
        replyStatusId = entity.getReplyStatusId();
        mentions = entity.getMentions();
        urls = entity.getUrls();
        hashtags = entity.getHashtags();
        medias = entity.getMedias();
        viaName = entity.getViaName();
        viaLink = entity.getViaLink();
        StatusServiceImpl statusService = new StatusServiceImpl();
        retweetFrom = statusService.findByID(entity.getRetweetFrom()).orElse(null);
        //TODO
        UserServiceImpl userService = new UserServiceImpl();
        owner = userService.findByID(entity.getStatusId(), null).orElse(null);
        isRetweeted = entity.isRetweeted();
        isFavorited = entity.isFavorited();
    }

    @Override
    public long getStatusId() {
        return statusId;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public long getReplyStatusId() {
        return replyStatusId;
    }

    @Override
    public String getViaName() {
        return viaName;
    }

    @Override
    public String getViaLink() {
        return viaLink;
    }

    @Override
    public net.orekyuu.javatter.api.models.Status getRetweetFrom() {
        return retweetFrom;
    }

    @Override
    public net.orekyuu.javatter.api.models.User getOwner() {
        return owner;
    }

    @Override
    public boolean isRetweeted() {
        return isRetweeted;
    }

    @Override
    public boolean isFavorited() {
        return isFavorited;
    }

    @Override
    public List<UserMentionEntity> getMentions() {
        return Arrays.asList(mentions);
    }

    @Override
    public List<URLEntity> getUrls() {
        return Arrays.asList(urls);
    }

    @Override
    public List<HashtagEntity> getHashtags() {
        return Arrays.asList(hashtags);
    }

    @Override
    public List<MediaEntity> getMedias() {
        return Arrays.asList(medias);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusModel{");
        sb.append("statusId=").append(statusId);
        sb.append(", text='").append(text).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", replyStatusId=").append(replyStatusId);
        sb.append(", viaName='").append(viaName).append('\'');
        sb.append(", viaLink='").append(viaLink).append('\'');
        sb.append(", retweetFrom=").append(retweetFrom);
        sb.append(", owner=").append(owner);
        sb.append(", isRetweeted=").append(isRetweeted);
        sb.append(", isFavorited=").append(isFavorited);
        sb.append(", mentions=").append(Arrays.toString(mentions));
        sb.append(", urls=").append(Arrays.toString(urls));
        sb.append(", hashtags=").append(Arrays.toString(hashtags));
        sb.append(", medias=").append(Arrays.toString(medias));
        sb.append('}');
        return sb.toString();
    }
}
