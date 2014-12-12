package net.orekyuu.javatter.core.column;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import twitter4j.Paging;
import twitter4j.Status;

/**
 * userstreamのコントローラです。
 */
public class UserStreamController implements JavatterColumn {
    @FXML
    private ListView<StatusModel> userStreamList;
    private static final int INITIALIZING_LIMIT = 30;

    @Override
    public void setClientUser(ClientUser clientUser) {
        // 初期設定タスク
        Task<List<Status>> initializing = new Task<List<Status>>() {
            @Override
            protected List<Status> call() {
                List<Status> htl = new ArrayList<Status>();
                try {
                    htl = clientUser.getTwitter().getHomeTimeline(
                            new Paging(1, INITIALIZING_LIMIT));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return htl;
            };

            @Override
            protected void succeeded() {
                super.succeeded();
                List<Status> homeTimeline = getValue();
                userStreamList
                        .setCellFactory(cell -> new TweetCell(clientUser));
                homeTimeline.stream()
                        .map(status -> StatusModel.Builder.build(status))
                        .forEachOrdered(userStreamList.getItems()::add);
                clientUser.getStream().addOnStatus(
                        status -> {
                            Platform.runLater(() -> {
                                userStreamList.getItems().add(0,
                                        StatusModel.Builder.build(status));
                                if (userStreamList.getItems().size() > 100)
                                    userStreamList.getItems().remove(100);
                            });
                        });
            }
        };
        TaskUtil.startTask(initializing);
    }
}
