package net.orekyuu.javatter.api.util.tasks;

import javafx.concurrent.Task;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;

/**
 * StatusModelを取得する非同期タスクです。
 */
public class GetStatusModelTask extends Task<StatusModel> {
    private long id;
    private ClientUser user;

    /**
     * 指定のIDのStatusModelを取得する非同期タスクを作成します。
     * @param id ツイートID
     * @param user ツイートを取得するClientUser
     */
    public GetStatusModelTask(long id, ClientUser user) {
        this.id = id;
        this.user = user;
    }

    @Override
    protected StatusModel call() throws Exception {
        return StatusModel.Builder.build(id, user);
    }
}
