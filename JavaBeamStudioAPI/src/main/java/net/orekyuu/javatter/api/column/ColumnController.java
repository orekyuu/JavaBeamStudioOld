package net.orekyuu.javatter.api.column;

import net.orekyuu.javatter.api.twitter.ClientUser;

/**
 * カラムを表すインターフェース
 */
public interface ColumnController {
    /**
     * カラムを所有するClientUserが与えられる
     *
     * @param clientUser ClientUser
     */
    void setClientUser(ClientUser clientUser);
}
