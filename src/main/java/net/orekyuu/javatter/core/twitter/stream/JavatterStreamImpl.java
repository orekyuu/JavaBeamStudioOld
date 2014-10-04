package net.orekyuu.javatter.core.twitter.stream;

import net.orekyuu.javatter.api.twitter.stream.JavatterStream;
import net.orekyuu.javatter.api.twitter.stream.ThConsumer;
import twitter4j.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * ClientUserが持っているユーザーストリームの情報。
 */
public class JavatterStreamImpl implements JavatterStream, UserStreamListener {

    @Override
    public JavatterStream addOnException(Consumer<Exception> exceptionConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnScrubGeo(BiConsumer<Long, Long> scrubGeoConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnProfileUpdate(Consumer<User> profileUpdateConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnDeletionNotice(BiConsumer<Long, Long> deletionNotice) {
        return this;
    }

    @Override
    public JavatterStream addOnStatus(Consumer<Status> statusConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnBlockUser(BiConsumer<User, User> blockConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnFavorite(ThConsumer<User, User, Status> favoriteConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnFollow(BiConsumer<User, User> followConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnUnBlockUser(BiConsumer<User, User> unBlockConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnUnFavorite(ThConsumer<User, User, Status> unFavoriteConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnUnFollow(BiConsumer<User, User> unFollowConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnDirectMessage(Consumer<DirectMessage> directMessageConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnUserListUpdate(BiConsumer<User, User> userListUpdateConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnUserListSubscripton(ThConsumer<User, User, UserList> userListConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnUserListUnsubscripton(ThConsumer<User, User, UserList> userListConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnUserMemberAdditon(ThConsumer<User, User, UserList> addMemberConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnUserMemberDeletion(ThConsumer<User, User, UserList> deletionMemberConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnUserListDeletion(BiConsumer<User, UserList> userListDeletionConsumer) {
        return this;
    }

    @Override
    public JavatterStream addOnUserListCreation(BiConsumer<User, UserList> userListCreationConsumer) {
        return this;
    }

    @Override
    public void onDeletionNotice(long directMessageId, long userId) {

    }

    @Override
    public void onFriendList(long[] friendIds) {

    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus) {

    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {

    }

    @Override
    public void onFollow(User source, User followedUser) {

    }

    @Override
    public void onUnfollow(User source, User unfollowedUser) {

    }

    @Override
    public void onDirectMessage(DirectMessage directMessage) {

    }

    @Override
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {

    }

    @Override
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {

    }

    @Override
    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {

    }

    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {

    }

    @Override
    public void onUserListCreation(User listOwner, UserList list) {

    }

    @Override
    public void onUserListUpdate(User listOwner, UserList list) {

    }

    @Override
    public void onUserListDeletion(User listOwner, UserList list) {

    }

    @Override
    public void onUserProfileUpdate(User updatedUser) {

    }

    @Override
    public void onBlock(User source, User blockedUser) {

    }

    @Override
    public void onUnblock(User source, User unblockedUser) {

    }

    @Override
    public void onStatus(Status status) {

    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {

    }

    @Override
    public void onStallWarning(StallWarning warning) {

    }

    @Override
    public void onException(Exception ex) {

    }
}
