package net.orekyuu.javatter.core;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import twitter4j.Status;

public class TweetCell extends ListCell<Status> {
    /**
     * 所定コントローラ
     */
    private TweetCellController tweetCellController;

    /**
     * アイテムの内容をStatusに従って切り替える
     *
     * @param status
     *            受け取ったステータス
     * @param empty
     *            空かどうか
     */
    @Override
    protected void updateItem(Status status, boolean empty) {
        // スーパークラスから必要な機能を継承
        super.updateItem(status, empty);
        if (empty) {
            // 空の場合は表示・描画を行わない
            setText(null);
            setGraphic(null);
            tweetCellController = null;
        } else {
            // 空でない場合は
            // 名前の取得と表示
            if (getGraphic() == null) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                try {
                    Parent parent = (Parent) fxmlLoader.load(Main.class
                            .getResourceAsStream("tweetcell.fxml"));
                    setGraphic(parent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tweetCellController = fxmlLoader.getController();
            }
            tweetCellController.updateTweetCell(status);
        }
    }
}
