package net.orekyuu.javatter.core.column;

import java.util.concurrent.CompletableFuture;

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
        HBox hBox = new HBox();
        hBox.getChildren().add(conversationListView);
        hBox.setMinWidth(WIDTH);
        Scene scene = new Scene(hBox);
        stage.setTitle("会話");
        stage.setScene(scene);
        stage.setMinWidth(WIDTH);
        stage.setMaxWidth(WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.show();
        CompletableFuture.runAsync(() -> {
            long nextStatusId = this.statusId;
            while (nextStatusId != -1)
                try {
                    Status status = clientUser.getTwitter().showStatus(nextStatusId);
                    Platform.runLater(() -> conversationList.add(StatusModel.Builder.build(status)));
                    nextStatusId = status.getInReplyToStatusId();
                } catch (TwitterException e) {
                    e.printStackTrace();
                    nextStatusId = -1;
                }
        });
    }

}
