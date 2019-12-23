package com.gpdi.operatingunit.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * describe: Created by IntelliJ IDEA.
 * 
 * @author zzl
 * @version 2007-1-29
 */
@SuppressWarnings("unchecked")
public class StrUtils {

	private static Map<String, String>	transformCache	= new Hashtable<String, String>();

	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public static boolean notEmpty(String s) {
		return s != null && s.length() > 0;
	}

	public static Double toDouble(String s) {
		if (isEmpty(s)) {
			return null;
		}
		return Double.valueOf(s);
	}

	public static BigDecimal toBitDecimal(String s) {
		if (isEmpty(s)) {
			return null;
		}
		return BigDecimal.valueOf(Double.valueOf(s));
	}

	public static String valueOf(Double value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(value);
	}

	public static String valueOf(BigDecimal value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(value);
	}

	public static String quotedStr(String str) {
		if (str == null) {
			return "''";
		}
		return "'" + str.replace("'", "''") + "'";
	}

    public static <T> String join(List<T> strs, String sep) {
        String result = "";
        if (strs == null) {
			return result;
		}
        for (int n = 0; n < strs.size(); n++) {
            if (n > 0) {
				result += sep;
			}
            result += strs.get(n);
        }
        return result;
    }

    public static <T> String join(List<T> items, char sep) {
        if (items == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < items.size(); n++) {
            if (n > 0) {
                sb.append(sep);
            }
            sb.append(items.get(n));
        }
        return sb.toString();
    }

    public static Integer toInteger(String s) {
		if (isEmpty(s)) {
			return null;
		}
		return Integer.valueOf(s);
	}

	public static Integer toInteger(String s, int defValue) {
		try {
			return Integer.valueOf(s);
		} catch (Throwable e) {
			return defValue;
		}
	}

