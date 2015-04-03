package net.orekyuu.javatter.core.column;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.column.ColumnController;
import net.orekyuu.javatter.core.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.UserMentionEntity;

import java.util.*;

public class MentionsController implements ColumnController {
    @FXML
    private ListView<net.orekyuu.javatter.api.models.Status> mentionsList;
    private static final int INITIALIZING_LIMIT = 30;
    private static final int STATUSES_LIMIT = 100;
    private Optional<ClientUser> user;

    @Override
    public void setClientUser(Optional<ClientUser> clientUser) {
        user = clientUser;
        Task<List<Status>> initializing = new Task<List<Status>>() {
            @Override
            protected List<Status> call() {
                try {
                    if (!user.isPresent()) {
                        return Collections.emptyList();
                    }
                    return user.get().getTwitter().getMentionsTimeline(
                            new Paging(1, INITIALIZING_LIMIT));
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return new ArrayList<>();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                user.ifPresent(user -> {
                    List<Status> mentionsTimeline = getValue();
                    mentionsList.setCellFactory(cell -> new TweetCell(user));
                    mentionsTimeline.stream()
                            .map(StatusModel::new)
                            .forEachOrdered(mentionsList.getItems()::add);
                    user.getStream().addOnStatus(MentionsController.this::onStatus);
                });
            }
        };
        TaskUtil.startTask(initializing);
    }

    private void onStatus(Status status) {
        user.ifPresent(user -> {
            boolean match = Arrays.stream(status.getUserMentionEntities())
                    .map(UserMentionEntity::getScreenName).anyMatch(name -> name.equals(user.getName()));
            if (match && !status.isRetweet()) {
                Platform.runLater(() -> {
                    mentionsList.getItems().add(0,
                            new StatusModel(status));
                    if (mentionsList.getItems().size() > STATUSES_LIMIT)
                        mentionsList.getItems().remove(STATUSES_LIMIT);
                });
            }
        });
    }
}
