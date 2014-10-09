package net.orekyuu.javatter.api; 

public class GlobalAccess {
	private static final GlobalAccess instance = new GlobalAccess();
	private Application application;
	
	private GlobalAccess(){
		
	}
	/**
	 * Application��Ԃ��܂��B
	 * @return �N���C�A���g�ɗB��̃A�v���P�[�V������Ԃ��܂�
	 */
	public Application getApplication(){
		return application;
	}
	public static GlobalAccess getInstance(){
		return instance;
	}
	
}
