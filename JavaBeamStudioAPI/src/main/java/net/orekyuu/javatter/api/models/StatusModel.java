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
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ツイートの情報を保持したクラス
 * @since 1.0.0
 */
public final class StatusModel {
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
     * @since 1.0.0
     */
    public long getStatusId() {
        return statusId;
    }

    /**
     * @return ツイートのテキスト
     * @since 1.0.0
     */
    public String getText() {
        return text;
    }

    /**
     * @return ツイートされた日時
     * @since 1.0.0
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @return リプライ先
     * @since 1.0.0
     */
    public long getReplyStatusId() {
        return replyStatusId;
    }

    /**
     * @return ツイートされたクライアント名
     * @since 1.0.0
     */
    public String getViaName() {
        return viaName;
    }

    /**
     * @return クライアントのリンク
     * @since 1.0.0
     */
    public String getViaLink() {
        return viaLink;
    }

    /**
     * @return リツイート元のツイート
     * @since 1.0.0
     */
    public StatusModel getRetweetFrom() {
        return retweetFrom;
    }

    /**
     * @return ツイートしたユーザー
     * @since 1.0.0
     */
    public UserModel getOwner() {
        return owner;
    }

    /**
     * @return このツイートをリツイートしたか
     * @since 1.0.0
     */
    public boolean isRetweeted() {
        return isRetweeted;
    }

    /**
     * @return このツイートをお気に入りしたか
     * @since 1.0.0
     */
    public boolean isFavorited() {
        return isFavorited;
    }

    /**
     * @return ツイートに含まれるメンション
     * @since 1.0.0
     */
    public List<UserMentionEntity> getMentions() {
        return Arrays.asList(mentions);
    }

    /**
     * @return ツイートに含まれるURL
     * @since 1.0.0
     */
    public List<URLEntity> getUrls() {
        return Arrays.asList(urls);
    }

    /**
     * @return ツイートに含まれるハッシュタグ
     * @since 1.0.0
     */
    public List<HashtagEntity> getHashtags() {
        return Arrays.asList(hashtags);
    }

    /**
     * @return ツイートに含まれるメディア
     * @since 1.0.0
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
     * {@link StatusModel}を作成するビルダーです。
     * @since 1.0.0
     */
    public final static class Builder {
        private static LoadingCache<Status, StatusModel> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
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
         * @return 引数のStatusを元に作られた{@link StatusModel}
         * @since 1.0.0
         * @exception com.google.common.util.concurrent.UncheckedExecutionException キャッシュに失敗した時
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
         * @return 取得した{@link StatusModel}
         * @since 1.0.0
         */
        public static StatusModel build(long statusId, ClientUser user) {
            Optional<StatusModel> first = cache.asMap().values().stream()
                    .filter(status -> status.getStatusId() == statusId).findFirst();
            if (first.isPresent()) {
                return first.get();
            }

            try {
                Status status = user.getTwitter().showStatus(statusId);
                return build(status);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
