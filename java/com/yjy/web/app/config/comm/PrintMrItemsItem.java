package com.yjy.web.app.config.comm;

import com.yjy.web.app.config.BaseItem;
import javafx.util.Pair;

/**
 * 病案打印申请项目
 * @desc PrintMrProgressItem
 * @author rwx
 * @email aba121mail@qq.com
 */
public class PrintMrItemsItem extends BaseItem {

    public static final String ITEM_CHECK_RESULT = "检查结果（拍片报告/彩超等）";
    public static final String ITEM_INSPECT_RESULT = "化验结果（血/尿/痰/便等）";
    public static final String ITEM_ASSAY_RESULT = "病理手术化验结果";
    public static final String ITEM_BED_RECORD = "入院记录";
    public static final String ITEM_LEFT_SUM = "出院记录/小结";
    public static final String ITEM_OPERA_RECORD = "手术记录";
    public static final String ITEM_NURSING_RECORD = "护理记录";
    public static final String ITEM_TEMP_RECORD = "体温单";
    public static final String ITEM_MEDICAL_ADVICE = "医嘱单";
    public static final String ITEM_MR_FACE = "病案首页";
    public static final String ITEM_ANTENATAL_FILES = "本院生产提交的产检本复印件";

    @Override
    public void callGenItems() {
        genItems(new Pair<>(ITEM_CHECK_RESULT, COLOR_BLACK),
                new Pair<>(ITEM_INSPECT_RESULT, COLOR_BLACK),
                new Pair<>(ITEM_ASSAY_RESULT, COLOR_BLACK),
                new Pair<>(ITEM_BED_RECORD, COLOR_BLACK),
                new Pair<>(ITEM_LEFT_SUM, COLOR_BLACK),
                new Pair<>(ITEM_OPERA_RECORD, COLOR_BLACK),
                new Pair<>(ITEM_NURSING_RECORD, COLOR_BLACK),
                new Pair<>(ITEM_TEMP_RECORD, COLOR_BLACK),
                new Pair<>(ITEM_MEDICAL_ADVICE, COLOR_BLACK),
                new Pair<>(ITEM_MR_FACE, COLOR_BLACK),
                new Pair<>(ITEM_ANTENATAL_FILES, COLOR_BLACK));
    }
}
