package net.orekyuu.javatter.api.inject;

/**
 * 関連付けられたプロパティが見つからなかった時にスローされる例外
 */
public class PropertyMissingException extends RuntimeException {

    public PropertyMissingException(String message) {
        super(message);
    }
}
