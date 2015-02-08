package net.orekyuu.javatter.core.column;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javafx.concurrent.Task;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;

/**
 * 会話を表示するためのWindowを生成するクラス
 * 
 * @author serioriKETC
 *
 */
public class ConversationWindowBuilder {

    private ClientUser clientUser;
    private long statusId;
    private static final int WIDTH = 550;
    private static final int MIN_HEIGHT = 600;

    /**
     * コンストラクタ
     * 
     * @param user
     *            会話を表示したいツイートの存在するカラムの持ち主
     * @param statusId
     *            会話を表示したいツイートのステータスID
     */
    public ConversationWindowBuilder(ClientUser user, long statusId) {
        this.clientUser = user;
        this.statusId = statusId;
    }

    /**
     * 会話を表示するウインドウを表示します
     */
    public void show() {
        ObservableList<StatusModel> conversationList = FXCollections.observableArrayList();
        ListView<StatusModel> conversationListView = new ListView<>(conversationList);
        conversationListView.setCellFactory(cell -> new TweetCell(clientUser));
        conversationListView.setMinHeight(MIN_HEIGHT);
        conversationListView.setMinWidth(WIDTH);
        Stage stage = new Stage();
        Scene scene = new Scene(conversationListView);
        stage.setScene(scene);
        stage.show();
        Task<List<StatusModel>> getConversation = new Task<List<StatusModel>>() {

            @Override
            protected List<StatusModel> call() throws Exception {
                long nextStatusId = statusId;
                List<StatusModel> statusList = new ArrayList<>();
                while (nextStatusId != -1) {
                    StatusModel status = StatusModel.Builder.build(nextStatusId, clientUser);
                    if (status == null) {
                        break;
                    }
                    nextStatusId = status.getReplyStatusId();
                    statusList.add(status);
                }
                return statusList;
            }

            protected void succeeded() {
                getValue().forEach(status -> conversationList.add(status));
            };
        };
        TaskUtil.startTask(getConversation);
    }
}
