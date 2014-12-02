package net.orekyuu.javatter.api;

import java.io.File;
import java.util.List;
import java.util.Optional;

import net.orekyuu.javatter.api.twitter.ClientUser;

public interface CurrentWindow {
    /**
     * リプライの設定を行う。
     * @param id リプライ先のステータスid
     * @param destinationName リプライ先のユーザーの名前
     */
   void setReply(long id,String destinationName);
   /**
    * 投稿する画像ファイルを取得する。
    * 投稿する画像ファイルがない場合はnullを返します。
    * @return List<File>
    */
   List<File> getappendedImages();
   /**
    * 現在のツイートを行うユーザーを取得する
    * @return ClientUser
    */
   Optional<ClientUser> getCurrentUser();
   /**
    * ツイートを行うユーザーを変更する。<br>
    * 指定したユーザーが見つからなかった場合はその旨のlogを表示する。
    * @param screenName 投稿したいユーザーのスクリーンネーム
    */
   void changeUser(String screenName);
   /**
    * TextArea内の文字を取得する.
    * @return String
    */
   String getText();
   /**
    * TextAreaに文字列をセットする。
    * @param text セットしたい文字列。
    */
   void setText(String text);
   
}
