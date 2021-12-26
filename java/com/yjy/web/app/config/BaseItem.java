package com.yjy.web.app.config;

import com.yjy.web.comm.utils.JSONUtil;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 选项基类
 */
public abstract class BaseItem {

    private final LinkedList<Map<String, Object>> items;

    /**
     * 黑色
     */
    protected final String COLOR_RED = "label-danger";
    protected final String COLOR_GREEN = "label-success";
    protected final String COLOR_GRAY = "label-default";
    protected final String COLOR_YELLOW = "label-warning";
    protected final String COLOR_BLACK = "";
    
    
    public BaseItem(){
        items = new LinkedList<>();
        callGenItems();
    }

    /**
     * 生成一个新的选项
     * @param pos 选项位置
     * @param item 选项文案
     * @param color 选项颜色
     * @return
     */
    protected Map<String, Object> genItem(Integer pos, String item, String color){
        Map<String, Object> map = new HashMap<>();
        map.put("pos", pos);
        map.put("item", item);
        map.put("color", color);
        return map;
    }

    /**
     * 生成选项列表
     * @param items
     */
    protected void genItems(Pair<String, String>... items){
        if(items != null){
            for(int pos=0;pos<items.length;pos++){
                this.items.add(genItem(pos+1, items[pos].getKey(), items[pos].getValue()));
            }
        }
    }

    @Override
    public String toString() {
        return JSONUtil.toJSON(items);
    }

    /**
     * 子类需要重写这个方法，把选项增加进去
     */
    public abstract void callGenItems();
}
