package net.orekyuu.javatter.core;

import net.orekyuu.javatter.api.EditText;
import javafx.scene.control.TextArea;

/**
 * カスタムされたTextAreaです。
 */
public class CustomTextArea extends TextArea {
    public CustomTextArea() {
        super();
    }

    private EditText editTextCallBack;
    private OnDropListener onDropCallBack;

    /**
     * コールバックするオブジェクトを指定します。
     * 
     * @param CustomTextArea
     *            EditTextを継承したオブジェクト
     */
    public void addChangeTextListener(EditText editText) {

        editTextCallBack = editText;
        this.textProperty().addListener((s1, s2, s3) -> {
            editTextCallBack.onChanged(getText(), getText().length());
        });

    }

    public void addOnDropListener(OnDropListener listener) {
        onDropCallBack = listener;
        this.setOnDragOver(e -> {
            onDropCallBack.onDrop(e);
        });
        this.setOnDragDropped(e -> {
            onDropCallBack.onDroped(e);
        });
    }

}