	public static String getCommonPrefix(String s1, String s2) {
		if (s1 == null || s2 == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < Math.min(s1.length(), s2.length()); n++) {
			char c = s1.charAt(n);
			if (c != s2.charAt(n)) {
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public static Long toLong(String s) {
		if (isEmpty(s)) {
			return null;
		}
		return Long.valueOf(s);
	}

	public static String valueOf(Integer value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(value);
	}

	public static String valueOf(Object o) {
		if (o == null) {
			return "";
		}
		return "" + o;
	}

	public static String left(String s, int cCount) {
		return s.substring(0, Math.min(s.length(), cCount));
	}

	public static boolean isAllNumber(String s) {
		return s.matches("\\d+");
	}

	public static int getByteLen(Object oText) {
		if (oText == null) {
			return 0;
		}
		String text = oText.toString();
		try {
			byte[] bts = text.getBytes("gbk");
			return bts.length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String ellipseText(Object oText, int maxBytes) {
		if (oText == null) {
			return "";
		}
		return ellipseText(oText.toString(), maxBytes);
	}

	public static String ellipseText(String text, int maxBytes) {
		if (maxBytes >= text.length() * 2) {
			return text;
		}
		String subText = text.substring(0, Math.min(maxBytes, text.length()));
		try {
			byte[] bts = subText.getBytes("gbk");
			if (maxBytes < bts.length) {
				String s = new String(bts, 0, maxBytes, "gbk");
				String s1 = new String(bts, 0, maxBytes + 1, "gbk");
				if (s1.length() == s.length()) {
					s = new String(bts, 0, maxBytes - 1);
				}
				return s + "...";
			}
		} catch (Exception e) {
			// never fail, do nothing
		}
		if (subText.length() < text.length()) {
			subText += "...";
		}
		return subText;
	}

	public static String emptyIsZero(String s) {
		return s == null ? "0" : (s.length() == 0 ? "0" : s);
	}

	public static String anyLike(String s) {
//		return "%" + s.replace("_", "[_]").replace("%", "[%]") + "%";
		return "%" + s.replace("_", "\\_").replace("%", "\\%") + "%"; //mysql
	}

	public static String rightLike(String s) {
		return s + "%";
	}

	public static String anyLikeIfNotEmpty(String s) {
		if (isEmpty(s)) {
			return s;
		}
		return anyLike(s);
	}

	public static boolean equalsIgnoreNull(String s1, String s2) {
		if (s1 == null) {
			s1 = "";
		}
		if (s2 == null) {
			s2 = "";
		}
		return s1.equals(s2);
	}

	/**
	 * 判断字符串是否为NULL或等于某个特定值，在检测下拉框值时十分好用。
	 * 空时返回true
	 * 
	 * @param str
	 *            字符串
	 * @param empty
	 *            特定值
	 * @return true/false
	 */
	public static boolean CheckString(String str, String empty) {
		if (str == null || str.equals(empty)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 给字符串前补零
	 * 
	 * @param s
	 *            字符串
	 * @param len
	 *            位数
	 * @return 补零后的字符串
	 */
	public static String appendPreZero(String s, int len) {
		int strLen = 0;
		if (s != null) {
			strLen = s.length();
			if (strLen > 0 && strLen < len) {
				for (int i = strLen; i < len; i++) {
					s = "0" + s;
				}
			}
		}
		return s;
	}

	public static String trimEnd(String textValue, char... charsToTrim) {
		char[] chars = charsToTrim;
		if (chars.length == 0) {
			chars = new char[] { '\r', '\n', '\t', ' ' };
		}
		int len = -1;
		while (len != textValue.length() && (len = textValue.length()) > 0) {
			for (int n = 0; n < chars.length; n++) {
				if (textValue.charAt(len - 1) == chars[n]) {
					textValue = textValue.substring(0, len - 1);
					break;
				}
			}
		}
		return textValue;
	}

	public static String lstrip(String s, char c) {
		if (s == null || s.length() == 0) {
			return s;
		}
		if (s.charAt(0) == c) {
			return s.substring(1);
		}
		return s;
	}

	public static String unCamelize(String s) {
		int firstPos = -1;
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);
			if (Character.isUpperCase(c)) {
				firstPos = n;
				break;
			}
		}
		if (firstPos == -1) {
			return s;
		}
		String result = transformCache.get(s);
		if (result != null) {
			return result;
		}
		StringBuilder sb = new StringBuilder(s.substring(0, firstPos));
		for (int n = firstPos; n < s.length(); n++) {
			char c = s.charAt(n);
			if (Character.isUpperCase(c)) {
				if(n > 0) {
					sb.append('_');
				}
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		result = sb.toString();
		transformCache.put(s, result);
		return result;
	}

	public static String camelize(String s, boolean firstUpper) {
		int firstPos = s.indexOf('_');
		if (firstPos == -1) {
			if (firstUpper) {
				s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
			}
			return s;
		}
		String result = transformCache.get(s);
		if (result != null) {
			return result;
		}
		StringBuilder sb = new StringBuilder(s.substring(0, firstPos));
		if (firstUpper) {
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		}
		boolean needUpcase = true;
		for (int n = firstPos + 1; n < s.length(); n++) {
			char c = s.charAt(n);
			if (c == '_') {
				needUpcase = true;
			} else if (needUpcase) {
				sb.append(Character.toUpperCase(c));
				needUpcase = false;
			} else {
				sb.append(c);
			}
		}
		result = sb.toString();
		transformCache.put(s, result);
		return result;
	}

	public static <T> String joinIgnoreNull(String sep, T... arr) {
		StringBuilder sb = new StringBuilder();
		int nCount = arr.length;
		for (int n = 0; n < nCount; n++) {
			if (arr[n] == null) {
				continue;
			}
			if (sb.length() > 0) {
				sb.append(sep);
			}
			sb.append(arr[n]);
		}
		return sb.toString();
	}

    public static List<String> splitToList(String s, char sep) {
        if (s == null || s.length() == 0) {
            return new ArrayList<>();
        }
        List<String> parts = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < s.length(); n++) {
            char c = s.charAt(n);
            if (c == sep) {
                parts.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        parts.add(sb.toString());

        return parts;
    }

    public static String[] split(String s, char sep) {
		if (s == null || s.length() == 0) {
			return new String[0];
		}
		List<String> parts = new ArrayList<String>();

		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);
			if (c == sep) {
				parts.add(sb.toString());
				sb.setLength(0);
			} else {
				sb.append(c);
			}
		}
		parts.add(sb.toString());

		return parts.toArray(new String[parts.size()]);
	}

	/**
	 * 返回新编码,参数例子：“AB009”,"AB00901",2或者：“AB009”,NULL,2
	 * 
	 * @param preCode
	 * @param maxCode
	 * @param length
	 * @return
	 */
	public static String getNewCode(String preCode, String maxCode, int length) {

		String sufStr = "";
		String code = "";
		if (StrUtils.isEmpty(maxCode)) {
			maxCode = "";
		}
		String sufNum = maxCode.replaceAll(preCode, "");
		if(!StrUtils.isEmpty(sufNum) && !StrUtils.isAllNumber(sufNum))
		{
			return null;
		}
		if (!StrUtils.isEmpty(maxCode)) {
			int numlen = String.valueOf(Integer.valueOf(sufNum)).length();
			for (int i = 0; i < length - numlen; i++) {
				sufStr += "0";
			}
			code = preCode + sufStr
					+ String.valueOf(Integer.valueOf(sufNum) + 1);
		} else {
			for (int i = 0; i < length - 1; i++) {
				sufStr += "0";
			}
			code = preCode + sufStr + 1;
		}
		return code;
	}

	public static void main(String[] args) {
		List<String> sss = new ArrayList<String>();
		StrUtils.join(sss, "X");
		String code = getNewCode("AB009", "MATE", 2);
	}

	public static String prefixLike(String s) {
		return s + '%';
	}

    public static String format(Double value) {
        return format(value, 2);
    }

	public static String format(Double value, int frationDigits) {
	    if(value == null) {
	        return "";
        }
        DecimalFormat format = new DecimalFormat();
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(frationDigits);
        return format.format(value);
	}

	public static String unCapitalize(String str) {
		if(str == null || str.length() == 0) {
			return str;
		}
        char c0 = str.charAt(0);
        if(!Character.isUpperCase(c0)) {
            return str;
        }
        return Character.toLowerCase(c0) + str.substring(1);
	}

	public static String getRandomNumStr(int len){
		String letters = "0123456789";
		StringBuilder str = new StringBuilder();
		for (int i=0;i<len;i++){
			str.append(letters.charAt(i));
		}
		return str.toString();
	}

	/**
	 * 获取excel列名称，用列下标获取
	 */
	public static List<String> getExcelColNames(){
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		List<String> arr = new ArrayList<>();
		for (int i = 0; i < str.length(); i++) {
			arr.add(String.valueOf(str.charAt(i)));
		}
		for (int i = 0; i < str.length(); i++) {
			String s = String.valueOf(str.charAt(i));
			for (int j = 0; j < str.length(); j++) {
				arr.add(s + String.valueOf(str.charAt(j)));
			}
		}
		return arr;
	}
}
