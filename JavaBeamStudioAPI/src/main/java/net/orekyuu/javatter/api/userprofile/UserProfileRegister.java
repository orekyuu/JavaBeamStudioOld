package net.orekyuu.javatter.api.userprofile;

import java.nio.file.Path;

/**
 * UserProfileTabのレジスタ
 */
public interface UserProfileRegister {

    /**
     * ユーザープロファイルに新しいタブを追加します。
     * @param path FXMLのパス
     */
    void registerUserProfileTab(String path);

    /**
     * ユーザープロファイルに新しいタブを追加します。
     * @param path FXMLのパス
     */
    void registerUserProfileTab(Path path);
}
