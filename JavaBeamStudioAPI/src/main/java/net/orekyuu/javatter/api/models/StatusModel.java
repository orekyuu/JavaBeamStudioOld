package net.orekyuu.javatter.api.models;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import net.orekyuu.javatter.api.twitter.ClientUser;
import twitter4j.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
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
    private final UserMentionEntity[] mentions;
    private final URLEntity[] urls;
    private final HashtagEntity[] hashtags;
    private final MediaEntity[] medias;

    private static final Pattern viaNamePattern = Pattern.compile("^<a.*>(.*)</a>$");
    private static final Pattern viaLinkPattern = Pattern.compile("^<a.*href=\"(.*?)\".*>.*</a>$");

    private StatusModel(Status status) {
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
        Status retweetedStatus = status.getRetweetedStatus();
        retweetFrom = retweetedStatus != null ? StatusModel.Builder.build(retweetedStatus) : null;
        owner = UserModel.Builder.build(status.getUser());
        isRetweeted = status.isRetweeted();
        isFavorited = status.isFavorited();
    }

    /**
     * @return ツイートID
     */
    public long getStatusId() {
        return statusId;
    }

    /**
     * @return ツイートのテキスト
     */
    public String getText() {
        return text;
    }

    /**
     * @return ツイートされた日時
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @return リプライ先
     */
    public long getReplyStatusId() {
        return replyStatusId;
    }

    /**
     * @return ツイートされたクライアント名
     */
    public String getViaName() {
        return viaName;
    }

    /**
     * @return クライアントのリンク
     */
    public String getViaLink() {
        return viaLink;
    }

    /**
     * @return リツイート元のツイート
     */
    public StatusModel getRetweetFrom() {
        return retweetFrom;
    }

    /**
     * @return ツイートしたユーザー
     */
    public UserModel getOwner() {
        return owner;
    }

    /**
     * @return このツイートをリツイートしたか
     */
    public boolean isRetweeted() {
        return isRetweeted;
    }

    /**
     * @return このツイートをお気に入りしたか
     */
    public boolean isFavorited() {
        return isFavorited;
    }

    /**
     * @return ツイートに含まれるメンション
     */
    public List<UserMentionEntity> getMentions() {
        return Arrays.asList(mentions);
    }

    /**
     * @return ツイートに含まれるURL
     */
    public List<URLEntity> getUrls() {
        return Arrays.asList(urls);
    }

    /**
     * @return ツイートに含まれるハッシュタグ
     */
    public List<HashtagEntity> getHashtags() {
        return Arrays.asList(hashtags);
    }

    /**
     * @return ツイートに含まれるメディア
     */
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
         * @param status status 情報元
         * @return 引数のStatusを元に作られたStatusModel
         */
        public static StatusModel build(Status status) {
            try {
                return cache.get(status);
            } catch (ExecutionException e) {
                throw new UncheckedExecutionException(e);
            }
        }

        /**
         * StatusModelをIDから取得します。<br>
         * キャッシュになければTwitterから取得し、キャッシュします。
         * @param statusId id ツイートID
         * @param user user 取得するユーザー
         * @return 取得したStatusModel
         */
        public static StatusModel build(long statusId, ClientUser user) {
            try {
                ConcurrentMap<Status, StatusModel> map = cache.asMap();
                for (StatusModel model : map.values()) {
                    if (model.getStatusId() == statusId) {
                        return model;
                    }
                }

                return build(user.getTwitter().showStatus(statusId));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
