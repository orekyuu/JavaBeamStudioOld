package net.orekyuu.javatter.core.config;

import java.util.Map;

/**
 * 一般設定の情報
 */
public class GeneralConfigModel {
    private boolean checkTweet = true;
    private boolean checkReply = true;
    private boolean checkRT = true;
    private boolean checkFav = true;
    private String nameDisplayType = NameDisplayType.ID_NAME.name();
    private boolean isExpandURL = true;

    public boolean isCheckTweet() {
        return checkTweet;
    }

    public boolean isCheckReply() {
        return checkReply;
    }

    public boolean isCheckRT() {
        return checkRT;
    }

    public boolean isCheckFav() {
        return checkFav;
    }

    public String getNameDisplayType() {
        return nameDisplayType;
    }

    public boolean isExpandURL() {
        return isExpandURL;
    }

    public void setCheckTweet(boolean checkTweet) {
        this.checkTweet = checkTweet;
    }

    public void setCheckReply(boolean checkReply) {
        this.checkReply = checkReply;
    }

    public void setCheckRT(boolean checkRT) {
        this.checkRT = checkRT;
    }

    public void setCheckFav(boolean checkFav) {
        this.checkFav = checkFav;
    }

    public void setNameDisplayType(String nameDisplayType) {
        this.nameDisplayType = nameDisplayType;
    }

    public void setExpandURL(boolean isExpandURL) {
        this.isExpandURL = isExpandURL;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GeneralConfigModel{");
        sb.append("checkTweet=").append(checkTweet);
        sb.append(", checkReply=").append(checkReply);
        sb.append(", checkRT=").append(checkRT);
        sb.append(", checkFav=").append(checkFav);
        sb.append(", nameDisplayType='").append(nameDisplayType).append('\'');
        sb.append(", isExpandURL=").append(isExpandURL);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralConfigModel that = (GeneralConfigModel) o;

        if (checkFav != that.checkFav) return false;
        if (checkRT != that.checkRT) return false;
        if (checkReply != that.checkReply) return false;
        if (checkTweet != that.checkTweet) return false;
        if (isExpandURL != that.isExpandURL) return false;
        if (nameDisplayType != null ? !nameDisplayType.equals(that.nameDisplayType) : that.nameDisplayType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (checkTweet ? 1 : 0);
        result = 31 * result + (checkReply ? 1 : 0);
        result = 31 * result + (checkRT ? 1 : 0);
        result = 31 * result + (checkFav ? 1 : 0);
        result = 31 * result + (nameDisplayType != null ? nameDisplayType.hashCode() : 0);
        result = 31 * result + (isExpandURL ? 1 : 0);
        return result;
    }

    static GeneralConfigModel create(Map<String, String> map) {
        GeneralConfigModel model = new GeneralConfigModel();
        model.checkFav = Boolean.valueOf(map.getOrDefault("checkFav", String.valueOf(model.checkFav)));
        model.checkReply = Boolean.valueOf(map.getOrDefault("checkReply", String.valueOf(model.checkReply)));
        model.checkRT = Boolean.valueOf(map.getOrDefault("checkRT", String.valueOf(model.checkRT)));
        model.checkTweet = Boolean.valueOf(map.getOrDefault("checkTweet", String.valueOf(model.checkTweet)));
        model.isExpandURL = Boolean.valueOf(map.getOrDefault("isExpandURL", String.valueOf(model.isExpandURL)));
        model.nameDisplayType = map.getOrDefault("nameDisplayType", model.nameDisplayType);
        return model;
    }
}
