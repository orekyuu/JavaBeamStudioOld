package net.orekyuu.javatter.core.twitter.stream;

import net.orekyuu.javatter.api.twitter.stream.JavatterStream;
import net.orekyuu.javatter.api.twitter.stream.ThConsumer;
import twitter4j.*;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * ClientUserが持っているユーザーストリームの情報。
 */
public class JavatterStreamImpl implements JavatterStream, UserStreamListener {

    private Set<Consumer<Exception>> exceptionConsumers = new LinkedHashSet<>();
    private Set<BiConsumer<Long, Long>> scrubGeoConsumers = new LinkedHashSet<>();
    private Set<Consumer<User>> profileUpdateConsumers = new LinkedHashSet<>();
    private Set<Consumer<StatusDeletionNotice>> deletionNoticeConsumers = new LinkedHashSet<>();
    private Set<Consumer<Status>> onStatusConsumers = new LinkedHashSet<>();
    private Set<BiConsumer<User, User>> onBlockConsumers = new LinkedHashSet<>();
    private Set<ThConsumer<User, User, Status>> onFavoriteConsumers = new LinkedHashSet<>();
    private Set<BiConsumer<User, User>> onFollowConsumers = new LinkedHashSet<>();
    private Set<BiConsumer<User, User>> onUnBlockUserConsumers = new LinkedHashSet<>();
    private Set<ThConsumer<User, User, Status>> onUnFavoriteConsumers = new LinkedHashSet<>();
    private Set<BiConsumer<User, User>> onUnFollowConsumers = new LinkedHashSet<>();
    private Set<Consumer<DirectMessage>> onDirectMessageConsumers = new LinkedHashSet<>();
    private Set<BiConsumer<User, UserList>> onUserListUpdateConsumers = new LinkedHashSet<>();
    private Set<ThConsumer<User, User, UserList>> onUserListSubscriptonConsumers = new LinkedHashSet<>();
    private Set<ThConsumer<User, User, UserList>> onUserListUnsubscriptonConsumers = new LinkedHashSet<>();
    private Set<ThConsumer<User, User, UserList>> onUserMemberAdditonConsumers = new LinkedHashSet<>();
    private Set<ThConsumer<User, User, UserList>> onUserMemberDeletionConsumers = new LinkedHashSet<>();
    private Set<BiConsumer<User, UserList>> onUserListDeletionConsumers = new LinkedHashSet<>();
    private Set<BiConsumer<User, UserList>> onUserListCreationConsumers = new LinkedHashSet<>();

