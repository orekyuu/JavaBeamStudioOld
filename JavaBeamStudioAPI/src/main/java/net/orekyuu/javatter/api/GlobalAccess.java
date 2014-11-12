package net.orekyuu.javatter.api;

public class GlobalAccess {
    private static final GlobalAccess instance = new GlobalAccess();
    private Application application;
    private ColumnRegister columnRegister;

    private GlobalAccess() {

    }

    /**
     * Application アプリケーションを返します。
     *
     * @return Application
     */
    public Application getApplication() {
        return application;
    }

    public static GlobalAccess getInstance() {
        return instance;
    }

    /**
     * カラムのRegisterを返します。
     * @return カラムのRegister
     */
    public ColumnRegister getColumnRegister() {
        return columnRegister;
    }
}
