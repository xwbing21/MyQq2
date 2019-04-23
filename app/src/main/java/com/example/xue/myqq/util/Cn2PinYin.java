package com.example.xue.myqq.util;

import android.text.TextUtils;
import android.util.Log;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 利用pinyin4j将汉字转为音
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/21 22:58
 **/
public class Cn2PinYin {
    private static final String TAG = "Cn2PinYin";

    /**
     * 获取字符串的拼音
     *
     * @param string String
     * @return 拼音字符串
     */
    public static String getPinYin(String string) {
        StringBuilder pinyin = new StringBuilder();
        if (!TextUtils.isEmpty(string)) {
            char[] chars = string.toCharArray();
            // pinyin4j常规操作
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (char aChar : chars) {
                if (String.valueOf(aChar).matches("[\\u4E00-\\u9FA5]+")) {
                    try {
                        String[] pys = PinyinHelper.toHanyuPinyinStringArray(aChar, defaultFormat);
                        for (String py : pys) {
                            pinyin.append(py);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "getPinYin: 转化为拼音失败 ===>" + aChar);
                    }
                } else {
                    pinyin.append(aChar);
                }
            }
        }
        return pinyin.toString();
    }

    /**
     * 获取字符串首字母
     *
     * @param string String
     * @return 首字母
     */
    public static String getFirstLetterPinYin(String string) {
        if (TextUtils.isEmpty(string)) {
            return "#";
        }
        return getPinYin(string).substring(0, 1);
    }

    /**
     * 获取汉字字符串首字母 例如 诸葛亮 zgl
     * @param string String
     * @return 拼音字符串
     */
    public static String getHeadChar(String string) {
        StringBuilder pinyin = new StringBuilder();
        if (!TextUtils.isEmpty(string)) {
            char[] chars = string.toCharArray();
            // pinyin4j常规操作
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (char aChar : chars) {
                if (String.valueOf(aChar).matches("[\\u4E00-\\u9FA5]+")) {
                    try {
                        String s = PinyinHelper.toHanyuPinyinStringArray(aChar, defaultFormat)[0];
                        pinyin.append(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "getPinYin: 转化为拼音失败 ===>" + aChar);
                    }
                } else {
                    pinyin.append(aChar);
                }
            }
        }
        return pinyin.toString();
    }

    /**
     * 根据姓名获取首字母
     *
     * @param name String 姓名
     * @return A-Z or #
     */
    public static String firstLetter4Name(String name) {
        if (TextUtils.isEmpty(name)) {
            return "#";
        }
        // 获取拼音首字母并转成大写
        String firstLetter = getFirstLetterPinYin(name).toUpperCase();
        // 如果不在A-Z中则默认为 #
        if (!firstLetter.matches("[A-Z]")) {
            firstLetter = "#";
        }
        return firstLetter;
    }
}
