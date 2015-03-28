package net.orekyuu.javatter.api.column;

import net.orekyuu.javatter.api.twitter.ClientUser;

import java.util.Optional;

/**
 * カラムを表すインターフェース
 */
public interface ColumnController {
    /**
     * カラムを所有するClientUserが与えられる
     *
     * @param clientUser ClientUser
     */
    void setClientUser(Optional<ClientUser> clientUser);
}
