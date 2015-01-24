package com.xazux._2dlib.states;

import java.util.HashMap;

/**
 * Created by josh on 23/01/15.
 */
public class StateStore {

    private HashMap<Integer, Object> _intMap = new HashMap<>();
    private HashMap<String, Object> _stringMap = new HashMap<>();

    void StateStore() {

    }

    public void put(int k, Object v) {
        _intMap.put(k, v);
    }

    public boolean containsKey(int k) {
        return _intMap.containsKey(k);
    }

    public boolean remove(int k) {
        return _intMap.remove(k) != null;
    }

    public void put(String k, Object v) {
        _stringMap.put(k, v);
    }

    public boolean containsKey(String k) {
        return _stringMap.containsKey(k);
    }

    public boolean remove(String k) {
        return _stringMap.remove(k) != null;
    }
}
