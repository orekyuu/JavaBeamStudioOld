package net.orekyuu.javatter.core;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.net.MalformedURLException;

/**
 * 画像のプレビュー
 */
public class PreviewImage extends ImageView {

    private ObjectProperty<File> previewFile = new SimpleObjectProperty<>();

    public PreviewImage() {
        previewFile.addListener(e -> {
            File f = previewFile.get();
            if (f == null) {
                imageProperty().set(null);
                return;
            }
            try {
                Image image = new Image(f.toURI().toURL().toExternalForm());
                imageProperty().set(image);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        });
    }

    public void setHoverImageView(ImageView hoverImageView) {
        hoverImageView.setOnMouseClicked(e -> previewFile.set(null));
        hoverImageView.setOnMouseEntered(this::onMouseEnter);
        hoverImageView.setOnMouseExited(this::onMouseExit);
    }

    private void onMouseExit(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        node.setOpacity(0);
    }

    private void onMouseEnter(MouseEvent mouseEvent) {
        if (getPreviewFile() != null) {
            Node node = (Node) mouseEvent.getSource();
            node.setOpacity(1.0);
        }
    }

    /**
     * プレビューで表示しているFileを返します。
     * @return プレビューしているファイル
     */
    public File getPreviewFile() {
        return previewFile.get();
    }

    /**
     * プレビューで表示しているファイルのプロパティです。
     * @return 表示しているファイルのプロパティ
     */
    public ObjectProperty<File> previewFileProperty() {
        return previewFile;
    }

    /**
     * プレビューに表示するファイルを設定します。
     * @param previewFile 表示したい画像ファイル
     */
    public void setPreviewFile(File previewFile) {
        this.previewFile.set(previewFile);
    }
}
