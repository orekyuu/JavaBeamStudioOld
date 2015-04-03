package net.orekyuu.javatter.core.twitter;

import net.orekyuu.javatter.core.entity.Account;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.twitter.stream.JavatterStream;
import net.orekyuu.javatter.core.twitter.stream.JavatterStreamImpl;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class ClientUserImpl implements ClientUser {

    private Account account;
    private Twitter twitter;
    private JavatterStreamImpl stream;

    public ClientUserImpl(Account account) {
        this.account = account;
        authentication();
    }

    @Override
    public AccessToken getAccessToken() {
        return new AccessToken(account.getAccessToken(), account.getAccessTokenSecret());
    }

    @Override
    public Twitter getTwitter() {
        return twitter;
    }

    @Override
    public String getName() {
        return account.getScreenName();
    }

    @Override
    public JavatterStream getStream() {
        return stream;
    }

    private void authentication() {
        ConfigurationBuilder conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("rMvLmU5qMgbZwg92Is5g")
                .setOAuthConsumerSecret("RD28Uuu44KeMOs90UuqXAAoVTWXRTmD4H8xYKZSgBk")
                .setOAuthAccessToken(account.getAccessToken())
                .setOAuthAccessTokenSecret(account.getAccessTokenSecret());
        twitter = new TwitterFactory(conf.build()).getInstance();

        stream = new JavatterStreamImpl();
        UserStreamAdapter adapter = new UserStreamAdapter() {
            @Override
            public void onException(Exception ex) {
                stream.onException(ex);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                stream.onScrubGeo(userId, upToStatusId);
            }

            @Override
            public void onUserProfileUpdate(User updatedUser) {
                stream.onUserProfileUpdate(updatedUser);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                stream.onDeletionNotice(statusDeletionNotice);
            }

            @Override
            public void onStatus(Status status) {
                stream.onStatus(status);
            }

            @Override
            public void onBlock(User source, User blockedUser) {
                stream.onBlock(source, blockedUser);
            }

            @Override
            public void onFavorite(User source, User target, Status favoritedStatus) {
                stream.onFavorite(source, target, favoritedStatus);
            }

            @Override
            public void onFollow(User source, User followedUser) {
                stream.onFollow(source, followedUser);
            }

            @Override
            public void onUnblock(User source, User unblockedUser) {
                stream.onUnblock(source, unblockedUser);
            }

            @Override
            public void onUnfollow(User source, User unfollowedUser) {
                stream.onUnfollow(source, unfollowedUser);
            }

            @Override
            public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
                stream.onUnfavorite(source, target, unfavoritedStatus);
            }

            @Override
            public void onDirectMessage(DirectMessage directMessage) {
                stream.onDirectMessage(directMessage);
            }

            @Override
            public void onUserListUpdate(User listOwner, UserList list) {
                stream.onUserListUpdate(listOwner, list);
            }

            @Override
            public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
                stream.onUserListSubscription(subscriber, listOwner, list);
            }

            @Override
            public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
                stream.onUserListUnsubscription(subscriber, listOwner, list);
            }

            @Override
            public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
                stream.onUserListMemberAddition(addedMember, listOwner, list);
            }

            @Override
            public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
                stream.onUserListMemberDeletion(deletedMember, listOwner, list);
            }

            @Override
            public void onUserListDeletion(User listOwner, UserList list) {
                stream.onUserListDeletion(listOwner, list);
            }

            @Override
            public void onUserListCreation(User listOwner, UserList list) {
                stream.onUserListCreation(listOwner, list);
            }
        };
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(adapter);
        twitterStream.setOAuthConsumer("rMvLmU5qMgbZwg92Is5g", "RD28Uuu44KeMOs90UuqXAAoVTWXRTmD4H8xYKZSgBk");
        twitterStream.setOAuthAccessToken(getAccessToken());
        String sampleStream = System.getProperty("sampleStream");
        if (sampleStream != null && Boolean.valueOf(sampleStream)) {
            twitterStream.sample();
        } else {
            twitterStream.user();
        }
    }
}
