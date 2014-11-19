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
	 * @param o
	 *            EditTextを継承したオブジェクト
	 */
	public void addChangeTextListener(Object o) {
		if (o instanceof EditText) {
			editTextCallBack = (EditText) o;
			this.textProperty().addListener((s1, s2, s3) -> {
				editTextCallBack.onChanged(getText(), getText().length());
			});
		}
	}

	public void addOnDropListener(Object o) {
		if (o instanceof OnDropListener) {
			onDropCallBack = (OnDropListener) o;
			this.setOnDragOver(e->{
				onDropCallBack.onDrop(e);
			});
			this.setOnDragDropped(e->{
				onDropCallBack.onDroped(e);
			});
		}
	}
}
