package net.orekyuu.javatter.api.twitter.stream;

import twitter4j.User;

/**
 * ユーザーをブロックした時の情報
 */
public final class OnBlockInfo {
    private final long forUser;
    private final User souce;
    private final User blockUser;

    public OnBlockInfo(long forUser, User souce, User blockUser) {
        this.forUser = forUser;
        this.souce = souce;
        this.blockUser = blockUser;
    }

    public long getForUser() {
        return forUser;
    }

    public User getSouce() {
        return souce;
    }

    public User getBlockUser() {
        return blockUser;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OnBlockInfo{");
        sb.append("forUser=").append(forUser);
        sb.append(", souce=").append(souce.getScreenName());
        sb.append(", blockUser=").append(blockUser.getScreenName());
        sb.append('}');
        return sb.toString();
    }
}
