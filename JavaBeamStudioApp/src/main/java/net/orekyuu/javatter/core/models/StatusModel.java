package net.orekyuu.javatter.core.models;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import javafx.scene.image.Image;
import net.orekyuu.javatter.core.cache.IconCache;
import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ツイートの情報を保持したクラス
 */
public class StatusModel {
    private final long statusId;
    private final String text;
    private final LocalDateTime createdAt;
    private final long replyStatusId;
    private final String viaName;
    private final String viaLink;
    private final StatusModel retweetFrom;
    private final UserModel owner;
    private final boolean isRetweeted;
    private final boolean isFavorited;

    private static final Pattern viaNamePattern = Pattern.compile("^<a.*>(.*)</a>$");
    private static final Pattern viaLinkPattern = Pattern.compile("^<a.*href=\"(.*?)\".*>.*</a>$");

    private StatusModel(Status status) {
        statusId = status.getId();
        text = status.getText();
        createdAt = LocalDateTime.ofInstant(status.getCreatedAt().toInstant(), ZoneId.systemDefault());
        replyStatusId = status.getInReplyToStatusId();
        String via = status.getSource();
        Matcher matcher = viaNamePattern.matcher(via);
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
        Status retweetedStatus = status.getRetweetedStatus();
        retweetFrom = retweetedStatus != null ? StatusModel.Builder.build(retweetedStatus) : null;
        owner = UserModel.Builder.build(status.getUser());
        isRetweeted = status.isRetweeted();
        isFavorited = status.isFavorited();
    }

    public long getStatusId() {
        return statusId;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getReplyStatusId() {
        return replyStatusId;
    }

    public String getViaName() {
        return viaName;
    }

    public String getViaLink() {
        return viaLink;
    }

    public StatusModel getRetweetFrom() {
        return retweetFrom;
    }

    public UserModel getOwner() {
        return owner;
    }

    public static Pattern getViaNamePattern() {
        return viaNamePattern;
    }

    public static Pattern getViaLinkPattern() {
        return viaLinkPattern;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public boolean isFavorited() {
        return isFavorited;
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
        sb.append('}');
        return sb.toString();
    }

    /**
     * StatusModelを作成するビルダーです。
     */
    public static class Builder {
        private static LoadingCache<Status, StatusModel> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .softValues()
                .maximumSize(100)
                .build(new CacheLoader<Status, StatusModel>() {
                    @Override
                    public StatusModel load(Status key) throws Exception {
                        return new StatusModel(key);
                    }
                });

        /**
         * StatusModelを作成します。
         * @param status status
         * @return StatusModel
         */
        public static StatusModel build(Status status) {
            try {
                return cache.get(status);
            } catch (ExecutionException e) {
                throw new UncheckedExecutionException(e);
            }
        }
    }
}
