package net.orekyuu.javatter.api.service;


import net.orekyuu.javatter.api.models.Status;

import java.util.Optional;

/**
 * ツイートの情報の操作を行うサービスです。
 */
public interface StatusService {

    /**
     * StatusからStatusEntityを作成します。
     * @param status Status
     * @return 引数のstatusを元に作られたStatusEntity
     */
    Status create(Status status);

    /**
     * statusIDからStatusEntityを見つけます。
     * @param statusId ステータスID
     * @return キャッシュか永続化されたものから見つけたStatusEntity
     */
    Optional<Status> findByID(long statusId);

    /**
     * StatusEntityを永続化します。
     * @param status 永続化したいStatusEntity
     */
    void persist(Status status);
}
