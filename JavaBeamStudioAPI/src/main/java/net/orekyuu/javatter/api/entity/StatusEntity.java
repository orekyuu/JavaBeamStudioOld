package net.orekyuu.javatter.api.entity;

import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * 永続化されるツイートの情報を持ったPOJO
 */
@Entity
@Table(name = "TWITTER_STATUS")
public class StatusEntity {

    @Id
    @OrderBy
    private long statusId;

    private Account account;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private Timestamp createdAt;
    @Column(nullable = false)
    private long replyStatusId;
    @Column(nullable = false)
    private String viaName;
    @Column(nullable = false)
    private String viaLink;
    @Column(nullable = false)
    private long retweetFrom;
    @Column(nullable = false)
    private long owner;
    @Column(nullable = false)
    private boolean isRetweeted;
    @Column(nullable = false)
    private boolean isFavorited;
    @JoinColumn(nullable = false)
    private UserMentionEntity[] mentions;
    @JoinColumn(nullable = false)
    private URLEntity[] urls;
    @JoinColumn(nullable = false)
    private HashtagEntity[] hashtags;
    @JoinColumn(nullable = false)
    private MediaEntity[] medias;

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public long getReplyStatusId() {
        return replyStatusId;
    }

    public void setReplyStatusId(long replyStatusId) {
        this.replyStatusId = replyStatusId;
    }

    public String getViaName() {
        return viaName;
    }

    public void setViaName(String viaName) {
        this.viaName = viaName;
    }

    public String getViaLink() {
        return viaLink;
    }

    public void setViaLink(String viaLink) {
        this.viaLink = viaLink;
    }

    public long getRetweetFrom() {
        return retweetFrom;
    }

    public void setRetweetFrom(long retweetFrom) {
        this.retweetFrom = retweetFrom;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public void setIsRetweeted(boolean isRetweeted) {
        this.isRetweeted = isRetweeted;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(boolean isFavorited) {
        this.isFavorited = isFavorited;
    }

    public UserMentionEntity[] getMentions() {
        return mentions;
    }

    public void setMentions(UserMentionEntity[] mentions) {
        this.mentions = mentions;
    }

    public URLEntity[] getUrls() {
        return urls;
    }

    public void setUrls(URLEntity[] urls) {
        this.urls = urls;
    }

    public HashtagEntity[] getHashtags() {
        return hashtags;
    }

    public void setHashtags(HashtagEntity[] hashtags) {
        this.hashtags = hashtags;
    }

    public MediaEntity[] getMedias() {
        return medias;
    }

    public void setMedias(MediaEntity[] medias) {
        this.medias = medias;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusEntity{");
        sb.append("statusId=").append(statusId);
        sb.append(", account=").append(account);
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
