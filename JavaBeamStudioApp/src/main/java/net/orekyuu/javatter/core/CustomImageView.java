package net.orekyuu.javatter.core;

import javafx.scene.image.ImageView;
/**
 *カスタムされたイメージビューです。 
 *
 */
public class CustomImageView extends ImageView {
    public CustomImageView() {
        super();
    }

    private OnHoverListener imageHoverListener;
    private OnClickListener onCLickListener;
    /**
     * ホバーイベントのリスナーをセットします
     * @param listener OnHoverListener
     */
    public void addOnHoverListener(OnHoverListener listener) {
        imageHoverListener = listener;
        this.setOnMouseEntered(e->{
            imageHoverListener.onHover(e);
        });
        this.setOnMouseExited(e->{
            imageHoverListener.onHoverd(e);
        });
    }
    /**
     * クリックイベントのリスナーをセットします
     * @param listener OnClickListener
     */
    public void addOnClickListener(OnClickListener listener){
        onCLickListener = listener;
        this.setOnMouseClicked(e->{
            onCLickListener.onCLicked(e);
        });
    }
}
