package com.example.xue.myqq.view.group;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/24 11:43
 **/
public class GroupItem {
    // 起始position
    private int startPosition;
    private Map<String,Object> dataMap;

    public GroupItem(int startPosition){
        this.startPosition = startPosition;
        dataMap = new HashMap<>();
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setData(String key,Object data){
        dataMap.put(key,data);
    }

    public Object getData(String key){
        return dataMap.get(key);
    }
}
