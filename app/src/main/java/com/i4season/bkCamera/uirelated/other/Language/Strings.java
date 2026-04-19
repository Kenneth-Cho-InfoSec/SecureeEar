package com.i4season.bkCamera.uirelated.other.Language;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.util.Log;
import com.i4season.bkCamera.WDApplication;
import com.i4season.i4season_camera.C0413R;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParserException;

public class Strings {
    public static final String LANGUAGE_AUTO = "auto";
    public static final String LANGUAGE_CROATIAN = "croatian";
    public static final String LANGUAGE_CZECH = "czech";
    public static final String LANGUAGE_DUTCH = "dutch";
    public static final String LANGUAGE_ENGLISH = "english";
    public static final String LANGUAGE_FINNISH = "suomen kieli";
    public static final String LANGUAGE_FRENCH = "french";
    public static final String LANGUAGE_GERMAN = "german";
    public static final String LANGUAGE_HUNGARIAN = "hungarian";
    public static final String LANGUAGE_ITALIAN = "italian";
    public static final String LANGUAGE_JANPANESE = "japanese";
    public static final String LANGUAGE_KOREAN = "korean";
    public static final String LANGUAGE_POLISH = "polish";
    public static final String LANGUAGE_PORTUGUESE = "portuguese";
    public static final String LANGUAGE_RUSSIAN = "russian";
    public static final String LANGUAGE_SCHINESE = "schinese";
    public static final String LANGUAGE_SERBIAN = "serbian";
    public static final String LANGUAGE_SPANISH = "spanish";
    public static final String LANGUAGE_SWEDISH = "svenska";
    public static final String LANGUAGE_TCHINESE = "tchinese";
    public static final String LANGUAGE_de = "de";
    public static final String LANGUAGE_en = "en";
    public static final String LANGUAGE_es = "es";
    public static final String LANGUAGE_fr = "fr";
    public static final String LANGUAGE_it = "it";
    public static final String LANGUAGE_ja = "ja";
    public static final String LANGUAGE_kr = "ko";
    public static final String LANGUAGE_nl = "nl";
    public static final String LANGUAGE_pl = "pl";
    public static final String LANGUAGE_pt = "pt";
    public static final String LANGUAGE_ru = "ru";
    public static final String LANGUAGE_zh_rCN = "zh_CN";
    public static final String LANGUAGE_zh_rTW = "zh_TW";
    public static final String PHONE_HUAWEI1 = "Huawei";
    public static final String PHONE_HUAWEI2 = "HUAWEI";
    public static String language = "auto";
    public static HashMap<Integer, Object> stringCustom;

    public static String getLanguage() {
        return language;
    }

    public static void initLanguage(Context context) {
        language = LANGUAGE_AUTO;
        String str = Build.MANUFACTURER;
        if (PHONE_HUAWEI1.equals(str) || PHONE_HUAWEI2.equals(str)) {
            initLanguageForHuawei(context);
            return;
        }
        String string = Locale.getDefault().toString();
        if (string.startsWith(LANGUAGE_en)) {
            language = LANGUAGE_ENGLISH;
            stringCustom = readStringsXML(context, C0413R.xml.strings_en);
            return;
        }
        if (string.startsWith(LANGUAGE_zh_rCN)) {
            language = LANGUAGE_SCHINESE;
            stringCustom = readStringsXML(context, C0413R.xml.strings_cn);
            return;
        }
        if (string.startsWith(LANGUAGE_de)) {
            language = LANGUAGE_GERMAN;
            stringCustom = readStringsXML(context, C0413R.xml.strings_de);
            return;
        }
        if (string.startsWith(LANGUAGE_es)) {
            language = LANGUAGE_SPANISH;
            stringCustom = readStringsXML(context, C0413R.xml.strings_es);
            return;
        }
        if (string.startsWith(LANGUAGE_ja)) {
            language = LANGUAGE_JANPANESE;
            stringCustom = readStringsXML(context, C0413R.xml.strings_ja);
        } else if (string.startsWith(LANGUAGE_fr)) {
            language = LANGUAGE_FRENCH;
            stringCustom = readStringsXML(context, C0413R.xml.strings_fr);
        } else if (string.startsWith(LANGUAGE_it)) {
            language = LANGUAGE_FRENCH;
            stringCustom = readStringsXML(context, C0413R.xml.strings_it);
        } else {
            language = LANGUAGE_ENGLISH;
            stringCustom = readStringsXML(context, C0413R.xml.strings_en);
        }
    }

