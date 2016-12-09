package com.codecraft.excel2html.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.SystemException;

/**
 * 字符串工具
 * 
 * @author xieshh
 * 
 */
public class StringsUtils {

	/**
	 * 字串是否为null
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isNull(String str) {
		String temStr = str;
		if (str != null) {
			temStr = str.trim();
			if (!"NULL".equals(temStr.toUpperCase())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 从字串中找一个字符的地址
	 * 
	 * @param str
	 * @return
	 */
	public static final int indexOf(String str, int offset, char c,
			boolean fromLeft) {
		if (fromLeft) {
			return str.indexOf(c, offset);
		} else {
			byte bytes[] = str.getBytes();
			int i = offset - 1;
			for (; i >= 0; i--) {
				if (bytes[i] == c) {
					break;
				}
			}
			return i;

		}
	}

	/**
	 * 把从start到end用desc替换掉
	 * 
	 * @param src
	 * @param start
	 * @param end
	 * @param desc
	 * @return
	 */
	public static final String replaceAll(String src, int start, int end,
			String desc) {

		byte srcBytes[] = src.getBytes();
		byte descBytes[] = desc.getBytes();
		byte tBytes[] = new byte[srcBytes.length + descBytes.length
				- (end - start)];
		for (int i = 0; i < start; i++) {
			tBytes[i] = srcBytes[i];
		}
		for (int i = 0; i < descBytes.length; i++) {
			tBytes[i + start] = descBytes[i];
		}
		for (int i = end; i < srcBytes.length; i++) {
			tBytes[i - end + start + descBytes.length] = srcBytes[i];
		}
		return new String(tBytes);
	}

