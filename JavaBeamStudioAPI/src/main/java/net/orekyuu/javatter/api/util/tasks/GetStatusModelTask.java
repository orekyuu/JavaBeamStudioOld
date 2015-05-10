package net.orekyuu.javatter.api.util.tasks;

import javafx.concurrent.Task;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;

/**
 * {@link StatusModel}を取得する非同期タスクです。
 *
 * @since 1.0.0
 */
public class GetStatusModelTask extends Task<StatusModel> {
    private long id;
    private ClientUser user;

    /**
     * 指定のIDの{@link StatusModel}を取得する非同期タスクを作成します。
     *
     * @param id   ツイートID
     * @param user ツイートを取得する{@link ClientUser}
     * @since 1.0.0
     */
    public GetStatusModelTask(long id, ClientUser user) {
        this.id = id;
        this.user = user;
    }

    @Override
    protected StatusModel call() throws Exception {
        return null;//StatusModelImpl.Builder.build(id, user);
    }
}
