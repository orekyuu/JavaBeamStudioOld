package net.orekyuu.javatter.api.entity;

import twitter4j.User;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 永続化されるユーザーの情報を持ったPOJO
 */
@Entity
@Table(name = "TWITTER_USER")
@NamedQueries({
        @NamedQuery(name = UserEntity.FIND_BY_ID_AND_ACCOUNT, query = "SELECT e FROM UserEntity e WHERE e.id = :id AND e.account = :account")
})
public class UserEntity {

    public static final String FIND_BY_ID_AND_ACCOUNT = "UserEntity.findByIdAndAccount";
    @Id
    @OrderBy
    private Long id;
    @Column(nullable = false)
    private Timestamp createdAt;

    private Account account;

    private String description;
    @Column(nullable = false)
    private Integer favCount;
    @Column(nullable = false)
    private Integer followersCount;
    @Column(nullable = false)
    private Integer friendsCount;
    @Column(nullable = false)
    private Integer tweetCount;
    @Column(nullable = false)
    private Integer listedCount;

    private String location;

    private String webSite;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String screenName;
    @Column(nullable = false)
    private String profileImageURL;

    public UserEntity() {

    }

    public UserEntity(Account account, User user) {
        setCreatedAt(Timestamp.from(user.getCreatedAt().toInstant()));
        setDescription(user.getDescription());
        setFavCount(user.getFavouritesCount());
        setFollowersCount(user.getFollowersCount());
        setFriendsCount(user.getFriendsCount());
        setId(user.getId());
        setListedCount(user.getListedCount());
        setLocation(user.getLocation());
        setName(user.getName());
        setScreenName(user.getScreenName());
        setProfileImageURL(user.getProfileImageURL());
        setTweetCount(user.getStatusesCount());
        setWebSite(user.getURL());
        setAccount(account);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFavCount() {
        return favCount;
    }

    public void setFavCount(Integer favCount) {
        this.favCount = favCount;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Integer getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(Integer tweetCount) {
        this.tweetCount = tweetCount;
    }

    public Integer getListedCount() {
        return listedCount;
    }

    public void setListedCount(Integer listedCount) {
        this.listedCount = listedCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (favCount != null ? !favCount.equals(that.favCount) : that.favCount != null) return false;
        if (followersCount != null ? !followersCount.equals(that.followersCount) : that.followersCount != null)
            return false;
        if (friendsCount != null ? !friendsCount.equals(that.friendsCount) : that.friendsCount != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (listedCount != null ? !listedCount.equals(that.listedCount) : that.listedCount != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (profileImageURL != null ? !profileImageURL.equals(that.profileImageURL) : that.profileImageURL != null)
            return false;
        if (screenName != null ? !screenName.equals(that.screenName) : that.screenName != null) return false;
        if (tweetCount != null ? !tweetCount.equals(that.tweetCount) : that.tweetCount != null) return false;
        if (webSite != null ? !webSite.equals(that.webSite) : that.webSite != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (favCount != null ? favCount.hashCode() : 0);
        result = 31 * result + (followersCount != null ? followersCount.hashCode() : 0);
        result = 31 * result + (friendsCount != null ? friendsCount.hashCode() : 0);
        result = 31 * result + (tweetCount != null ? tweetCount.hashCode() : 0);
        result = 31 * result + (listedCount != null ? listedCount.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (webSite != null ? webSite.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (screenName != null ? screenName.hashCode() : 0);
        result = 31 * result + (profileImageURL != null ? profileImageURL.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserEntity{");
        sb.append("id=").append(id);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", description='").append(description).append('\'');
        sb.append(", favCount=").append(favCount);
        sb.append(", followersCount=").append(followersCount);
        sb.append(", friendsCount=").append(friendsCount);
        sb.append(", tweetCount=").append(tweetCount);
        sb.append(", listedCount=").append(listedCount);
        sb.append(", location='").append(location).append('\'');
        sb.append(", webSite='").append(webSite).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", screenName='").append(screenName).append('\'');
        sb.append(", profileImageURL='").append(profileImageURL).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
