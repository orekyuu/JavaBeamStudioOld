package net.orekyuu.javatter.api.twitter.stream;

/**
 * 3つの入力引数を受け取って結果を返さないオペレーションを表します。
 *
 * @param <T> 第一引数の型
 * @param <U> 第二引数の型
 * @param <V> 第三引数の型
 * @since 1.0.0
 */
@FunctionalInterface
public interface ThConsumer<T, U, V> {
    /**
     * 指定された引数でこのオペレーションを実行します。
     *
     * @param t 第1引数
     * @param u 第2引数
     * @param v 第3引数
     * @since 1.0.0
     */
    void accept(T t, U u, V v);
}
