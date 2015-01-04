package net.orekyuu.javatter.api.plugin;

import java.lang.annotation.*;

/**
 * プラグインがロードされた時にこのアノテーションがつけられたメソッドが呼び出されます。
 * plugin.infoファイルのエントリポイントに記述したクラスでなければ呼び出されません。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface OnLoad {
}
