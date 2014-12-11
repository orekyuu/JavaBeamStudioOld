package net.orekyuu.javatter.core.column;

import java.util.List;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import twitter4j.Status;

/**
 * userstreamのコントローラです。
 */
public class UserStreamController implements JavatterColumn {
    @FXML
    private ListView<StatusModel> userStreamList;
    private final int INITIALIZING_LIMIT = 30;

    @Override
    public void setClientUser(ClientUser clientUser) {
        // 初期設定タスク
        Task initializing = new Task() {
            @Override
            protected Object call() {
                List<Status> homeTimeline = null;
                try {
                    homeTimeline = clientUser.getTwitter().getHomeTimeline();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                homeTimeline
                        .stream()
                        .limit(INITIALIZING_LIMIT)
                        .forEachOrdered(
                                status -> {
                                    userStreamList.getItems().add(
                                            StatusModel.Builder.build(status));
                                });
                return null;
            }
            @Override
            protected void succeeded() {
            	super.succeeded();
                userStreamList
                        .setCellFactory(cell -> new TweetCell(clientUser));
                clientUser.getStream().addOnStatus(status -> {
                    Task statusAdding = new Task() {
                        @Override
                        protected Object call() {
                            userStreamList.getItems().add(0,
                                    StatusModel.Builder.build(status));
                            if (userStreamList.getItems().size() > 100)
                                userStreamList.getItems().remove(100);
                            return null;
                        }
                    };
                    TaskUtil.startTask(statusAdding);
                });
            }
        };
        TaskUtil.startTask(initializing);

    }
}
