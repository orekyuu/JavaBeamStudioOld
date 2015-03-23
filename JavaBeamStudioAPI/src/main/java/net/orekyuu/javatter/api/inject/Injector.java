package net.orekyuu.javatter.api.inject;


import java.lang.reflect.Field;

/**
 * インジェクションを行うクラス
 */
public class Injector {

    private final InjectProperty property;

    /**
     * @param property インジェクションするためのプロパティ
     */
    public Injector(InjectProperty property) {
        this.property = property;
    }

    /**
     * 指定のインスタンスに対してDIを行います
     * @param object DIを行うインスタンス
     */
    public void inject(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Inject annotation = field.getAnnotation(Inject.class);
            if (annotation != null) {
                if (!field.getType().isInterface()) {
                    throw new InjectException(field.getName() + " is not interface. class: " + field.getType().getName());
                }

                field.setAccessible(true);
                Object instance = property.getInstance(field.getType());
                try {
                    field.set(object, instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                //再帰的にinjectする
                inject(instance);
            }
        }
    }
}
