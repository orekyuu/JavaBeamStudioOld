package net.orekyuu.javatter.core.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.orekyuu.javatter.core.Main;

import java.io.IOException;

/**
 * 例外ダイアログを作成するクラス
 */
public class ExceptionDialogBuilder {

    private ExceptionDialogBuilder() {
        throw new AssertionError();
    }

    /**
     * 例外情報を表示するダイアログを表示します。<br>
     * このメソッドを呼び出した時PrimaryStageのcloseメソッドが呼び出されます。<br>
     *
     * @param exception 例外
     */
    public static void create(Exception exception) {
        //Main.getPrimaryStage().close();
        FXMLLoader loader = new FXMLLoader();
        try {
            Scene scene = new Scene(loader.load(Main.class.getResourceAsStream("ExceptionDialog.fxml")));
            ExceptionDialogController controller = loader.getController();
            controller.setException(exception);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("エラー");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
