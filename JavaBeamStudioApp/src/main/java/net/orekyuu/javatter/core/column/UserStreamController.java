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
import twitter4j.TwitterException;

/**
 * userstreamのコントローラです。
 */
public class UserStreamController implements JavatterColumn {
    @FXML
    private ListView<StatusModel> userStreamList;
    private static final int INITIALIZING_LIMIT = 30;
    private static final int STATUSES_LIMIT = 100;

    @Override
    public void setClientUser(ClientUser clientUser) {
        // 初期設定タスク
        Task<List<Status>> initializing = new Task<List<Status>>() {
            @Override
            protected List<Status> call() {
                try {
                    return clientUser.getTwitter().getHomeTimeline(
                            new Paging(1, INITIALIZING_LIMIT));
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return new ArrayList<>();
            };

            @Override
            protected void succeeded() {
                super.succeeded();
                List<Status> homeTimeline = getValue();
                userStreamList
                        .setCellFactory(cell -> new TweetCell(clientUser));
                homeTimeline.stream()
                        .map(StatusModel.Builder::build)
                        .forEachOrdered(userStreamList.getItems()::add);
                clientUser.getStream().addOnStatus(
                        status -> {
                            Platform.runLater(() -> {
                                userStreamList.getItems().add(0,
                                        StatusModel.Builder.build(status));
                                if (userStreamList.getItems().size() > STATUSES_LIMIT)
                                    userStreamList.getItems().remove(STATUSES_LIMIT);
                            });
                        });
            }
        };
        TaskUtil.startTask(initializing);
    }
}
