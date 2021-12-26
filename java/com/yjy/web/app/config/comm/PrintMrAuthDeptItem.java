package com.yjy.web.app.config.comm;

import com.yjy.web.app.config.BaseItem;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * 病案打印授权部门
 * @desc PrintMrAuthDeptItem
 * @author rwx
 * @email aba121mail@qq.com
 */
public class PrintMrAuthDeptItem extends BaseItem {

    public static final String ITEM_DEPT_ADMIN = "总经办";
    public static final String ITEM_DEPT_RND = "研发部";
    public static final String ITEM_DEPT_OFFICE = "办公室";
    public static final String ITEM_DEPT_NET = "网络信息科";
    public static final String ITEM_DEPT_MR = "病案科";

    public static final String ITEM_DEPT_MR_EXT = "[{\"empId\":\"195008\"},{\"empId\":\"185006\"}]";

    @Override
    public void callGenItems() {
        genItems(new Pair<>(ITEM_DEPT_ADMIN, COLOR_BLACK),
                new Pair<>(ITEM_DEPT_RND, COLOR_BLACK),
                new Pair<>(ITEM_DEPT_OFFICE, COLOR_BLACK),
                new Pair<>(ITEM_DEPT_NET, COLOR_BLACK),
                new Pair<>(ITEM_DEPT_MR, ITEM_DEPT_MR_EXT));
    }

    @Override
    public Map<String, Object> genItem(Integer pos, String item, String color){
        Map<String, Object> map = new HashMap<>();
        map.put("pos", pos);
        map.put("item", item);
        map.put("ext", color);
        return map;
    }
}
