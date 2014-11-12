package net.orekyuu.javatter.core.column;

import java.io.InputStream;

/**
 * Columnを表すインターフェイス
 */
public interface Column extends Comparable<Column> {
    /**
     * カラムの名前
     * @return 名前
     */
    String getName();

    /**
     * カラムのFXMLを読むためのInputStreamを作成します。
     * @return InputStream
     */
    InputStream createInputStream();
}
