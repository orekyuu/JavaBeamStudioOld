package net.orekyuu.javatter.api; 

public class GlobalAccess {
	private static final GlobalAccess instance = new GlobalAccess();
	private Application application;
	
	private GlobalAccess(){
		
	}
	/**
	 * Application アプリケーションを返します。
	 * @return  Application
	 */
	public Application getApplication(){
		return application;
	}
	public static GlobalAccess getInstance(){
		return instance;
	}
	
}