	/**
	 * 把str从start到end的src用desc替换掉
	 * 
	 * @param src
	 * @param start
	 * @param end
	 * @param desc
	 * @return
	 */
	public static final String replaceAll(String str, String src, int start,
			int end, String desc) {

		char srcBytes[] = src.toCharArray();
		char descBytes[] = desc.toCharArray();
		char strBytes[] = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end && i < strBytes.length; i++) {
			char tempByte = strBytes[i];
			// 如果符合条件，同替换处理
			if (equals(strBytes, i, srcBytes)) {
				i += srcBytes.length - 1;
				sb.append(descBytes);
			} else {
				sb.append(tempByte);
			}
		}
		return sb.toString();
	}

	/**
	 * 把str从start到end的src用desc替换掉
	 * 
	 * @param src
	 * @param start
	 * @param end
	 * @param desc
	 * @return
	 */
	public static final String replaceAll(String str, String src, String desc) {
		if (isEmpty(str)) {
			return "";
		}
		int start = 0;
		int end = str.length();
		return replaceAll(str, src, start, end, desc);
	}

	

	/**
	 * str字串，从START开始是否与desc相同
	 * 
	 * @param src
	 * @param start
	 * @param desc
	 * @return
	 */
	private static final boolean equals(char srcBytes[], int start,
			char descBytes[]) {
		for (int i = start; i < srcBytes.length && i - start < descBytes.length; i++) {
			char a = srcBytes[i];
			char b = descBytes[i - start];
			if (a != b) {
				return false;
			}

		}
		return true;
	}

	/**
	 * str字串，从START开始是否与desc相同
	 * 
	 * @param src
	 * @param start
	 * @param desc
	 * @return
	 */
	public static final boolean equals(String src, int start, String desc) {
		char srcBytes[] = src.toCharArray();
		char descBytes[] = desc.toCharArray();
		return equals(srcBytes, start, descBytes);
	}

	public static final boolean isEmpty(String str) {
		String temStr = str;
		if (str != null) {
			temStr = str.trim();
			if (temStr.length() != 0) {
				if (!"NULL".equals(temStr.toUpperCase())) {
					if (!"".equals(temStr)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 一句话(line) 包括单词(subStr) aa 包含[aaaa aa]
	 * 
	 * @param line
	 * @param subStr
	 * @return
	 */
	public static final boolean hasWord(String line, String subStr) {
		if (subStr == null || line == null) {
			return false;
		}
		byte lineBytes[] = line.getBytes();
		byte subBytes[] = subStr.getBytes();
		if (lineBytes.length < subBytes.length) {
			byte temp[] = lineBytes;
			lineBytes = subBytes;
			subBytes = temp;
		}
		for (int i = 0; i < lineBytes.length - subBytes.length; i++) {
			if (i != 0) {
				if (!isWordSeparator((char) lineBytes[i])) {
					continue;
				} else {
					i++;
				}
			} else {
				if (isWordSeparator((char) lineBytes[i])) {
					i++;
				}
			}
			int j = 0;
			for (j = 0; j < subBytes.length; j++) {
				if (lineBytes[i + j] != subBytes[j]) {
					break;
				}
			}
			if (j == subBytes.length) {
				if (i != lineBytes.length - subBytes.length) {
					if (!isWordSeparator((char) lineBytes[i + j])) {
						continue;
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是单词分隔字符
	 * 
	 * @param c
	 * @return
	 */
	public static final boolean isWordSeparator(char c) {
		if (c == ' ' || c == '\t') {
			return true;
		}
		return false;
	}

	/**
	 * 取出括号(...)的中间内容
	 * 
	 * @param src
	 * @param a
	 * @param b
	 * @return
	 */
	public static String betweenPair(char cs[], int offset, char a, char b) {
		// int ret[] = null;
		int i = offset;
		for (; i < cs.length; i++) {
			char c = cs[i];
			if (c == a) {
				break;
			}
		}
		// System.out.println(cs[i]);
		long count = 0;
		for (int j = i; j < cs.length; j++) {
			char c = cs[j];
			if (c == a) {
				count++;
			} else if (c == b) {
				count--;
			}
			if (count == 0) {
				return new String(cs, i + 1, j - i - 1);
			}

		}

		return "";
	}

	/**
	 * 取出括号(...)的中间内容
	 * 
	 * @param src
	 * @param a
	 * @param b
	 * @return
	 */
	public static int[] betweenPairIndex(String src, char a, char b) {
		int ret[] = null;
		byte bytes[] = src.getBytes();
		int i = 0;
		for (i = 0; i < bytes.length; i++) {
			if (bytes[i] == a) {
				break;
			}
		}
		long count = 0;
		for (int j = i; j < bytes.length; j++) {
			// cout<<str[i]<<endl;
			if (bytes[j] == a) {
				count++;
			} else if (bytes[j] == b) {
				count--;
			}
			if (count == 0) {
				return new int[] { i + 1, j };
			}

		}

		return ret;
	}

	/**
	 * 取出括号(...)的中间内容
	 * 
	 * @param src
	 * @param a
	 * @param b
	 * @return
	 */
	public static String betweenPair(String src, char a, char b) {
		String ret = "";
		byte bytes[] = src.getBytes();
		int i = 0;
		for (i = 0; i < bytes.length; i++) {
			if (bytes[i] == a) {

				long count = 0;
				for (int j = i; j < bytes.length; j++) {
					// cout<<str[i]<<endl;
					if (bytes[j] == a) {
						count++;
					} else if (bytes[j] == b) {
						count--;
					}
					if (count == 0) {
						return new String(bytes, i + 1, j - 1);
					}

				}

			}
		}

		return ret;
	}

	/**
	 * 取出括号(...)的中间内容
	 * 
	 * @param src
	 * @param a
	 * @param b
	 * @return
	 */
	public static List<String> between4Quote(String src, char a) {
		List<String> ret = new ArrayList<String>();
		char bytes[] = src.toCharArray();
		between4Quote(bytes, 0, a, ret);

		return ret;
	}

	private static void between4Quote(char bytes[], int offset, char a,
			List<String> ret) {
		int i = between4Quote(bytes, offset, a);
		if (i != -1) {
			int index = between4Quote(bytes, i + 1, a);
			if (index != -1) {
				ret.add(new String(bytes, i + 1, index - i - 1));
				between4Quote(bytes, index + 1, a, ret);
			}

		}
	}

	/**
	 * 取出括号(...)的中间内容
	 * 
	 * @param src
	 * @param a
	 * @param b
	 * @return
	 */
	private static int between4Quote(char cs[], int offset, char a) {

		for (int i = offset; i < cs.length; i++) {
			if (cs[i] == a) {
				return i;
			}
		}
		return -1;

	}

	/**
	 * 判断字串中，a与b成对的出现
	 * 
	 * @param src
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isAllInPair(String src, char a, char b) {
		long count = 0;
		byte bytes[] = src.getBytes();
		for (int i = 0; i < bytes.length; i++) {

			if (bytes[i] == a) {
				count++;
			} else if (bytes[i] == b) {
				count--;
			}

		}
		if (count == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 从src中取出包括str任意字符串的开始地址
	 * 
	 * @param src
	 * @param str
	 * @return
	 */
	public static int indexInclude(String src, String str) {
		int ret = -1;
		byte bytes[] = src.getBytes();
		byte iBytes[] = str.getBytes();

		for (int i = 0; i < bytes.length; i++) {

			for (int j = 0; j < iBytes.length; j++) {

				if (bytes[i] == iBytes[j]) {
					return i;
				}

			}

		}

		return ret;
	}

	/***************************************************************************
	 * 判断字串是否是数字字串
	 **************************************************************************/
	public static final boolean isNumber(String str) {
		String tempStr = str.trim();
		if (tempStr.startsWith("-")) {
			tempStr = tempStr.substring(1, tempStr.length());
		}
		if (tempStr.startsWith("+")) {
			tempStr = tempStr.substring(1, tempStr.length());
		}
		byte bytes[] = tempStr.getBytes();
		int count = 0;
		for (int i = 0; i < bytes.length; i++) {
			if (".".indexOf(bytes[i]) != -1) {
				if (count > 0) {
					return false;
				} else {
					count++;
					continue;
				}
			}
			if ("1234567890".indexOf(bytes[i]) == -1) {
				return false;
			}

		}
		if (bytes.length == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断是否是布尔值
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isBoolean(String str) {
		if (isEmpty(str)) {
			return false;
		}
		if ("true".equals(str.toLowerCase())) {
			return true;
		} else if ("false".equals(str.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 取第一个字
	 * 
	 * @param src
	 * 
	 * @return
	 */
	public static String firstWord(String src) {
		String ret = "";
		byte bytes[] = src.getBytes();
		for (int i = 0; i < bytes.length; i++) {

			if (bytes[i] == ' ' || bytes[i] == '\n' || bytes[i] == '\t') {
				return new String(bytes, 0, i);
			}

		}
		return ret;
	}

	/**
	 * 第一个字的结束地址
	 * 
	 * @param src
	 * 
	 * @return
	 */
	public static int firstWordAt(String src) {
		int ret = -1;
		byte bytes[] = src.getBytes();
		for (int i = 0; i < bytes.length; i++) {

			if (bytes[i] == ' ' || bytes[i] == '\n' || bytes[i] == '\t') {
				return i;
			}

		}
		return ret;
	}

	public static List<Object> getListFromListString(String listString) {
		List<Object> list = new ArrayList<Object>();
		String temp = "";
		temp = betweenPair(listString, '[', ']');
		if (!"".equals(temp)) {
			long mIndex = temp.indexOf('{');

			if (mIndex != -1) {
				int index[] = betweenPairIndex(temp, '{', '}');
				String subStr = temp.substring(index[0] - 1, index[1] + 1);
				// Map subParam = getMapFromMapString(subStr);
				int keyStart = indexOf(temp, index[0], ',', false);
				if (keyStart == -1) {
					keyStart = 0;
				}
				list.add(subStr);
				// temp = temp.replace(subStr, "");
				temp = replaceAll(temp, keyStart, index[1] + 1, "");
				list.addAll(getListFromListString("[" + temp + "]"));
				return list;
			}

			String valuesString[] = temp.split(",");

			for (int i = 0; i < valuesString.length; i++) {
				list.add(valuesString[i]);
				// log.debug(key);
			}

		}
		return list;
	}

	public static String[] getArrayFromListString(String arrayString) {

		String temp = "";
		temp = betweenPair(arrayString, '[', ']');
		if (!"".equals(temp)) {

			return temp.split(",");

		}
		return null;
	}

	/**
	 * 把以desc结尾的去掉:
	 * 
	 * @param src
	 * @return
	 */
	public static String cust(String src, String desc) {

		long index = src.lastIndexOf(desc);
		if (index == -1) {
			return src;
		} else {
			return src.substring(0, src.length() - desc.length());
		}

	}

	/**
	 * 把字串变为指定长度，用指定值填补空位
	 * 
	 * @param line
	 * @return
	 * @throws Exception
	 */
	public static String fixStringToLength(String numStr, int length,
			char addChar) {
		if (length <= 0) {
			return "";
		}
		if (numStr.length() == length) {
			return numStr;
		} else if (numStr.length() > length) {
			return numStr.substring(0, length);
		}
		char bytes[] = new char[length];
		char cs[] = numStr.toCharArray();
		int fromId = length - cs.length;

		for (int i = 0; i < fromId; i++) {
			bytes[i] = addChar;
		}
		for (int i = 0; i < cs.length; i++) {
			bytes[fromId] = cs[i];
			fromId++;
		}

		return new String(bytes, 0, fromId);
	}

	/**
	 * newObject=StringUtil.fixStringToHttpHeadKey(obj, '-');
	 * 
	 * @param numStr
	 * @param addChar
	 * @return
	 */
	public static String fixStringToHttpHeadKey(String numStr, char addChar) {

		char bytes[] = numStr.toCharArray();
		char newBytes[] = new char[bytes.length];
		for (int i = 0; i < newBytes.length; i++) {
			char c = bytes[i];

			if (i == 0) {
				if (c > 'Z') {
					newBytes[i] = (char) (c - 32);
				} else {
					newBytes[i] = c;
				}
				continue;
			} else {
				char bc = bytes[i - 1];
				if (bc == addChar) {
					if (c > 'Z') {
						newBytes[i] = (char) (c - 32);
					} else {
						newBytes[i] = c;
					}
					continue;
				} else {
					newBytes[i] = c;
				}
			}

		}
		return new String(newBytes);
	}

	/**
	 * 当字串大于指定值时，把多出的字串截掉，并以...结尾
	 * 
	 * @param line
	 * @return
	 * @throws Exception
	 */
	public static String custStringBigByLength(String numStr, int length) {

		if (length <= 0) {
			return "";
		} else if (length == 1) {
			return ".";
		} else if (length == 2) {
			return "..";
		} else if (length == 3) {
			return "...";
		} else {
			if (numStr.length() <= length) {
				return numStr;
			} else {
				StringBuilder sb = new StringBuilder(numStr.substring(0,
						length - 3));
				sb.append("...");
				return sb.toString();
			}
		}
	}

	/**
	 * 取得 lc到rc之间的字串
	 * 
	 * @param str
	 * @param lc
	 * @param rc
	 * @return
	 */
	public static String getInnerString(String str, char lc, char rc) {
		if (str == null || str.length() == 0) {
			return "";
		}
		char cs[] = str.toCharArray();
		int i = 0;
		for (i = 0; i < cs.length; i++) {
			char c = cs[i];
			if (c == lc) {
				break;
			}
		}
		int lId = i;
		for (i = cs.length - 1; i >= lId; i--) {
			char c = cs[i];
			if (c == rc) {
				break;
			}
		}
		int rId = i;
		if (lId >= rId) {
			return "";
		}
		return new String(cs, lId + 1, rId - lId - 1);
	}

	

	private static final int[] allChineseScope = { 1601, 1637, 1833, 2078,
			2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730,
			3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5600,
			Integer.MAX_VALUE };

	public static final char unknowChar = '*';

	private static final char[] allEnglishLetter = { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'W', 'X', 'Y', 'Z', unknowChar };

	/**
	 * 取中文拼音首字符
	 * 
	 * @param str
	 * @return
	 */
	public static char getFirstLetterFromChinessWord(String str) {
		char result = '*';
		String temp = str.toUpperCase();
		try {

			byte[] bytes = temp.getBytes("gbk");
			if ((bytes[0] & 0x0FF) < 128 && (bytes[0] & 0x0FF) > 0) {
				return (char) bytes[0];
			}

			int gbkIndex = 0;

			for (int i = 0; i < bytes.length; i++) {
				bytes[i] -= 160;
			}
			gbkIndex = bytes[0] * 100 + bytes[1];
			for (int i = 0; i < allEnglishLetter.length; i++) {
				if (i == 22) {
					// System.out.println(allEnglishLetter.length
					// +" "+allChineseScope.length);
				}
				if (gbkIndex >= allChineseScope[i]
						&& gbkIndex < allChineseScope[i + 1]) {
					result = allEnglishLetter[i];
					break;
				}
			}

		} catch (Exception e) {

		}
		return result;
	}

	

	/**
	 * 从char[]缓冲中取一行
	 * 
	 * @param cs
	 * @param startId
	 * @return
	 * @throws Exception
	 */
	public static int getLine(char cs[], int count, int offset)
			throws Exception {
		if (cs == null || cs.length == 0) {
			return -1;

		}
		if (count < 1) {
			return -1;

		}
		if (count > cs.length) {
			count = cs.length;

		}

		for (int i = offset; i < count; i++) {
			int b = cs[i];
			// if (b == -1) {
			// ExceptionFactory.throwSystemException("", "读取行失败",
			// new Object[] {});
			// }
			if (b == '\r') {
				if (i + 1 < count) {
					int a = cs[i + 1];
					if (a == '\n') {
						return i + 1;
					} else {
						i++;
					}
				}

			} else {
				if (b == '\n') {
					return i;
				} else {
					continue;
				}

			}
		}

		return -1;
	}

	/**
	 * 从char[]缓冲中取一行
	 * 
	 * @param cs
	 * @param startId
	 * @return
	 * @throws Exception
	 */
	public static int getLine(byte cs[], int count, int offset)
			throws Exception {
		if (cs == null || cs.length == 0) {
			return -1;

		}
		if (count < 1) {
			return -1;

		}
		if (count > cs.length) {
			count = cs.length;

		}

		for (int i = offset; i < count; i++) {
			int b = cs[i];
			// if (b == -1) {
			// ExceptionFactory.throwSystemException("", "读取行失败",
			// new Object[] {});
			// }
			if (b == '\r') {
				if (i + 1 < count) {
					int a = cs[i + 1];
					if (a == '\n') {
						return i + 1;
					} else {
						i++;
					}
				}

			} else {
				if (b == '\n') {
					return i;
				} else {
					continue;
				}

			}
		}

		return -1;
	}

	/**
	 * 把字串变为double
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static double getDouble(String str) {
		double defaultValue = 0;
		return getDouble(str, defaultValue);
	}

	/**
	 * 把字串变为double
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static double getDouble(String str, double defaultValue) {
		if (isEmpty(str)) {
			return defaultValue;
		} else {
			if (isNumber(str)) {
				return Double.parseDouble(str);
			} else {
				return defaultValue;
			}

		}
	}

	/**
	 * 把字串变为long
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static long getLong(String str, long defaultValue) {
		try {
			if (isEmpty(str)) {
				return defaultValue;
			} else {
				if (isNumber(str)) {
					return Long.parseLong(str);
				} else {
					return defaultValue;
				}

			}
		} catch (java.lang.NumberFormatException e) {
			throw new RuntimeException("字符格式太长了");
		}

	}

	/**
	 * 把字串变为long
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static long getLong(String str) {
		return getLong(str, 0);
	}

	/**
	 * 把字串变为boolean
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBoolean(String str, boolean defaultValue) {
		if (isEmpty(str)) {
			return defaultValue;
		} else {
			if (isBoolean(str)) {
				return Boolean.parseBoolean(str);
			} else {
				return defaultValue;
			}

		}
	}

	/**
	 * 把字串变为boolean
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBoolean(String str) {
		return getBoolean(str, false);
	}

	/**
	 * 把字串变为int
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static int getInteger(String str, int defaultValue) {
		if (isEmpty(str)) {
			return defaultValue;
		} else {
			if (isNumber(str)) {
				return Integer.parseInt(str);
			} else {
				return defaultValue;
			}

		}
	}

	/**
	 * 把字串变为int
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static int getInteger(String str) {
		return getInteger(str, 0);
	}

	/**
	 * 把字串变为String
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static String getString(String str, String defaultValue) {
		if (isEmpty(str)) {
			return defaultValue;
		} else {
			return str;
		}
	}

	/**
	 * 把字串变为String
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static String getString(String str) {
		return getString(str, "");
	}

	/**
	 * 从文件全路径中取文名
	 * 
	 * @param str
	 * @return
	 * 
	 * @throws SystemException
	 */
	public static String getSimpleFileNameFromFullFileName(String fFileName)
			throws SystemException {
		if (isEmpty(fFileName)) {
			return "";
		}
		// log.info("--------------------"+fFileName);
		char cs[] = fFileName.toCharArray();
		int startId = 0;
		int retId = cs.length;
		for (int i = cs.length - 1; i >= 0; i--) {
			char c = cs[i];
			if (c == '.') {

				if (retId == cs.length) {
					retId = i;
				}
				// else {
				// ExceptionFactory.throwSystemException("参数${0}不是合法的文件路径",
				// new String[] { fFileName });
				// }
			} else if (c == '/') {

				if (startId == 0) {
					startId = i;
					break;
				}
			} else if (c == '\\') {
				if (startId == 0) {
					startId = i;
					break;
				}
			}

		}
		if (startId == 0) {
			return new String(cs, startId, retId - startId);
		} else {
			return new String(cs, startId + 1, retId - startId - 1);
		}

	}

	/**
	 * 从文件全路径中取文件后缀名
	 * 
	 * @param str
	 * @return
	 * 
	 * @throws SystemException
	 */
	public static String getFileSubNameFromFullFileName(String fFileName)
			throws SystemException {
		if (isEmpty(fFileName)) {
			return "";
		}
		char cs[] = fFileName.toCharArray();

		for (int i = cs.length - 1; i >= 0; i--) {
			char c = cs[i];
			if (c == '.') {
				return new String(cs, i + 1, cs.length - i - 1);
			} else if (c == '/') {
				return "";
			} else if (c == '\\') {
				return "";
			}

		}

		return "";
	}

	public static List<String> split(String line, char c) {
		List<String> list = new ArrayList<String>();
		if (isEmpty(line)) {
			return list;
		}
		if (line.indexOf(c) == -1) {
			list.add(line);
			return list;
		}
		char cs[] = line.toCharArray();

		int start = 0;

		for (int i = 0; i < cs.length; i++) {
			char tc = cs[i];
			if (c == tc) {
				list.add(new String(cs, start, i - start));
				start = i + 1;
			}
		}
		list.add(new String(cs, start, line.length() - start));
		return list;
	}

	public static String arrayToString(Object objs[]) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; objs != null && i < objs.length; i++) {
			Object obj = objs[i];
			if (ret.length() != 0) {
				ret.append(",");
			}
			ret.append(obj);
		}
		return ret.toString();
	}

	/**
	 * 取得URL的路径，去?号之后的字串，去;号之后的数据字串
	 * 
	 * @param curUrl
	 * @return
	 * @throws SystemException
	 */
	public static String getUrlForPath(String curUrl) {

		if (StringsUtils.isEmpty(curUrl)) {
			return "";
		}

		int id = curUrl.indexOf(';');
		if (id != -1) {
			curUrl = curUrl.substring(0, id);
		} else {
			id = curUrl.indexOf('?');
			if (id != -1) {
				curUrl = curUrl.substring(0, id);
			}
		}
		return curUrl;
	}

	/**
	 * 判断字串是否包含中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static Map<String, String> getParameterFromQueryString(
			String queryString) throws Exception {
		if (isEmpty(queryString)) {
			return new HashMap<String, String>();
		}
		if (queryString.startsWith("?")) {
			queryString = queryString.substring(1, queryString.length());
		}
		String items[] = queryString.split("&");

		Map<String, String> param = new HashMap<String, String>(items.length);
		for (String item : items) {
			String keyvalue[] = item.split("=");
			if (keyvalue.length == 2) {
				param.put(keyvalue[0], keyvalue[1]);
			}
		}
		return param;
	}

	/**
	 * 将日期转化为指定格式[yyyy-MM-dd]字符串
	 */
	public static String formatDate(Date date) {
		return formatDate(date, null);
	}

	/**
	 * 将日期转化为指定格式字符串
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (isEmpty(pattern)) {
			pattern = "yyyy-MM-dd";
		}
		return (new SimpleDateFormat(pattern)).format(date);
	}

	private static String inj_str = "'|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|; |or|-|+|,|--";

	/**
	 * 检查字串中是否包含SQL脚本,true=是SQL脚本，false=不是SQL脚本
	 * 
	 * @param date
	 * @return
	 */
	public static boolean validateSql(String str1) {
		if (isEmpty(str1)) {
			return false;
		}
		String str = str1.trim();
		str = str.toUpperCase();
		String[] inj_stra = inj_str.toUpperCase().split("\\|");
		for (int i = 0; i < inj_stra.length; i++) {
			if (str.indexOf(" " + inj_stra[i] + " ") >= 0
					|| str.indexOf(inj_stra[i] + " ") >= 0
					|| str.indexOf(" " + inj_stra[i]) >= 0
					|| str.indexOf(inj_stra[i] + ";") >= 0
					|| str.indexOf(";" + inj_stra[i]) >= 0
					|| str.indexOf(inj_stra[i] + "/**/") >= 0
					|| str.indexOf("/**/" + inj_stra[i]) >= 0
					|| str.indexOf(inj_stra[i] + "%") >= 0
					|| str.indexOf("%" + inj_stra[i]) >= 0) {

				return false;
			}
			if ("--".equals(inj_stra[i]) && str.indexOf(inj_stra[i]) >= 0) {

				return false;
			}
		}
		return true;
	}

	/**
	 * 将容易引起xss漏洞的半角字符直接替换成全角字符
	 * 
	 * @param s
	 * @return
	 */
	public static String doXssEncode(String s) {
		if (s == null || "".equals(s)) {
			return s;
		}
		StringBuilder sb = new StringBuilder(s.length() + 16);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '>':
				sb.append('＞');// 全角大于号
				break;
			case '<':
				sb.append('＜');// 全角小于号
				break;
			case '\'':
				sb.append('‘');// 全角单引号
				break;
			case '\"':
				sb.append('“');// 全角双引号
				break;
			case '&':
				sb.append('＆');// 全角
				break;
			case '\\':
				sb.append('＼');// 全角斜线
				break;
			case '#':
				sb.append('＃');// 全角井号
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	public static String getMsgByThrowable4Dialog(Throwable t) {
		return getMsgByThrowable(t,512);

	}
	protected static String getMsgByThrowable(Throwable t,int size) {
		if(t==null){
			return "";
		}
		if(size<256){
			size=256;
		}
		StringBuilder sb = new StringBuilder(size);
		String temp = t.toString();
		if (temp != null) {
			if (temp.length() > size) {
				return temp.substring(0, size);
			}
			sb.append(t.toString());
		}
		//sb.setLength(0);
		StackTraceElement[] trace = t.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			if(sb.length()>0){
				sb.append("\n");
			}
			sb.append("\tat " + trace[i]);
			if (sb.length() > size) {
				return sb.toString().substring(0, size);
			}
		}
		return sb.toString();

	}
}
