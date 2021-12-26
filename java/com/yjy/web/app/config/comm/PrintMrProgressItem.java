package com.yjy.web.app.config.comm;

import com.yjy.web.app.config.BaseItem;
import javafx.util.Pair;

/**
 * 病案打印申请进度
 * @desc PrintMrProgressItem
 * @author rwx
 * @email aba121mail@qq.com
 */
public class PrintMrProgressItem extends BaseItem {

    public static final String ITEM_CHECKING = "未审核";
    public static final String ITEM_FAIL = "未通过";
    public static final String ITEM_PRINTING = "打印中";
    public static final String ITEM_TO_PAY = "未支付";
    public static final String ITEM_PAID_VERIFYING = "核对中";
    public static final String ITEM_PAID = "已支付";
    public static final String ITEM_SENT = "已邮寄";
    public static final String ITEM_CANCEL = "已取消";

    @Override
    public void callGenItems() {
        genItems(new Pair<>(ITEM_CHECKING, COLOR_GREEN),
                new Pair<>(ITEM_FAIL, COLOR_RED),
                new Pair<>(ITEM_PRINTING, COLOR_GREEN),
                new Pair<>(ITEM_TO_PAY, COLOR_GREEN),
                new Pair<>(ITEM_PAID_VERIFYING, COLOR_GREEN),
                new Pair<>(ITEM_PAID, COLOR_GREEN),
                new Pair<>(ITEM_SENT, COLOR_GREEN),
                new Pair<>(ITEM_CANCEL, COLOR_GRAY));
    }

}
