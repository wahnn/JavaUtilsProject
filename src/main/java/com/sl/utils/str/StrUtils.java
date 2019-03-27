package com.sl.utils.str;

/**
 * Description(这里用一句话描述这个方法的作用)
 * author: Gao Xueyong
 * Create at: 2018/12/11 16:28
 */
public class StrUtils {

    /**
     * 判断字符串是否全英文  是 true  否 false
     *
     * @param str
     * @return
     */
    public boolean isEngStr(String str) {
        if (str == null || str == "") {
            return false;
        }
        char[] strAry = str.toCharArray();
        String regex = "[a-zA-Z]";
        boolean flag = true;
        for (char cr : strAry) {
            if (!(cr + "").matches(regex)) {
                flag = false;
                break;
            }

        }
        return flag;
    }
}
