package com.gpdi.operatingunit.utils;

import java.text.NumberFormat;

/**
 * @desc: String类型转int类型
 */
public class StringToIntUtil {

    public int stringToInt(String data) {
        int newData = 0;
        try {
            if (data.equals("null")) {
                newData = 0;
            } else {

                if (data.contains(".")) {
                    data = data.substring(0, data.lastIndexOf("."));
                    if (!data.equals("0")) {
                        newData = Integer.parseInt(data);
                    }
                } else {
                    newData = Integer.parseInt(data.toString());
                }
            }
        } catch (Exception e) {

        }


        return newData;
    }

    //String 类型截取两个小数点
    public String stringSubTwoDecimalPoint(String str) {

        String last = str.substring(str.lastIndexOf(".") + 1, str.length());
        if (str.contains(".") && last.length() > 2) {
            str = str.substring(0, str.lastIndexOf(".") + 3);
        } else {
        }
        return str;
    }

    //double取两位整数
    public String getDoubleToStringSaveTwo(double value) {
        String str = "";
        try {
            NumberFormat percentInstance = NumberFormat.getPercentInstance();
            percentInstance.setMaximumFractionDigits(2); // 保留小数两位
            str = percentInstance.format(value); // 结果是81.25% ，最后一们四舍五入了

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }


    public static void main(String[] args) {
        StringToIntUtil s = new StringToIntUtil();
        System.out.println(s.stringSubTwoDecimalPoint("32.11000000000001"));
    }

}
