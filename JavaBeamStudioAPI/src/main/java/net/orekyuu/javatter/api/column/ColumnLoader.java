package net.orekyuu.javatter.api.column;

import javafx.scene.Parent;
import net.orekyuu.javatter.api.loader.FxLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * �J�����̃��[�h���s�����[�_�[�ł��B
 */
public class ColumnLoader {

    private final Column column;
    private FxLoader columnContentLoader;
    private boolean isLoaded;

    /**
     * �J���������[�h���郍�[�_�[���쐬���܂��B
     * @param column ���[�h����J����
     */
    public ColumnLoader(Column column) {
        this.column = column;
        columnContentLoader = new FxLoader();
    }

    /**
     * �J���������[�h���܂��B
     * @throws IOException FXML�̓ǂݍ��݂ŗ�O������
     * @throws IllegalStateException ���łɃ��[�h����Ă�����
     */
    public void load() throws IOException {
        if (isLoaded) {
            throw new IllegalStateException(column.getPluginId() + ":" + column.getColumnId() + " is loaded.");
        }
        InputStream inputStream = column.getType().createInputStream(column.getColumnPath());
        columnContentLoader.load(inputStream);
        isLoaded = true;
    }

    /**
     * �J�����̃R���g���[��
     * @return �J�����̃R���g���[��
     * @throws IllegalStateException ���[�h����Ă��Ȃ��ꍇ
     */
    public ColumnController getController() {
        if (!isLoaded) {
            throw new IllegalStateException(column.getPluginId() + ":" + column.getColumnId() + " is not loaded.");
        }
        return columnContentLoader.getController();
    }

    /**
     * �J�����̃��[�gNode��Ԃ��܂��B
     * @param <T> ���[�gNode�̌^
     * @return �J�����̃��[�gNode
     * @throws IllegalStateException ���[�h����Ă��Ȃ��ꍇ
     */
    public <T extends Parent> T getRoot() {
        if (!isLoaded) {
            throw new IllegalStateException(column.getPluginId() + ":" + column.getColumnId() + " is not loaded.");
        }
        return columnContentLoader.getRoot();
    }

}