    @Override
    public JavatterStream addOnException(Consumer<Exception> exceptionConsumer) {
        exceptionConsumers.add(exceptionConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnScrubGeo(BiConsumer<Long, Long> scrubGeoConsumer) {
        scrubGeoConsumers.add(scrubGeoConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnProfileUpdate(Consumer<User> profileUpdateConsumer) {
        profileUpdateConsumers.add(profileUpdateConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnDeletionNotice(Consumer<StatusDeletionNotice> deletionNotice) {
        deletionNoticeConsumers.add(deletionNotice);
        return this;
    }

    @Override
    public JavatterStream addOnStatus(Consumer<Status> statusConsumer) {
        onStatusConsumers.add(statusConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnBlockUser(BiConsumer<User, User> blockConsumer) {
        onBlockConsumers.add(blockConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnFavorite(ThConsumer<User, User, Status> favoriteConsumer) {
        onFavoriteConsumers.add(favoriteConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnFollow(BiConsumer<User, User> followConsumer) {
        onFollowConsumers.add(followConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnUnBlockUser(BiConsumer<User, User> unBlockConsumer) {
        onUnBlockUserConsumers.add(unBlockConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnUnFavorite(ThConsumer<User, User, Status> unFavoriteConsumer) {
        onUnFavoriteConsumers.add(unFavoriteConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnUnFollow(BiConsumer<User, User> unFollowConsumer) {
        onUnFollowConsumers.add(unFollowConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnDirectMessage(Consumer<DirectMessage> directMessageConsumer) {
        onDirectMessageConsumers.add(directMessageConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnUserListUpdate(BiConsumer<User, UserList> userListUpdateConsumer) {
        onUserListUpdateConsumers.add(userListUpdateConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnUserListSubscripton(ThConsumer<User, User, UserList> userListConsumer) {
        onUserListSubscriptonConsumers.add(userListConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnUserListUnsubscripton(ThConsumer<User, User, UserList> userListConsumer) {
        onUserListUnsubscriptonConsumers.add(userListConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnUserMemberAdditon(ThConsumer<User, User, UserList> addMemberConsumer) {
        onUserMemberAdditonConsumers.add(addMemberConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnUserMemberDeletion(ThConsumer<User, User, UserList> deletionMemberConsumer) {
        onUserMemberDeletionConsumers.add(deletionMemberConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnUserListDeletion(BiConsumer<User, UserList> userListDeletionConsumer) {
        onUserListDeletionConsumers.add(userListDeletionConsumer);
        return this;
    }

    @Override
    public JavatterStream addOnUserListCreation(BiConsumer<User, UserList> userListCreationConsumer) {
        onUserListCreationConsumers.add(userListCreationConsumer);
        return this;
    }

    @Override
    public void onDeletionNotice(long directMessageId, long userId) {
        //使わないので無視
    }

    @Override
    public void onFriendList(long[] friendIds) {
        //動作がわからないので無視
    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus) {
        onFavoriteConsumers.forEach(consumer -> consumer.accept(source, target, favoritedStatus));
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        onUnFavoriteConsumers.forEach(consumer -> consumer.accept(source, target, unfavoritedStatus));
    }

    @Override
    public void onFollow(User source, User followedUser) {
        onFollowConsumers.forEach(consumer -> consumer.accept(source, followedUser));
    }

    @Override
    public void onUnfollow(User source, User unfollowedUser) {
        onUnFollowConsumers.forEach(consumer -> consumer.accept(source, unfollowedUser));
    }

    @Override
    public void onDirectMessage(DirectMessage directMessage) {
        onDirectMessageConsumers.forEach(consumer -> consumer.accept(directMessage));
    }

    @Override
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
        onUserMemberAdditonConsumers.forEach(consumer -> consumer.accept(addedMember, listOwner, list));
    }

    @Override
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
        onUserMemberDeletionConsumers.forEach(consumer -> consumer.accept(deletedMember, listOwner, list));
    }

    @Override
    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
        onUserListSubscriptonConsumers.forEach(consumer -> consumer.accept(subscriber, listOwner, list));
    }

    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
        onUserListUnsubscriptonConsumers.forEach(consumer -> consumer.accept(subscriber, listOwner, list));
    }

    @Override
    public void onUserListCreation(User listOwner, UserList list) {
        onUserListCreationConsumers.forEach(consumer -> consumer.accept(listOwner, list));
    }

    @Override
    public void onUserListUpdate(User listOwner, UserList list) {
        onUserListUpdateConsumers.forEach(consumer -> consumer.accept(listOwner, list));
    }

    @Override
    public void onUserListDeletion(User listOwner, UserList list) {
        onUserListDeletionConsumers.forEach(consumer -> consumer.accept(listOwner, list));
    }

    @Override
    public void onUserProfileUpdate(User updatedUser) {
        profileUpdateConsumers.forEach(consumer -> consumer.accept(updatedUser));
    }

    @Override
    public void onBlock(User source, User blockedUser) {
        onBlockConsumers.forEach(consumer -> consumer.accept(source, blockedUser));
    }

    @Override
    public void onUnblock(User source, User unblockedUser) {
        onUnBlockUserConsumers.forEach(consumer -> consumer.accept(source, unblockedUser));
    }

    @Override
    public void onStatus(Status status) {
        onStatusConsumers.forEach(consumer -> consumer.accept(status));
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        deletionNoticeConsumers.forEach(consumer -> consumer.accept(statusDeletionNotice));
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        //使わないので無視
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        scrubGeoConsumers.forEach(consumer -> consumer.accept(userId, upToStatusId));
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        //使わないので無視
    }

    @Override
    public void onException(Exception ex) {
        exceptionConsumers.forEach(consumer -> consumer.accept(ex));
    }
}
