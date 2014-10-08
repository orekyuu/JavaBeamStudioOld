package net.orekyuu.javatter.api;

public class GlobalAccess {
	private static final GlobalAccess instance = new GlobalAccess();
	private Application application;
	
	private GlobalAccess(){
		
	}
	/**
	 * Applicationを返します。
	 * @return クライアントに唯一のアプリケーションを返します
	 */
	public Application getApplication(){
		return application;
	}
	public static GlobalAccess getInstance(){
		return instance;
	}
	
}