    public static void initLanguageForHuawei(Context context) {
        String language2 = Locale.getDefault().getLanguage();
        Log.d("liusheng", "language: " + language2);
        if (language.startsWith(LANGUAGE_en)) {
            language = LANGUAGE_ENGLISH;
            stringCustom = readStringsXML(context, C0413R.xml.strings_en);
            return;
        }
        if (language2.startsWith("zh")) {
            language = LANGUAGE_SCHINESE;
            stringCustom = readStringsXML(context, C0413R.xml.strings_cn);
            return;
        }
        if (language2.startsWith(LANGUAGE_ja)) {
            language = LANGUAGE_JANPANESE;
            stringCustom = readStringsXML(context, C0413R.xml.strings_ja);
            return;
        }
        if (language2.startsWith(LANGUAGE_de)) {
            language = LANGUAGE_GERMAN;
            stringCustom = readStringsXML(context, C0413R.xml.strings_de);
            return;
        }
        if (language2.startsWith(LANGUAGE_es)) {
            language = LANGUAGE_SPANISH;
            stringCustom = readStringsXML(context, C0413R.xml.strings_es);
        } else if (language2.startsWith(LANGUAGE_fr)) {
            language = LANGUAGE_FRENCH;
            stringCustom = readStringsXML(context, C0413R.xml.strings_fr);
        } else if (language2.startsWith(LANGUAGE_it)) {
            language = LANGUAGE_FRENCH;
            stringCustom = readStringsXML(context, C0413R.xml.strings_it);
        } else {
            language = LANGUAGE_ENGLISH;
            stringCustom = readStringsXML(context, C0413R.xml.strings_en);
        }
    }

    public static String getString(int i, Context context) {
        return getString(i, LanguageInfo.isAUTO, context);
    }

    public static String getString(int i) {
        return getString(i, LanguageInfo.isAUTO, WDApplication.getInstance());
    }

    private static String getString(int i, boolean z, Context context) {
        try {
            if (z) {
                return context.getString(i);
            }
            return (String) stringCustom.get(Integer.valueOf(i));
        } catch (Exception unused) {
            return "";
        }
    }

    public static String[] getStringArray(int i) {
        return (String[]) stringCustom.get(Integer.valueOf(i));
    }

    private static String[] readStringArray(XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException {
        LinkedList linkedList = new LinkedList();
        while (true) {
            xmlResourceParser.next();
            if ("string-array".equals(xmlResourceParser.getName())) {
                return (String[]) linkedList.toArray(new String[linkedList.size()]);
            }
            String name = xmlResourceParser.getName();
            if (xmlResourceParser.getEventType() == 2 && name.equals("item")) {
                xmlResourceParser.next();
                linkedList.add(xmlResourceParser.getText());
            }
        }
    }

    private static HashMap<Integer, Object> readStringsXML(Context context, int i) {
        HashMap<Integer, Object> map = new HashMap<>();
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        XmlResourceParser xml = context.getResources().getXml(i);
        try {
            for (int next = xml.next(); next != 1; next = xml.next()) {
                if (next == 2) {
                    String name = xml.getName();
                    if ("string".equals(name)) {
                        String attributeValue = xml.getAttributeValue(0);
                        if (xml.next() == 4) {
                            map.put(Integer.valueOf(resources.getIdentifier(attributeValue, "string", packageName)), xml.getText());
                        }
                    } else if ("string-array".equals(name)) {
                        int identifier = resources.getIdentifier(xml.getAttributeValue(0), "array", packageName);
                        map.put(Integer.valueOf(identifier), readStringArray(xml));
                    }
                }
            }
        } catch (IOException | XmlPullParserException unused) {
        }
        return map;
    }

    public static void setLanguage(Context context, String str) {
        if (str.equals(language)) {
            return;
        }
        if (str.equals(LANGUAGE_ENGLISH)) {
            stringCustom = readStringsXML(context, C0413R.xml.strings_en);
            language = str;
            return;
        }
        if (str.equals(LANGUAGE_SCHINESE)) {
            stringCustom = readStringsXML(context, C0413R.xml.strings_cn);
            language = str;
            return;
        }
        if (str.equals(LANGUAGE_TCHINESE)) {
            stringCustom = readStringsXML(context, C0413R.xml.strings_tw);
            language = str;
            return;
        }
        if (str.equals(LANGUAGE_GERMAN)) {
            stringCustom = readStringsXML(context, C0413R.xml.strings_de);
            language = str;
            return;
        }
        if (str.equals(LANGUAGE_JANPANESE)) {
            stringCustom = readStringsXML(context, C0413R.xml.strings_ja);
            language = str;
            return;
        }
        if (str.equals(LANGUAGE_SPANISH)) {
            stringCustom = readStringsXML(context, C0413R.xml.strings_es);
            language = str;
            return;
        }
        if (str.equals(LANGUAGE_PORTUGUESE)) {
            stringCustom = readStringsXML(context, C0413R.xml.strings_pt);
            language = str;
            return;
        }
        if (str.equals(LANGUAGE_KOREAN)) {
            stringCustom = readStringsXML(context, C0413R.xml.strings_ko);
            language = str;
            return;
        }
        if (str.equals(LANGUAGE_FRENCH)) {
            stringCustom = readStringsXML(context, C0413R.xml.strings_fr);
            language = str;
        } else if (str.equals(LANGUAGE_ITALIAN)) {
            stringCustom = readStringsXML(context, C0413R.xml.strings_it);
            language = str;
        } else {
            if (language.equals(LANGUAGE_SCHINESE)) {
                return;
            }
            stringCustom = readStringsXML(context, C0413R.xml.strings_cn);
            language = str;
        }
    }

    public static String getAppName(Context context) {
        String string = getString(C0413R.string.app_name, context);
        return string != null ? string : "";
    }
}
