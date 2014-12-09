package net.orekyuu.javatter.api.util.tasks;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import net.orekyuu.javatter.api.cache.IconCache;
import net.orekyuu.javatter.api.models.UserModel;

/**
 * ユーザーのアイコンを取得する非同期タスク
 */
public class GetIconTask extends Task<Image> {

    private UserModel user;

    /**
     * 指定のユーザーのアイコンを取得する非同期タスクを作成します。
     * @param user アイコンを取得するユーザー
     */
    public GetIconTask(UserModel user) {
        this.user = user;
    }

    @Override
    protected Image call() throws Exception {
        return IconCache.getImage(user.getProfileImageURL());
    }
}
