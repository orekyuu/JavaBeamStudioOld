package net.orekyuu.javatter.api.twitter.stream;

import twitter4j.DirectMessage;

/**
 * ダイレクトメッセージが届いた時の情報
 */
public class OnDirectMessageInfo {
    private final long forUser;
    private final DirectMessage message;

    public OnDirectMessageInfo(long forUser, DirectMessage message) {
        this.forUser = forUser;
        this.message = message;
    }

    public long getForUser() {
        return forUser;
    }

    public DirectMessage getMessage() {
        return message;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OnDirectMessageInfo{");
        sb.append("forUser=").append(forUser);
        sb.append(", message=").append(message.getText());
        sb.append('}');
        return sb.toString();
    }
}
