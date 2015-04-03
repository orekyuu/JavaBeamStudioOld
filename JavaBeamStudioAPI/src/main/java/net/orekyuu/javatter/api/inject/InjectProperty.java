package net.orekyuu.javatter.api.inject;

import java.util.HashMap;
import java.util.Map;

/**
 * インジェクションの関連付けを行うプロパティ
 */
public class InjectProperty {

    private Map<Class<?>, Class<?>> injectMap = new HashMap<>();

    /**
     * インジェクションするフィールドの型
     * @param interfaceClass インターフェイスのクラスを渡す
     * @param <T> インターフェイスの型
     * @return {@link net.orekyuu.javatter.api.inject.InjectProperty.InjectMapping}
     */
    public <T> InjectMapping<T> bind(Class<T> interfaceClass) {
        InjectMapping<T> mapping = new InjectMapping<>();
        mapping.interfaceClass = interfaceClass;
        return mapping;
    }

    public class InjectMapping<T> {
        private Class<T> interfaceClass;

        private InjectMapping(){

        }

        /**
         * インジェクションするインスタンスの型
         * @param injectClass インジェクションするクラス
         */
        public void to(Class<? extends T> injectClass) {
            InjectProperty.this.injectMap.put(interfaceClass, injectClass);
        }
    }

    /**
     * 引数のインスタンスに関連付けられるプロパティを返します。
     * @param interfaceClass インスタンスとバインドされているクラス
     * @param <T> インターフェイスの型
     * @return 関連付けられたクラスのインスタンス
     * @throws net.orekyuu.javatter.api.inject.PropertyMissingException 関連付けされたDIプロパティが見つからなかった時
     * @throws java.lang.NullPointerException 引数がnullだった時
     */
    <T> T getInstance(Class<T> interfaceClass) {
        if (interfaceClass == null) {
            throw new NullPointerException();
        }

        T result = null;
        try {
            Class<?> instanceClass = injectMap.get(interfaceClass);
            if (instanceClass == null) {
                throw new PropertyMissingException(interfaceClass.getName());
            }
            Object o = instanceClass.getConstructor().newInstance();
            result = (T) o;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return result;
    }
}
