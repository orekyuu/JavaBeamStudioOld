package net.orekyuu.javatter.api.twitter.stream;

import twitter4j.Status;
import twitter4j.User;

/**
 * お気に入り時の情報
 */
public class OnFavoriteInfo {
    private final long forUser;
    private final User souce;
    private final User target;
    private final Status favoritedStatus;

    public OnFavoriteInfo(long forUser, User souce, User target, Status favoritedStatus) {
        this.forUser = forUser;
        this.souce = souce;
        this.target = target;
        this.favoritedStatus = favoritedStatus;
    }

    public long getForUser() {
        return forUser;
    }

    public User getSouce() {
        return souce;
    }

    public User getTarget() {
        return target;
    }

    public Status getFavoritedStatus() {
        return favoritedStatus;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OnFavoriteInfo{");
        sb.append("forUser=").append(forUser);
        sb.append(", souce=").append(souce.getScreenName());
        sb.append(", target=").append(target.getScreenName());
        sb.append(", favoritedStatus=").append(favoritedStatus.getText());
        sb.append('}');
        return sb.toString();
    }
}
