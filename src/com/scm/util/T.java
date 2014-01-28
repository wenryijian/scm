package com.scm.util;

import java.io.BufferedReader;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class T {
    // HOUR SECOND
    public static final long HOUR = 3600L * 1000L;

    // DAY SECOND
    public static final long DAY = 24L * HOUR;

    public T() {
    }

    public static boolean isEmpty(String str) {
	return (str == null || str.length() == 0);
    }

    public static boolean isBlank(String str) {
	return (str == null || str.trim().length() == 0);
    }

    public static boolean isNumeric(String str) {
	if (str == null || str.trim().length() == 0) {
	    return false;
	}
	int offset = 0;
	if (str.startsWith("-") || str.startsWith("+")) {
	    if (str.length() > 1) {
		offset = 1;
	    } else {
		return false;
	    }
	}
	for (int i = offset; i < str.length(); i++) {
	    if (!Character.isDigit(str.charAt(i))) {
		return false;
	    }
	}
	return true;
    }

    public static String toUTF8(String arg) {
	byte b[] = arg.getBytes();
	char c[] = new char[b.length];
	for (int x = 0; x < b.length; x++)
	    c[x] = (char) (b[x] & 0xff);
	return new String(c);
    }

    /** ************************ȡĬ��valueֵ�Ŀ�ʼ********************************** */
    public static String stringValue(String v, String def) {
	if (v == null || v.length() == 0)
	    return def;
	return v.trim();
    }

    public static String[] stringArrayValue(String[] v, String[] def) {
	if (v == null || v.length == 0)
	    return def;
	return v;
    }

    public static byte byteValue(String v, byte def) {
	if (v == null || v.length() == 0)
	    return def;
	try {
	    return Byte.parseByte(v);
	} catch (Exception e) {
	    return def;
	}
    }

    public static char charValue(String v, char def) {
	if (v == null || v.length() == 0)
	    return def;
	try {
	    return (char) Integer.parseInt(v);
	} catch (Exception e) {
	    return def;
	}
    }

    public static int intValue(String v, int def) {
	if (v == null || v.length() == 0)
	    return def;
	try {
	    return Integer.parseInt(v.trim());
	} catch (Exception e) {
	    return def;
	}
    }

    public static Integer integerValue(String v) {
	return integerValue(v, null);
    }

    public static Integer integerValue(String v, int def) {
	if (isBlank(v))
	    return new Integer(def);
	try {
	    return Integer.valueOf(v);
	} catch (Exception e) {
	    return new Integer(def);
	}
    }

    public static Integer integerValue(String v, Integer def) {
	if (isBlank(v))
	    return def;
	try {
	    return Integer.valueOf(v);
	} catch (Exception e) {
	    return def;
	}
    }

    public static long longValue(String v, long def) {
	if (v == null || v.length() == 0)
	    return def;
	try {
	    return Long.parseLong(v.trim());
	} catch (Exception e) {
	    return def;
	}
    }

    public static boolean booleanValue(String v, boolean def) {
	if (v == null || v.length() == 0)
	    return def;

	if (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("yes")
		|| v.equalsIgnoreCase("1")) {
	    return true;
	} else if (v.equalsIgnoreCase("false") || v.equalsIgnoreCase("no")
		|| v.equalsIgnoreCase("0")) {
	    return false;
	} else {
	    return def;
	}
    }

    public static float floatValue(String v, float def) {
	if (v == null || v.length() == 0)
	    return def;
	try {
	    return Float.parseFloat(v.trim());
	} catch (Exception e) {
	    return def;
	}
    }

    public static float floatValue(String v, int remain, float def) {
	try {
	    BigDecimal bd = new BigDecimal(v);
	    bd = bd.setScale(remain, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	} catch (Exception e) {
	    return def;
	}
    }

    public static double doubleValue(String v, double def) {
	if (v == null || v.length() == 0)
	    return def;
	try {
	    return Double.parseDouble(v.trim());
	} catch (Exception e) {
	    return def;
	}
    }

    public static Date dateValue(String v, String fm, Date def) {
	if (v == null || v.length() == 0)
	    return def;
	try {
	    return new SimpleDateFormat(fm).parse(v.trim());
	} catch (Exception e) {
	    e.printStackTrace();
	    return def;
	}
    }

    public static Date dateValue(String v, Date def) {
	return dateValue(v, "yyyy-MM-dd", def);
    }

    public static Date datetimeValue(String v, Date def) {
	return dateValue(v, "yyyy-MM-dd HH:mm:ss", def);
    }

    public static long periodValue(String value, long deflt) {
	if (value == null)
	    return deflt;

	long period = 0;
	long sign = 1;// ������ʶ
	int i = 0;// ��ʵλ��
	int length = value.length();

	if (length > 0 && value.charAt(i) == '-') {
	    sign = -1;
	    i++;
	}

	while (i < length) {
	    long delta = 0;
	    char ch;

	    for (; i < length && (ch = value.charAt(i)) >= '0' && ch <= '9'; i++) {
		delta = 10 * delta + ch - '0';
	    }

	    if (i >= length) {
		period += 1000 * delta;// ������
	    } else {
		switch (value.charAt(i++)) {
		case 's':
		case 'S':
		    period += 1000 * delta;
		    break;
		case 'm':
		    period += 60 * 1000 * delta;
		    break;
		case 'h':
		case 'H':
		    period += 60L * 60 * 1000 * delta;
		    break;
		case 'd':
		case 'D':
		    period += DAY * delta;
		    break;
		case 'w':
		case 'W':
		    period += 7L * DAY * delta;
		    break;
		default:
		    return deflt;
		}
	    }
	}
	return sign * period;
    }

    /** ************************ȡĬ��valueֵ�Ľ���********************************** */

    public static Date getNow() {
	return new Date(System.currentTimeMillis());
    }

    // �õ������賿��ʱ��.
    public static Date getToday() {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	return cal.getTime();
    }

    // �õ�����23��59 ʱ��
    public static Date getTodayLast() {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.HOUR_OF_DAY, 23);
	cal.set(Calendar.MINUTE, 59);
	cal.set(Calendar.SECOND, 59);
	cal.set(Calendar.MILLISECOND, 999);
	return cal.getTime();
    }

    // �õ�ĳһ����賿ʱ��
    public static Date getToday(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	return cal.getTime();
    }

    // �õ�ĳһ���23��59 ʱ��
    public static Date getTodayLast(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.HOUR_OF_DAY, 23);
	cal.set(Calendar.MINUTE, 59);
	cal.set(Calendar.SECOND, 59);
	cal.set(Calendar.MILLISECOND, 999);
	return cal.getTime();
    }

    // �õ�����ǰ����ʱ��===================================================================
    public static Date getSomeDate(Date date, int dayNum) {
	Calendar cal = Calendar.getInstance();
	long DAY = 1000 * 3600 * 24;
	cal.setTimeInMillis(date.getTime() + DAY * (long) dayNum);
	return cal.getTime();
    }

    // �õ�����ǰ����ʱ��===================================================================
    public static Date getSomeMonthDate(Date date, int monthNum) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + monthNum);
	return cal.getTime();
    }

    // �õ�24Сʱ��ĳСʱ�Ŀ�ʼʱ��
    public static Date getSubsectionHourBegin(Date date, int sub) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.HOUR_OF_DAY, sub);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	return cal.getTime();
    }

    // �õ�24Сʱ��ĳСʱ��ĩβʱ��
    public static Date getSubsectionHourEnd(Date date, int sub) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.HOUR_OF_DAY, sub);
	cal.set(Calendar.MINUTE, 59);
	cal.set(Calendar.SECOND, 59);
	cal.set(Calendar.MILLISECOND, 999);
	return cal.getTime();
    }

    public static Date getYesterday() {
	return getYesterday(T.getToday());
    }

    public static Date getYesterday(Date date) {
	long t = date.getTime();
	Date result = new Date(t - 24 * 60 * 60 * 1000l);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	try {
	    date = sdf.parse(sdf.format(date));
	} catch (ParseException e) {
	}
	return result;
    }

    public static Date getTheDay(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	return cal.getTime();
    }

    public static int getYear(Date date) {
	Calendar cld = Calendar.getInstance();
	cld.setTime(date);
	return cld.get(Calendar.YEAR);
    }

    // ���ص������� ��Ȼ��-1 Ҳ����˵���ص����Ǵ� 0--11
    public static int getMonth(Date date) {
	Calendar cld = Calendar.getInstance();
	cld.setTime(date);
	return cld.get(Calendar.MONTH);
    }

    public static int getDate(Date date) {
	Calendar cld = Calendar.getInstance();
	cld.setTime(date);
	return cld.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour(Date date) {
	Calendar cld = Calendar.getInstance();
	cld.setTime(date);
	return cld.get(Calendar.HOUR_OF_DAY);
    }

    public static String format(Date date, String fmt) {
	if (date == null)
	    return "";
	DateFormat formatter = new SimpleDateFormat(fmt);
	return formatter.format(date);
    }

    public static String format(Date date) {
	return format(date, "yyyy-MM-dd");
    }

    public static String format() {
	return format(new Date(System.currentTimeMillis()), "yyyy-MM-dd");
    }

    // �õ�������ڿ�ʼ��ʱ��,���ڵĿ�ʼ��getFirstDayOfWeek()�õ�
    public static Date getThisWeekStart() {
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DATE, -(cal.get(Calendar.DAY_OF_WEEK) - 1));
	return getTheDay(cal.getTime());
    }

    // ���µĿ�ʼ
    public static Date getThisMonthStart() {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.DAY_OF_MONTH, 1);
	return getTheDay(cal.getTime());
    }

    // ���µĽ���
    public static Date getThisMonthEnd() {
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.MONTH, 1);
	cal.set(Calendar.DAY_OF_MONTH, 1);
	cal.add(Calendar.DATE, -1);
	cal.set(Calendar.HOUR_OF_DAY, 23);
	cal.set(Calendar.MINUTE, 59);
	cal.set(Calendar.SECOND, 59);
	cal.set(Calendar.MILLISECOND, 999);
	return cal.getTime();
    }

    // ���µĿ�ʼ
    public static Date getMonthStart(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.DAY_OF_MONTH, 1);
	return getTheDay(cal.getTime());
    }

    public static Date getTheMonthStart(int amount) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(getThisMonthStart());
	cal.add(Calendar.MONTH, amount);
	return cal.getTime();
    }

    // n��ǰ��� + -
    public static Date addDay(Date date, int day) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(Calendar.DATE, day);
	return new Date(cal.getTime().getTime());
    }

    // n��ǰ��� + -
    public static java.util.Date addMonth(java.util.Date date, int month) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(Calendar.MONTH, month);
	return new java.util.Date(cal.getTime().getTime());
    }

    // n��ǰ��� + -
    public static java.util.Date addYear(java.util.Date date, int year) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(Calendar.YEAR, year);
	return new java.util.Date(cal.getTime().getTime());
    }

    // ������������֮�������
    public static int getDays(Date date1, Date date2) {
	int days = 0;
	days = (int) (Math.abs((date2.getTime() - date1.getTime())) / (24 * 60 * 60 * 1000));
	return days;
    }

    public static int getDays(String date1, String date2) {
	int days = 0;
	days = (int) (Math.abs((dateValue(date1, "yyyy-Mm-dd", new Date())
		.getTime() - dateValue(date2, "yyyy-Mm-dd", new Date())
		.getTime())) / (24 * 60 * 60 * 1000));
	return days;
    }

    // ������������֮���ʱ��� ��ϸ���� ��������ΪString
    public static String getDayDif(Date date1, Date date2) {
	long DAY = 24 * 60 * 60 * 1000;
	long between = Math.abs((date2.getTime() - date1.getTime()));
	long day = between / DAY;
	long hour = (between / (60 * 60 * 1000) - day * 24);
	long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
	long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
	return "" + day + "��" + hour + "Сʱ" + min + "��" + s + "��";
    }

    // ʱ���ʽΪyyyy-MM-dd HH:mm:ss
    public static String getDayDif(String date1, String date2) {
	long DAY = 24 * 60 * 60 * 1000;
	long between = Math
		.abs(dateValue(date1, "yyyy-MM-dd HH:mm:ss", new Date())
			.getTime()
			- dateValue(date2, "yyyy-MM-dd HH:mm:ss", new Date())
				.getTime());
	long day = between / DAY;
	long hour = (between / (60 * 60 * 1000) - day * 24);
	long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
	long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
	return "" + day + "��" + hour + "Сʱ" + min + "��" + s + "��";
    }

    public static URL getClassURL(Class clasz) {
	String strName = clasz.getName();
	strName = strName.replace('.', '/');
	strName = "/" + strName + ".class";
	return clasz.getResource(strName);
    }

    static public boolean empty(String e) {
	return e == null || e.length() == 0;
    }

    static public String splitString(String[] str) {
	String result = "";
	if (str == null || str.length == 0) {
	    return result;
	}
	for (int i = 0; i < str.length; i++) {
	    result += str[i];
	    if (i != (str.length - 1)) {
		result += ",";
	    }
	}
	return result;
    }

    static public List string2List(String str, String split) {
	List list = null;
	String temp[] = string2Array(str, split);
	if (temp == null)
	    return list;
	list = Arrays.asList(temp);
	return list;
    }

    static public String[] string2Array(String str, String split) {
	List list = new ArrayList();
	if (str == null || str.equals("")) {
	    return null;
	}
	String temp[] = str.split(split);
	if (split.equals("")) {
	    String result[] = new String[temp.length - 1];
	    for (int i = 0; i < result.length; i++) {
		result[i] = temp[i + 1];
	    }
	    return result;
	} else {
	    return temp;
	}
    }

    static public String array2String(String str[], String split) {
	if (str == null) {
	    return "";
	}
	String result = "";
	for (int i = 0; i < str.length; i++) {
	    if (!result.equals("")) {
		result += split;
	    }
	    result += str[i];
	}

	return result;
    }

    static public String list2String(List list, String split) {
	if (list == null) {
	    return "";
	}
	String result = "";
	for (int i = 0; i < list.size(); i++) {
	    if (!result.equals("")) {
		result += split;
	    }
	    result += (String) list.get(i);
	}
	return result;
    }

    public static String replaceBlank(String str) {
	Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	Matcher m = p.matcher(str);
	String after = m.replaceAll("");
	return after;
    }

    // ȥ��html��ǩ
    public static String trimHtmlTag(String strValue) {
	if (strValue == null || strValue.equals(""))
	    return "";
	// ȥ��img��ǩ�������">"����
	strValue = strValue.replaceAll("width>screen", "");
	Pattern p = null;
	Matcher m = null;
	String pattern = "<[^<>]*>?";
	p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	m = p.matcher(strValue);
	strValue = m.replaceAll("");

	return strValue;
    }

    // ��ȡ���ĳ���
    public static String cutString(String value, int length, String suffix) {
	String strValue = value;
	boolean cut = false;
	int lengthByte = length * 2;
	if (value == null || value.equals(""))
	    return "";
	try {
	    if (strValue.length() < length) {
		strValue = toHTML(strValue);
		return strValue;
	    }
	    if (strValue.length() > lengthByte) {
		strValue = strValue.substring(0, lengthByte);
		cut = true;
	    }

	    byte[] bytes = strValue.getBytes("GBK");
	    if (bytes.length <= lengthByte) {
	    } else {
		cut = true;
		byte[] newBytes = new byte[lengthByte];
		System.arraycopy(bytes, 0, newBytes, 0, lengthByte);
		strValue = new String(newBytes, "GBK");
		if (strValue.charAt(strValue.length() - 1) == (char) 65533) {
		    strValue = strValue.substring(0, strValue.length() - 1);
		}
	    }
	    // System.out.println("before:" + strValue);
	    strValue = toHTML(strValue);
	    // System.out.println("after:" + strValue);

	} catch (Exception ex) {
	    System.out.println("��ȡ�ַ�������" + ex);
	    return "";
	}
	if (!cut)
	    suffix = "";
	return strValue + suffix;
    }

    /**
     * @param string
     * @return
     */
    // ���زõ���html��ǩ,���ڽ�ȡ��־
    public static String reFillHtml(String string) {
	if (string == null || string.trim().equals("")) {
	    return string;
	}
	// System.out.println("s----:"+string);
	// ȥ��img���Ե�>��
	string = string.replaceAll("width>screen", "width#_gt_#screen");
	// ȥ�����Ĳ������ı�ǩ
	string = string.replaceAll("<[^<>]*$", "");
	// �滻���е���ǩ
	string = string.replaceAll("<([^<>]*)/>", "#_lt_#$1/#_gt_#");
	string = string.replaceAll("<br>", "#_lt_#br#_gt_#");
	// �滻���гɶԱ�ǩ
	while (true) {
	    String temp = string;
	    // System.out.println("---t--:"+temp);
	    string = string.replaceAll("<([^/][^<>]*)>([^<>]*)</([^<>]*)>",
		    "#_lt_#$1#_gt_#$2#_lt_#/$3#_gt_#");
	    // System.out.println("---s--:"+string);
	    if (temp.equals(string)) {
		// �����쳣���������Ľ�����ǩ
		string = string.replaceAll("</[^<>]*>", "");
		break;
	    }
	}
	// ���ز������ı�ǩ
	while (true) {
	    String temp = string;
	    string = string.replaceAll("<([^/][^\\s<>]*)([^<>]*)>([^<>]*)$",
		    "#_lt_#$1$2#_gt_#$3#_lt_#/$1#_gt_#");
	    // System.out.println("---2--:"+string);
	    if (temp.equals(string)) {
		// �����쳣���������Ŀ�ʼ��ǩ
		string = string.replaceAll("<[^<>]*>", "");
		break;
	    }
	}
	string = string.replaceAll("#_lt_#", "<").replaceAll("#_gt_#", ">");
	try {
	    // System.out.println("bytes----:"+string.getBytes("GBK").length);
	    while (string.getBytes("GBK").length >= 2000) {
		// System.out.println(string.getBytes("GBK").length+" >= 2000 ");
		string = string.replaceFirst(
			"<([^/][^<>]*)>([^<>]*)</([^<>]*)>", "");
	    }
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	// System.out.println("e----:"+string);
	return string;
    }

    public static String encode(String str) {
	if (str == null || str.equals(""))
	    return "";
	try {
	    str = java.net.URLEncoder.encode(str, "GBK");

	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	return str;
    }

    // �����ַ����ĳ��ȣ�һ��������2
    public static int getLength(String str) {
	if (empty(str)) {
	    return 0;
	}
	int reInt = 0;
	char[] chars = str.toCharArray();
	for (int i = 0; i < chars.length; i++) {
	    reInt += String.valueOf(chars[i]).getBytes().length;
	}
	return reInt;
    }

    /** ��URLת��Ϊ����ƴ���ύ�����е��ַ��� */
    public static String encodeURL(String s) throws Exception {
	return s == null ? null : java.net.URLEncoder.encode(s, "GBK");
    }

    /**
     * ���ַ�����ȡָ���ֽ������ȡ� ��Ϊjava�������ַ���ʹ��unicdoe��һ���ַ�ռ�����ֽڡ�
     * ����ҳ����ʾ��ʱ�������ַ�ռ�Ŀ����Ӣ�ĵ�����������Ϊ�����룬 ��Ҫ�����ֽڼ�����ַ�����ȡ��
     * �����Ҫ��ȡ�ĳ��ȣ��պð�һ�������ַ��п��ˣ���ô���ؽ���ͼ���һ���ַ�
     * 
     * @param str
     *            ��Ҫ������ַ���
     * @param cutLen
     *            ��Ҫ��ȡ���ֽ���
     * @return ������ȡ���ַ���
     */
    public static String byteSubstring(String str, int cutLen) {
	return byteSubstring(str, cutLen, null);
    }

    /**
     * �����ֽ�����ȡ�ַ�������ο�byteSubstring(String, int) ��ͬ���ǣ������ȡ�õ����ַ�������С��ԭ�ַ����������ʡ�Է���
     * �������ʡ�Է�����null���߿մ���Ҳֱ�ӷ��ؽ�ȡ����ַ���
     * 
     * @param str
     *            ��Ҫ������ַ���
     * @param cutLen
     *            ��Ҫ��ȡ�ĳ��ȣ��ֽ�����
     * @param suspensionSymbol
     *            ʡ�Է���
     * @return ������ȡ���ַ���
     */
    public static String byteSubstring(String str, int cutLen,
	    String suspensionSymbol) {
	if (empty(str))
	    return str;
	if (cutLen <= 0)
	    return "";
	int reInt = 0;
	if (!empty(suspensionSymbol)) {
	    char[] sArray = suspensionSymbol.toCharArray();
	    int suspensionSymbolLength = 0;
	    for (int i = 0; i < sArray.length; i++) {
		suspensionSymbolLength += String.valueOf(sArray[i]).getBytes().length;
	    }
	    reInt += suspensionSymbolLength;
	}
	String reStr = "";
	char[] chars = str.toCharArray();
	for (int i = 0; (i < chars.length && cutLen > reInt); i++) {
	    reInt += String.valueOf(chars[i]).getBytes().length;
	    reStr += chars[i];
	}
	reStr = reInt > cutLen ? reStr.substring(0, reStr.length() - 1) : reStr;
	return !empty(suspensionSymbol) && str.length() > reStr.length() ? reStr
		+ suspensionSymbol
		: reStr;
    }

    /**
     * �����ֽ�����ȡ�ַ�������ο�byteSubstring(String, int) ��ͬ���ǣ������ȡ�õ����ַ�������С��ԭ�ַ����������ʡ�Է���
     * �������ʡ�Է�����null���߿մ���Ҳֱ�ӷ��ؽ�ȡ����ַ���
     * 
     * @param str
     *            ��Ҫ������ַ���
     * @param cutLen
     *            ��Ҫ��ȡ�ĳ��ȣ��ֽ�����
     * @param suspensionSymbol
     *            ʡ�Է���
     * @param isFilterUBB
     *            �Ƿ���˵�UBB����
     * @return ������ȡ���ַ���
     */
    public static String byteSubstring(String str, int cutLen,
	    String suspensionSymbol, boolean isFilterUBB) {
	if (isFilterUBB) {
	    String s = byteSubstring(str, cutLen, suspensionSymbol);
	    s = simpleStringFilter(s, "img");
	    s = simpleStringFilter(s, "url");
	    s = simpleStringFilter(s, "email");
	    s = simpleStringFilter(s, "FLASH");
	    s = simpleStringFilter(s, "RM");
	    s = simpleStringFilter(s, "code");
	    s = simpleStringFilter(s, "quote");
	    s = simpleStringFilter(s, "color");
	    s = simpleStringFilter(s, "fly");
	    s = simpleStringFilter(s, "move");
	    s = simpleStringFilter(s, "glow");
	    s = simpleStringFilter(s, "shadow");
	    s = simpleStringFilter(s, "b");
	    s = simpleStringFilter(s, "i");
	    s = simpleStringFilter(s, "u");
	    s = simpleStringFilter(s, "center");
	    s = simpleStringFilter(s, "size");
	    s = simpleStringFilter(s, "face");
	    s = simpleStringFilter(s, "align");
	    s = simpleStringFilter(s, "em");
	    s = simpleStringFilter(s, "font");
	    s = simpleStringFilter(s, "WMA");
	    s = simpleStringFilter(s, "WMV");
	    return s;
	} else {
	    return byteSubstring(str, cutLen, suspensionSymbol);
	}
    }

    /**
     * ���˵�ĳ��UBB��tag
     * 
     * @param string
     *            ��Ҫ������ַ���
     * @param filter
     *            ��Ҫ���˵�UBB��tag
     * @return ����������ַ���
     */
    public static String simpleStringFilter(String string, String filter) {
	if (string == null || string.trim().equals("")) {
	    return string;
	}

	Pattern p = null;
	Matcher m = null;
	Pattern p1 = null;
	Matcher m1 = null;
	String pStr1 = "\\[unknown\\]";
	String pStr2 = "\\[/unknown\\]";
	if (filter.equalsIgnoreCase("url")) {
	    pStr1 = "\\[url=[^\\[\\]]*\\]";
	    pStr2 = "\\[/url\\]";
	} else if (filter.equalsIgnoreCase("email")) {
	    pStr1 = "\\[email=[^\\[\\]]*\\]";
	    pStr2 = "\\[/email\\]";
	} else if (filter.equalsIgnoreCase("color")) {
	    pStr1 = "\\[\\s*color\\s*=\\s*(#?[a-z0-9].*?)\\]";
	    pStr2 = "\\[\\s*/color\\s*\\]";
	} else if (filter.equalsIgnoreCase("glow")) {
	    pStr1 = "\\[glow=[^\\[\\]]*\\]";
	    pStr2 = "\\[/glow\\]";
	} else if (filter.equalsIgnoreCase("shadow")) {
	    pStr1 = "\\[shadow=[^\\[\\]]*\\]";
	    pStr2 = "\\[/shadow\\]";
	} else if (filter.equalsIgnoreCase("size")) {
	    pStr1 = "\\[size=[^\\[\\]]*\\]";
	    pStr2 = "\\[/size\\]";
	} else if (filter.equalsIgnoreCase("face")) {
	    pStr1 = "\\[face=[^\\[\\]]*\\]";
	    pStr2 = "\\[/face\\]";
	} else if (filter.equalsIgnoreCase("align")) {
	    pStr1 = "\\[align=[^\\[\\]]*\\]";
	    pStr2 = "\\[/align\\]";
	} else if (filter.equalsIgnoreCase("font")) {
	    pStr1 = "\\[font=[^\\[\\]]*\\]";
	    pStr2 = "\\[/font\\]";
	} else if (filter.equalsIgnoreCase("em")) {
	    pStr1 = "\\[em([0-9]*)\\]";

	} else {
	    pStr1 = pStr1.replaceAll("unknown", filter);
	    pStr2 = pStr2.replaceAll("unknown", filter);
	}

	int start = 0;
	int end = 0;

	if (!filter.equals("em")) {
	    p = Pattern.compile(pStr1, Pattern.CASE_INSENSITIVE);
	    m = p.matcher(string);
	    p1 = Pattern.compile(pStr2, Pattern.CASE_INSENSITIVE);
	    m1 = p1.matcher(string);
	    while (m.find()) {
		start = m.start();
		end = m.end();
		if (end < string.length()) {
		    if (!m1.find(end)) {
			string = string.substring(0, start)
				+ string.substring(end, string.length());
		    }
		} else {
		    if (start < string.length()) {
			string = string.substring(0, start);
		    }
		}
	    }
	}

	int left = string.lastIndexOf("[");
	int right = string.lastIndexOf("]");
	if (left >= 0) {
	    if (left > right) {
		string = string.substring(0, left);
	    }
	}
	return string;
    }

    public static boolean regulaxDomain(String domain) {
	return Pattern.matches("[a-z_0-9]{2,20}", domain);
    }

    public boolean isShow(String StringSet, int index) {
	String[] sets = string2Array(StringSet, "");
	return sets[index].equals("1") ? true : false;
    }

    /** ȡ��һ���������ĵ��ַ������ֽڳ��� */
    public static int getByteLength(String s) {
	return s == null ? 0 : s.replaceAll("[^\\x00-\\xff]", "12").length();
    }

    // ����logo��ȡ����ͼ
    public static String getLogoThumb(String logo, int size) {
	if (isEmpty(logo))
	    return "";
	Pattern jsPattern = Pattern.compile("(.*)\\.([^.]*)");
	Matcher matcher = jsPattern.matcher(logo);
	return matcher.replaceFirst("$1_thu" + size + ".$2");
    }

    public static String calTimeInterval(Date createTime) {
	String str = "1����ǰ";
	Date now = T.getNow();
	long interval = now.getTime() - createTime.getTime();
	long min = interval / (60 * 1000);
	if (min >= 1 && min < 60) {// ����1���ӣ�С��1Сʱ
	    str = min + "����ǰ";
	} else if (min >= 60 && min <= 24 * 60) {// ����1Сʱ��С��1��
	    long hour = min / 60;
	    str = hour + "Сʱ"
		    + ((min - hour * 60) > 0 ? (min - hour * 60) + "����ǰ" : "ǰ");
	} else if (min > 24 * 60) {
	    str = "����1��ǰ";
	}
	return str;
    }

    // ��ð������ֺ���ĸ������ַ���,bitΪλ��
    public static String getRandomStr(int bit) {
	String[] str = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
		"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
		"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	Random rdm = new Random();
	StringBuilder sb = new StringBuilder();
	for (int j = 0; j < bit; j++) {
	    sb.append(str[rdm.nextInt(36)]);
	}
	return sb.toString();
    }

    // ��ð������ֵ�����ַ���,bitΪλ��
    public static String getNumberRandomStr(int bit) {
	String[] str = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	Random rdm = new Random();
	StringBuilder sb = new StringBuilder();
	for (int j = 0; j < bit; j++) {
	    sb.append(str[rdm.nextInt(10)]);
	}
	return sb.toString();
    }

    public static String getLocalHostIp() {
	InetAddress addr = null;
	String address = "";
	try {
	    addr = InetAddress.getLocalHost();
	    address = addr.getHostAddress().toString();

	    if (address.indexOf("127.0.0.1") != -1) {
		Enumeration netInterfaces = NetworkInterface
			.getNetworkInterfaces();
		InetAddress ip = null;
		while (netInterfaces.hasMoreElements()) {
		    NetworkInterface ni = (NetworkInterface) netInterfaces
			    .nextElement();
		    System.out.println(ni.getName());
		    ip = (InetAddress) ni.getInetAddresses().nextElement();
		    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
			    && ip.getHostAddress().indexOf(":") == -1) {
			address = ip.getHostAddress();
			// System.out.println("������ip=" + ip.getHostAddress());
			break;
		    } else {
			ip = null;
		    }
		}
	    }
	} catch (UnknownHostException uhe) {
	} catch (SocketException se) {
	}
	return address;// ��ñ���IP
    }

    // ð�ݷ�����
    public static void bubleSort(List<Integer> list, boolean asc) {
	if (list == null)
	    return;
	int n = list.size();
	for (int i = 0; i < n; i++) {
	    for (int j = n - 1; j > i; j--) {
		int int1 = list.get(j);
		int int2 = list.get(j - 1);
		if (asc) {
		    if (int1 < int2) {
			list.set(j, int2);
			list.set(j - 1, int1);
		    }
		} else {
		    if (int1 > int2) {
			list.set(j, int2);
			list.set(j - 1, int1);
		    }
		}
	    }
	}
    }

    // �ٷֱ�,����,��ĸ,��λС����
    public static String percent(double numerator, double denominator,
	    int radixPoint) {
	String str;
	double p3 = numerator / denominator;
	NumberFormat nf = NumberFormat.getPercentInstance();
	nf.setMinimumFractionDigits(radixPoint);
	str = nf.format(p3);
	return str;
    }

    // ת���ɷ���domain��ʽ���ַ���
    public static String toDomain(String domain) {
	int length = domain.length();
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < length; i++) {
	    char c = domain.charAt(i);
	    if (Pattern.matches("[a-z_0-9]", String.valueOf(c))) {
		sb.append(c);
	    }
	}
	String result = sb.toString();
	if (result.length() < 2) {
	    result = getRandomStr(6).toLowerCase();
	}
	if (result.length() > 20) {
	    result = result.substring(0, 20);
	}
	return result;
    }

    private static String startPreChange(String str) {
	str = str.replaceAll("&times;", "#times;");
	str = str.replaceAll("&hellip;", "#hellip;");
	str = str.replaceAll("&amp;", "#amp;");
	str = str.replaceAll("&lt;", "#lt;");
	str = str.replaceAll("&gt;", "#gt;");
	str = str.replaceAll("&nbsp;", "#nbsp;");
	str = str.replaceAll("<br>", "#br#");
	str = str.replaceAll("&ldquo;", "#ldquo;");// ת����˫����
	str = str.replaceAll("&rdquo;", "#rdquo;");// ת����˫����
	str = str.replaceAll("&divide;", "#divide;");// ת�����ں�
	str = str.replaceAll("&mdash;", "#mdash;");// ת�����ۺ�
	return str;
    }

    private static String endPreChange(String str) {
	str = str.replaceAll("#times;", "&times;");
	str = str.replaceAll("#hellip;", "&hellip;");
	str = str.replaceAll("#amp;", "&amp;");
	str = str.replaceAll("#lt;", "&lt;");
	str = str.replaceAll("#gt;", "&gt;");
	str = str.replaceAll("#nbsp;", "&nbsp;");
	str = str.replaceAll("#br#", "<br>");
	str = str.replaceAll("#ldquo;", "&ldquo;");
	str = str.replaceAll("#rdquo;", "&rdquo;");
	str = str.replaceAll("#divide;", "&divide;");
	str = str.replaceAll("#mdash;", "&mdash;");
	return str;
    }

    /*
     * public static String toHTML(String str){ System.out.println(11212); str =
     * str.replaceAll("&", "&amp;"); str = str.replaceAll("<", "&lt;"); str =
     * str.replaceAll(">", "&gt;"); str = str.replaceAll("  ", "&nbsp;&nbsp;");
     * str = str.replaceAll(" ", "&nbsp;"); str = str.replaceAll("\r\n", "\n");
     * str = str.replaceAll("\n", "&nbsp;"); str = str.replaceAll("\t",
     * "&nbsp;&nbsp;&nbsp;&nbsp;"); str = str.replaceAll("'", "&#039;"); str =
     * str.replaceAll("\"", "&#034;"); return str; }
     */

    // max �Ǹ����Ļ���ȫ���滻
    public static String replace(String text, String repl, String with, int max) {
	if (text == null || repl == null || with == null || repl.length() == 0
		|| max == 0) {
	    return text;
	}
	StringBuffer buf = new StringBuffer(text.length());
	int start = 0, end = 0;
	while ((end = text.indexOf(repl, start)) != -1) {
	    buf.append(text.substring(start, end)).append(with);
	    start = end + repl.length();
	    if (--max == 0) {
		break;
	    }
	}
	buf.append(text.substring(start));
	return buf.toString();
    }

    // ȫ���滻
    public static String replace(String text, String repl, String with) {
	return replace(text, repl, with, -1);
    }

    public static String toHTML(String str) {
	return toHTML(str, "<br>", false);
    }

    public static String toHTML(String str, String enter) {
	return toHTML(str, enter, false);
    }

    public static String toHTML(String str, String enter, boolean exceptEm) {
	if (str == null || str.equals(""))
	    return "";
	str = T.replace(str, "&", "&amp;");
	str = T.replace(str, "<", "&lt;");
	str = T.replace(str, ">", "&gt;");
	// str = T.replace(str, "  ", "&nbsp;&nbsp;");
	str = T.replace(str, " ", "&nbsp;");
	str = T.replace(str, "\r\n", "\n");
	str = T.replace(str, "\n", enter);
	str = T.replace(str, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
	str = T.replace(str, "'", "��");
	str = T.replace(str, "\"", "��");
	return str;
    }

    // ���ݾ�̬����������ȡ��Ӧ����ֵ
    public static String getParam(String paramStr, String paramName) {
	if (paramStr == null || paramStr.trim().equals("")) {
	    return "";
	}
	if (paramName == null || paramName.trim().equals("")) {
	    return "";
	}
	paramStr = paramStr.replaceAll("^/*", "/").replaceAll("/*$", "");
	Pattern p = Pattern.compile("/" + paramName + "[^/]*",
		Pattern.CASE_INSENSITIVE);
	Matcher m = p.matcher(paramStr);
	String result = "";
	if (m.find()) {
	    result = m.group().replaceFirst("/" + paramName, "");
	}
	return result;
    }

    public static String toLowercaseStrHead(String str) {
	if (str == null) {
	    return null;
	}
	if (str.length() > 0) {
	    return str.substring(0, 1).toLowerCase()
		    + (str.length() > 1 ? str.substring(1) : "");
	} else {
	    return "";
	}
    }

    public static String filterInput(String html) {
	if (html == null)
	    return html;
	StringBuilder sb = new StringBuilder(html.length());
	for (int i = 0, c = html.length(); i < c; ++i) {
	    char ch = html.charAt(i);
	    switch (ch) {
	    case '>':
		sb.append("&gt;");
		break;
	    case '<':
		sb.append("&lt;");
		break;
	    case '&':
		sb.append("&amp;");
		break;
	    case '"':
		sb.append("&quot;");
		break;
	    case '\'':
		sb.append("&#039");
		break;
	    default:
		sb.append(ch);
		break;
	    }
	}
	return sb.toString();
    }

    public static boolean validate(HttpServletRequest request) {
	if (!request.getMethod().equalsIgnoreCase("POST")) {
	    return false;
	}
	return true;
    }

    // �õ����¿�ʼ֮����ǰ���ڵ�ʱ��
    public static List<Date> getAllDayByMonthAndDay(Date date) {
	List<Date> list = new ArrayList<Date>();
	// �õ����µĿ�ʼ
	Date d1 = getMonthStart(date);
	list.add(d1);
	Date d4 = d1;
	while (d4.getDate() != date.getDate()) {
	    // �ӱ��¿�ʼʱ�����+1 ������ʱ��
	    d4 = getSomeDate(d4, 1);
	    list.add(d4);
	}
	return list;
    }

    /**
     * ���б������ȡ��һ�������Ķ���
     * 
     * @param list
     * @param ranCount
     * @return
     */
    public static List randomList(List list, int ranCount) {
	List result = new ArrayList();
	Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
	int size = list.size();
	if (size <= ranCount)
	    return list;

	Random ran = new Random();
	for (int i = 0; i < ranCount; i++) {
	    int index = ran.nextInt(size);
	    while (indexMap.get(new Integer(index)) != null) {
		index = ran.nextInt(size);
	    }
	    indexMap.put(new Integer(index), new Integer(index));
	}

	for (Iterator it = indexMap.keySet().iterator(); it.hasNext();) {
	    result.add(list.get((Integer) it.next()));
	}
	return result;
    }

    /**
     * ����ȡС����
     * 
     * @param obj1
     * @param obj2
     * @param format
     * @return
     */
    public static String decimalFormat(Object obj1, Object obj2, String format) {
	java.text.DecimalFormat df = new java.text.DecimalFormat(format);
	return df.format(Double.valueOf(String.valueOf(obj2))
		/ Double.valueOf(String.valueOf(obj1)));
    }

    
    // ip long to String
    static public String long2Ip(long ipaddress, boolean bigEndian) {
	StringBuffer sb = new StringBuffer("");
	if (bigEndian) {
	    sb.append(String.valueOf((ipaddress >>> 24)));
	    sb.append(".");
	    sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
	    sb.append(".");
	    sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
	    sb.append(".");
	    sb.append(String.valueOf((ipaddress & 0x000000FF)));
	} else {	    
	    sb.append(String.valueOf((ipaddress & 0x000000FF)));
	    sb.append(".");
	    sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));	    
	    sb.append(".");
	    sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
	    sb.append(".");
	    sb.append(String.valueOf((ipaddress >>> 24)));	    
	}
	return sb.toString();
    }

    // string ip to long
    static public long ip2Long(String ipaddress, boolean bigEndian) {
	long[] ip = new long[4];
	int position1 = ipaddress.indexOf(".");
	int position2 = ipaddress.indexOf(".", position1 + 1);
	int position3 = ipaddress.indexOf(".", position2 + 1);
	if (bigEndian) {
	    ip[0] = Long.parseLong(ipaddress.substring(0, position1));
	    ip[1] = Long.parseLong(ipaddress.substring(position1 + 1, position2));
	    ip[2] = Long.parseLong(ipaddress.substring(position2 + 1, position3));
	    ip[3] = Long.parseLong(ipaddress.substring(position3 + 1));
	} else {
	    ip[3] = Long.parseLong(ipaddress.substring(0, position1));
	    ip[2] = Long.parseLong(ipaddress.substring(position1 + 1, position2));
	    ip[1] = Long.parseLong(ipaddress.substring(position2 + 1, position3));
	    ip[0] = Long.parseLong(ipaddress.substring(position3 + 1));
	}
	return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }
    

    public static void main(String[] args) throws IOException {
	System.out.println(T.ip2Long("10.11.80.51", false));
    }
}