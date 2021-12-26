package com.yjy.web.app.config.comm;

import com.yjy.web.app.config.BaseItem;
import javafx.util.Pair;

/**
 * 病案打印申请原因
 * @desc PrintMrProgressItem
 * @author rwx
 * @email aba121mail@qq.com
 */
public class PrintMrForItem extends BaseItem {

    public static final String ITEM_SAVE = "保存";
    public static final String ITEM_INSURANCE = "进行保险业务";
    public static final String ITEM_INSURANCE_REIMBURSEMENT = "进行保险报销";
    public static final String ITEM_OTHERS = "其他";

    @Override
    public void callGenItems() {
        genItems(new Pair<>(ITEM_SAVE, COLOR_RED),
                new Pair<>(ITEM_INSURANCE, COLOR_GREEN),
                new Pair<>(ITEM_INSURANCE_REIMBURSEMENT, COLOR_GRAY),
                new Pair<>(ITEM_OTHERS, COLOR_YELLOW));
    }
}
