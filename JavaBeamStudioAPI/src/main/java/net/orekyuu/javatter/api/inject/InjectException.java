package net.orekyuu.javatter.api.inject;

/**
 * インジェクション時に失敗した時に投げられる例外
 */
public class InjectException extends RuntimeException {

    public InjectException(String message) {
        super(message);
    }
}
