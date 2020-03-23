package com.sjm5z.community.common;

/**
 *
 */
public class StringCommon {

    /**
     * 將字符串转换为数字
     * @return
     */
    public static Integer StringToInteger(String str){
        if(str == null || str.length() == 0){
            return 1;
        }
        int num = str.charAt(0) - 48;
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if(c < 48 || c > 57){
                break;
            }
            num *= 10;
            num += c - 48;

        }
        return num;
    }

}
