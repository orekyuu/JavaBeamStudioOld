package net.orekyuu.javatter.api.twitter.stream;

import twitter4j.User;

/**
 * フォローイベントの情報
 */
public class OnFollowInfo {
    private final long forUser;
    private final User souce;
    private final User followedUser;

    public OnFollowInfo(long forUser, User souce, User followedUser) {
        this.forUser = forUser;
        this.souce = souce;
        this.followedUser = followedUser;
    }

    public long getForUser() {
        return forUser;
    }

    public User getSouce() {
        return souce;
    }

    public User getFollowedUser() {
        return followedUser;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OnFollowInfo{");
        sb.append("forUser=").append(forUser);
        sb.append(", souce=").append(souce.getScreenName());
        sb.append(", followedUser=").append(followedUser.getScreenName());
        sb.append('}');
        return sb.toString();
    }
}
