package net.orekyuu.javatter.core.column;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.column.ColumnController;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * userstreamのコントローラです。
 */
public class UserStreamController implements ColumnController {
    @FXML
    private ListView<StatusModel> userStreamList;
    private static final int INITIALIZING_LIMIT = 30;
    private static final int STATUSES_LIMIT = 100;

    @Override
    public void setClientUser(Optional<ClientUser> clientUser) {
        // 初期設定タスク
        Task<List<Status>> initializing = new Task<List<Status>>() {
            @Override
            protected List<Status> call() {
                try {
                    if (!clientUser.isPresent()) {
                        return Collections.emptyList();
                    }
                    return clientUser.get().getTwitter().getHomeTimeline(
                            new Paging(1, INITIALIZING_LIMIT));
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return Collections.emptyList();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                clientUser.ifPresent(user -> {
                    List<Status> homeTimeline = getValue();
                    userStreamList
                            .setCellFactory(cell -> new TweetCell(user));
                    homeTimeline.stream()
                            .map(StatusModel.Builder::build)
                            .forEachOrdered(userStreamList.getItems()::add);
                    user.getStream().addOnStatus(
                            status -> {
                                Platform.runLater(() -> {
                                    userStreamList.getItems().add(0,
                                            StatusModel.Builder.build(status));
                                    if (userStreamList.getItems().size() > STATUSES_LIMIT)
                                        userStreamList.getItems().remove(STATUSES_LIMIT);
                                });
                            });
                });
            }
        };
        TaskUtil.startTask(initializing);
    }
}
