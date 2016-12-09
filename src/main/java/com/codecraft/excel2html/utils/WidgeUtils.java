package com.codecraft.excel2html.utils;

import java.util.HashMap;
import java.util.Map;

public class WidgeUtils {

	//解析控件属性
	public static Map<String,String> parseAttr(String widgeType) {
		Map<String,String> map = new HashMap<String,String>();
		
		String[] arrs = widgeType.split("[\\[\\]]");
		for(String s : arrs){
			if(!"".endsWith(s) && s.indexOf("=") != -1 ){
				String key = s.substring(0, s.indexOf("="));
				String value = s.substring(s.indexOf("=")+1);
				map.put(key.toLowerCase(), value);
			}
		}
		return map;
	}
	
	/*
	 * 用于将Excel表格中列号字母转成列索引，从1对应A开始
	 */
	public static int columnToIndex(String column) {
		column = column.toUpperCase();
        if (!column.matches("[A-Z]+")) {
            try {
                throw new Exception("Invalid parameter");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int index = 0;
        char[] chars = column.toUpperCase().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            index += ((int) chars[i] - (int) 'A' + 1)
                * (int) Math.pow(26, chars.length - i - 1);
        }
        return index;
	}
	
	/*
	 * 用于将Excel表格中第几列转为字母,columnNum为1返回A
	 */
	public static String getColumnName(int columnNum) {
        int first;
        int last;
        String result = "";
        if (columnNum > 256)
                columnNum = 256;
        first = columnNum / 27;
        last = columnNum - (first * 26);

        if (first > 0){
            result = String.valueOf((char) (first + 64));
        }
        if (last > 0){
            result = result + String.valueOf((char) (last + 64));
        }
        result = result.toLowerCase();
        return result;
	}
}
