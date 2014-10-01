package net.orekyuu.javatter.core.util;

import net.orekyuu.javatter.api.Cache;
import twitter4j.Status;

import java.util.Map;
import java.util.TreeMap;

/**
 * Statusのキャッシュを行うクラス
 */
public class StatusCache implements Cache<Long, Status>{

    private Map<Long, Status> map = new TreeMap<>();

    @Override
    public Status storage(Status value) {
        map.put(value.getId(), value);
        return null;
    }

    @Override
    public Status get(Long key) {
        return map.get(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void remove(Status value) {
        map.remove(value.getId());
    }

    @Override
    public int size() {
        return map.size();
    }
}
