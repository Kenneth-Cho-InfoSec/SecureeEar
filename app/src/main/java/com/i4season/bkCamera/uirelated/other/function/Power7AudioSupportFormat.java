package com.i4season.bkCamera.uirelated.other.function;

import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;

public class Power7AudioSupportFormat implements AudioSupportInterface {
    @Override
    public int getSupportFormatMarked(int i, String str) {
        if (str.equals(FileNode.FOLDER_TYPE)) {
            return 5;
        }
        if (str.equals("JPG") || str.equals("JPEG") || str.equals("GIF") || str.equals("PNG") || str.equals("BMP")) {
            return 1;
        }
        if (str.equals("TIF") || str.equals("TIFF") || str.equals("BMPF") || str.equals("RAW") || str.equals("ICO") || str.equals("CUR") || str.equals("XBM")) {
            return 6;
        }
        if (str.equals("MP3")) {
            return 3;
        }
        if (str.equals("OGG") || str.equals("FLAC") || str.equals("APE") || str.equals("AAC") || str.equals("ADTS") || str.equals("CAF") || str.equals("SND") || str.equals("AU") || str.equals("SD2") || str.equals("SF") || str.equals("SND") || str.equals("WAV") || str.equals("AC3") || str.equals("AIF") || str.equals("AIFF") || str.equals("AIFC") || str.equals("AAC PROTECTED") || str.equals("FLA") || str.equals("MP3 VBR") || str.equals("AUDIBLE") || str.equals("APPLE LOSSLESS") || str.equals("M4P") || str.equals("WMA")) {
            return 7;
        }
        if (str.equals("MP4") || str.equals("MKV") || str.equals("3GP") || str.equals("MPG") || str.equals("RM") || str.equals("RMVB") || str.equals("WMV") || str.equals("AVI") || str.equals("M4V") || str.equals("TS") || str.equals("TP") || str.equals("FLV") || str.equals("ASF") || str.equals("VOB") || str.equals("MTS") || str.equals("M2TS") || str.equals("DIVX") || str.equals("OGM") || str.equals("TRP") || str.equals("MPEG") || str.equals("SWF") || str.equals("MPV") || str.equals("MOV") || str.equals("3G2") || str.equals("WEBM") || str.equals("DAT") || str.equals("F4V")) {
            return 2;
        }
        if (str.equals("DOC") || str.equals("DOCX") || str.equals("PAGES") || str.equals("RTF")) {
            return 8;
        }
        if (str.equals("TXT")) {
            return 10;
        }
        if (str.equals("XLS") || str.equals("XLSX") || str.equals("NUMBERS")) {
            return 11;
        }
        if (str.equals("PPT") || str.equals("PPTX") || str.equals("KEY") || str.equals("PPS")) {
            return 12;
        }
        if (str.equals("PDF")) {
            return 13;
        }
        if (str.equals("HTML") || str.equals("HTM")) {
            return 14;
        }
        if (str.equals("APK")) {
            return 15;
        }
        if (str.equals("CHM")) {
            return 16;
        }
        if (str.equals("SWF")) {
            return 17;
        }
        if (str.equals("ZIP")) {
            return 18;
        }
        if (str.equals("RAR")) {
            return 20;
        }
        if (str.equals("TAR")) {
            return 21;
        }
        if (str.equals("VCF")) {
            return 19;
        }
        if (str.equals("EXCEL")) {
            return 22;
        }
        return i;
    }
}
