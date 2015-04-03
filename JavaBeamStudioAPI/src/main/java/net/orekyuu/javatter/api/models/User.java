package net.orekyuu.javatter.api.models;

import java.time.LocalDateTime;

public interface User {
    /**
     * @return ���[�U�[���쐬���ꂽ���t
     * @since 1.0.0
     */
    LocalDateTime getCreatedAt();

    /**
     * @return ���[�U�[�̐�����
     * @since 1.0.0
     */
    String getDescription();

    /**
     * @return ���[�U�[�̂��C�ɓ��萔
     * @since 1.0.0
     */
    int getFavCount();

    /**
     * @return ���[�U�[�̃t�H�����[��
     * @since 1.0.0
     */
    int getFollowersCount();

    /**
     * @return ���[�U�[�̃t�H���[��
     * @since 1.0.0
     */
    int getFriendsCount();

    /**
     * @return ���[�U�[ID
     * @since 1.0.0
     */
    long getId();

    /**
     * @return �ǉ�����Ă��郊�X�g�̐�
     * @since 1.0.0
     */
    int getListedCount();

    /**
     * @return ���[�U�[�̃v���t�B�[���ɐݒ肳��Ă���ꏊ
     * @since 1.0.0
     */
    String getLocation();

    /**
     * @return ���[�U�[�̖��O
     * @since 1.0.0
     */
    String getName();

    /**
     * @return ���[�U�[�̃X�N���[���l�[��
     * @since 1.0.0
     */
    String getScreenName();

    /**
     * @return ���[�U�[�A�C�R����URL
     * @since 1.0.0
     */
    String getProfileImageURL();

    /**
     * @return ���[�U�[�̃c�C�[�g��
     * @since 1.0.0
     */
    int getTweetCount();

    /**
     * @return ���[�U�[��Web�T�C�g
     * @since 1.0.0
     */
    String getWebSite();
}
