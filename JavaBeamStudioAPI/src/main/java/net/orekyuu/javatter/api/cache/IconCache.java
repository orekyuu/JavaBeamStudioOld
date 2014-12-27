package net.orekyuu.javatter.api.cache;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import javafx.scene.image.Image;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * アイコンのキャッシュ
 *
 * @since 1.0.0
 */
public class IconCache {

    private static LoadingCache<String, Image> cache;

    static {
        cache = CacheBuilder.<String, Image>newBuilder()
                .maximumSize(30)//最大容量を30個
                .softValues()//ImageをSoftReferenceで持つ
                .expireAfterAccess(10, TimeUnit.MINUTES)//アクセスから10分経過すれば削除
                .build(CacheLoader.from(new Function<String, Image>() {
                    @Nullable
                    @Override
                    public Image apply(String input) {
                        return new Image(input);
                    }
                }));

    }

    private IconCache() {
        throw new AssertionError();
    }

    /**
     * 画像を取得する
     *
     * @param url 画像のURL
     * @return Image
     * @since 1.0.0
     */
    public static Image getImage(String url) {
        try {
            return cache.get(url);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
