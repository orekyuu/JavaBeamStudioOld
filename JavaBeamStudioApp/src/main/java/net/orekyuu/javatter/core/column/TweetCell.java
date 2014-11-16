package net.orekyuu.javatter.core.column;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.Main;
import net.orekyuu.javatter.core.column.TweetCellController;
import net.orekyuu.javatter.core.models.StatusModel;
import twitter4j.Status;

public class TweetCell extends ListCell<StatusModel> {
    /**
     * 所定コントローラ
     */
    private TweetCellController tweetCellController;
    private ClientUser clientUser;

    /**
     * コンストラクタ
     * @param clientuser
     *            UserStreamの持つClientUser
     */
    TweetCell(ClientUser clientuser) {
        super();
        clientUser = clientuser;
    }

    /**
     * アイテムの内容をStatusに従って切り替える
     *
     * @param status
     *            受け取ったステータス
     * @param empty
     *            空かどうか
     */
    @Override
    protected void updateItem(StatusModel status, boolean empty) {
        // スーパークラスから必要な機能を継承
        super.updateItem(status, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
            tweetCellController = null;
        } else {
            // 空でない場合は
            // 名前の取得と表示
            if (tweetCellController == null) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                try {
                    Parent parent = fxmlLoader.load(Main.class
                            .getResourceAsStream("tweetcell.fxml"));
                    setGraphic(parent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tweetCellController = fxmlLoader.getController();
                tweetCellController.setClientUser(clientUser);
            }
            if(tweetCellController != null)
                tweetCellController.updateTweetCell(status);
        }
    }
}
