package net.orekyuu.javatter.core.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 例外表示ウィンドウのコントローラーです
 */
public class ExceptionDialogController {
    @FXML
    private TextArea stacktrace;

    void setException(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        pw.flush();
        stacktrace.setText(sw.toString());
    }
}
