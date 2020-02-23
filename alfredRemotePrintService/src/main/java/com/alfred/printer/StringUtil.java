package com.alfred.printer;

import com.alfred.print.jobs.PrintJob;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String padRight(String txt, int len) {
        int length = 0;
        StringBuilder ret = null;
        try {
            length = txt.getBytes("GBK").length;
            ret = new StringBuilder(txt);
            if (len > length) {
                int pad = len - length;
                String padchars = String.format(Locale.US,"%1$" + pad + "s", "");
                ret.append(padchars);
            }
            return (ret.toString());
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return txt;
    }

    public static String padLeft(String txt, int len) {
        int length = 0;
        StringBuilder ret = null;
        try {
            length = txt.getBytes("GBK").length;
            ret = new StringBuilder(txt);
            if (len > length) {
                int pad = len - length;
                String padchars = String.format(Locale.US,"%1$" + pad + "s", "");
                ret.insert(0, padchars);
            }
            return (ret.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return txt;
    }

    public static String padCenter(String txt, int len) {
        int length = 0;
        StringBuilder ret = null;
        try {
            length = txt.getBytes("GBK").length;
            ret = new StringBuilder(txt);
            String padchars = String.format(Locale.US,"%1$" + len + "s", "");
            ret.insert(0, padchars);
            ret.append(padchars);
            float mid = (length + len) / 2;
            float start = mid - (len / 2);
            float end = start + len;
            String out = ret.substring((int) start, (int) end);

            return out;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return txt;
    }

    public static String padCenterWithDash(String txt, int len) {
        int length = 0;
        StringBuilder ret = null;
        String out = txt;
        try {
            txt = " " + txt + " ";
            length = txt.getBytes("GBK").length;

            if (len > length) {
                ret = new StringBuilder(txt);
                int padlen = (len - length) / 2;
                String padchars = String.format(Locale.US,"%1$" + padlen + "s", "-");
                padchars = padchars.replace(' ', '-');
                ret.insert(0, padchars);
                ret.append(padchars);
                out = ret.toString();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static ArrayList<String> splitEqually(String text, int size) {
        int length = text.length();
        byte[] data = text.getBytes();

        try {
            length = text.getBytes("GBK").length;
            data = text.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<String> ret = new ArrayList<String>((length + size - 1) / size);

        for (int start = 0; start < length; start += size) {
            byte[] tmp = new byte[size];

            for (int m = 0; m < size; m++) {
                if (start + m < length)
                    tmp[m] = data[start + m];
            }
            String splitted = null;
            try {
                splitted = new String(tmp, "GBK");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ret.add(splitted);
        }
        return ret;
    }

    public static int nearestTen(double num) {
        return (int) Math.ceil((num / 5d) * 5);
    }


    public static boolean isChinese(String args) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(args);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    public static String[] formatLn(int size, String orginStr)
            throws UnsupportedEncodingException {
        StringBuffer stringBuffer = new StringBuffer();
        List<String> list = Arrays.asList(orginStr.replaceAll("\\s{2,}", " ")
                .split(" "));
        int newSize = 0;
        for (String str : list) {
            if (str.getBytes("GBK").length > size) {
                if (stringBuffer.length() != 0) {
                    stringBuffer.append("\n");
                }
//				stringBuffer.append("\n").append(str).append("\n");
                char[] Characterlist = str.toCharArray();
                int newChatSize = 0;
                for (char c : Characterlist) {
                    newChatSize += Character.toString(c).getBytes("GBK").length;
                    if (newChatSize <= size) {
                        stringBuffer.append(c);
                    } else {
                        stringBuffer.append("\n").append(c);
                        newChatSize = Character.toString(c).getBytes("GBK").length;
                    }
                }
                stringBuffer.append(" ");
                newSize = newChatSize + 1;
                continue;
            }
            newSize += str.toString().getBytes("GBK").length;
            if (newSize <= size) {
                if (list.indexOf(str) == list.size() - 1) {
                    stringBuffer.append(str);
                } else {
                    stringBuffer.append(str).append(" ");
                    newSize++;
                }
            } else {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                stringBuffer.append("\n").append(str).append(" ");
                newSize = str.toString().getBytes("GBK").length + 1;
            }
        }
        return stringBuffer.toString().split("\n");
    }

    public static String getStr(String[] strs) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            str.append(strs[i] + PrintJob.reNext);
        }
        return str.toString();
    }
}
