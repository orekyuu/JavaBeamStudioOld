package net.orekyuu.javatter.api.models;

import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface Status {
    /**
     * @return �c�C�[�gID
     * @since 1.0.0
     */
    long getStatusId();

    /**
     * @return �c�C�[�g�̃e�L�X�g
     * @since 1.0.0
     */
    String getText();

    /**
     * @return �c�C�[�g���ꂽ����
     * @since 1.0.0
     */
    LocalDateTime getCreatedAt();

    /**
     * @return ���v���C��
     * @since 1.0.0
     */
    long getReplyStatusId();

    /**
     * @return �c�C�[�g���ꂽ�N���C�A���g��
     * @since 1.0.0
     */
    String getViaName();

    /**
     * @return �N���C�A���g�̃����N
     * @since 1.0.0
     */
    String getViaLink();

    /**
     * @return ���c�C�[�g���̃c�C�[�g
     * @since 1.0.0
     */
    Status getRetweetFrom();

    /**
     * @return �c�C�[�g�������[�U�[
     * @since 1.0.0
     */
    User getOwner();

    /**
     * @return ���̃c�C�[�g�����c�C�[�g������
     * @since 1.0.0
     */
    boolean isRetweeted();

    /**
     * @return ���̃c�C�[�g�����C�ɓ��肵����
     * @since 1.0.0
     */
    boolean isFavorited();

    /**
     * @return �c�C�[�g�Ɋ܂܂�郁���V����
     * @since 1.0.0
     */
    List<UserMentionEntity> getMentions();

    /**
     * @return �c�C�[�g�Ɋ܂܂��URL
     * @since 1.0.0
     */
    List<URLEntity> getUrls();

    /**
     * @return �c�C�[�g�Ɋ܂܂��n�b�V���^�O
     * @since 1.0.0
     */
    List<HashtagEntity> getHashtags();

    /**
     * @return �c�C�[�g�Ɋ܂܂�郁�f�B�A
     * @since 1.0.0
     */
    List<MediaEntity> getMedias();
}
