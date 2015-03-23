package net.orekyuu.javatter.api.inject;

import java.lang.annotation.*;

/**
 * インジェクションするフィールドを表すアノテーションです。
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface Inject {
}
