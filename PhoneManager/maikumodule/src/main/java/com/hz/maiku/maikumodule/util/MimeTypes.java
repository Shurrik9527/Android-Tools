package com.hz.maiku.maikumodule.util;

import android.webkit.MimeTypeMap;

import com.hz.maiku.maikumodule.R;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 手机文件类型
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class MimeTypes {

    private MimeTypes() {}

    private static final long BIG_FILE = 10 * 1024 * 1024;
    private static final HashMap<String, Integer> EXT_ICONS = new HashMap<>();
    private static final HashMap<String, String> MIME_TYPES = new HashMap<>();

    static {
        // 二进制文件
        EXT_ICONS.put("a", R.mipmap.type_unknown);
        EXT_ICONS.put("bin", R.mipmap.type_unknown);
        EXT_ICONS.put("class", R.mipmap.type_unknown);
        EXT_ICONS.put("com", R.mipmap.type_unknown);
        EXT_ICONS.put("dex", R.mipmap.type_unknown);
        EXT_ICONS.put("dump", R.mipmap.type_unknown);
        EXT_ICONS.put("exe", R.mipmap.type_unknown);
        EXT_ICONS.put("dat", R.mipmap.type_unknown);
        EXT_ICONS.put("dll", R.mipmap.type_unknown);
        EXT_ICONS.put("lib", R.mipmap.type_unknown);
        EXT_ICONS.put("o", R.mipmap.type_unknown);
        EXT_ICONS.put("obj", R.mipmap.type_unknown);
        EXT_ICONS.put("pyc", R.mipmap.type_unknown);
        EXT_ICONS.put("pyo", R.mipmap.type_unknown);
        EXT_ICONS.put("ser", R.mipmap.type_unknown);
        EXT_ICONS.put("swf", R.mipmap.type_unknown);
        EXT_ICONS.put("so", R.mipmap.type_unknown);

        // Shell
        EXT_ICONS.put("bar", R.mipmap.type_unknown);
        EXT_ICONS.put("csh", R.mipmap.type_unknown);
        EXT_ICONS.put("ksh", R.mipmap.type_unknown);
        EXT_ICONS.put("sh", R.mipmap.type_unknown);

        // TEXT
        EXT_ICONS.put("csv", R.mipmap.type_note);
        EXT_ICONS.put("diff", R.mipmap.type_note);
        EXT_ICONS.put("in", R.mipmap.type_note);
        EXT_ICONS.put("list", R.mipmap.type_note);
        EXT_ICONS.put("log", R.mipmap.type_note);
        EXT_ICONS.put("rc", R.mipmap.type_note);
        EXT_ICONS.put("text", R.mipmap.type_note);
        EXT_ICONS.put("txt", R.mipmap.type_note);
        EXT_ICONS.put("tsv", R.mipmap.type_note);

        // Properties
        EXT_ICONS.put("properties", R.mipmap.type_config);
        EXT_ICONS.put("conf", R.mipmap.type_config);
        EXT_ICONS.put("config", R.mipmap.type_config);
        EXT_ICONS.put("prop", R.mipmap.type_config);

        // HTML
        EXT_ICONS.put("htm", R.mipmap.type_html);
        EXT_ICONS.put("html", R.mipmap.type_html);
        EXT_ICONS.put("mhtml", R.mipmap.type_html);
        EXT_ICONS.put("xhtml", R.mipmap.type_html);

        // XML
        EXT_ICONS.put("xml", R.mipmap.type_xml);
        EXT_ICONS.put("mxml", R.mipmap.type_xml);

        // DOCUMENT
        EXT_ICONS.put("doc", R.mipmap.type_note);
        EXT_ICONS.put("docx", R.mipmap.type_note);
        EXT_ICONS.put("odp", R.mipmap.type_note);
        EXT_ICONS.put("odt", R.mipmap.type_note);
        EXT_ICONS.put("rtf", R.mipmap.type_note);
        EXT_ICONS.put("ods", R.mipmap.type_note);
        EXT_ICONS.put("xls", R.mipmap.type_note);
        EXT_ICONS.put("xlsx", R.mipmap.type_note);

        // Presentation
        EXT_ICONS.put("ppt", R.mipmap.type_note);
        EXT_ICONS.put("pptx", R.mipmap.type_note);

        // PDF
        EXT_ICONS.put("pdf", R.mipmap.type_pdf);
        EXT_ICONS.put("fdf", R.mipmap.type_pdf);
        EXT_ICONS.put("ldwf", R.mipmap.type_pdf);

        // Package
        EXT_ICONS.put("ace", R.mipmap.type_package);
        EXT_ICONS.put("bz", R.mipmap.type_package);
        EXT_ICONS.put("bz2", R.mipmap.type_package);
        EXT_ICONS.put("cab", R.mipmap.type_package);
        EXT_ICONS.put("cpio", R.mipmap.type_package);
        EXT_ICONS.put("gz", R.mipmap.type_package);
        EXT_ICONS.put("lha", R.mipmap.type_package);
        EXT_ICONS.put("lrf", R.mipmap.type_package);
        EXT_ICONS.put("lzma", R.mipmap.type_package);
        EXT_ICONS.put("rar", R.mipmap.type_package);
        EXT_ICONS.put("tar", R.mipmap.type_package);
        EXT_ICONS.put("tgz", R.mipmap.type_package);
        EXT_ICONS.put("xz", R.mipmap.type_package);
        EXT_ICONS.put("zip", R.mipmap.type_package);
        EXT_ICONS.put("Z", R.mipmap.type_package);
        EXT_ICONS.put("7z", R.mipmap.type_package);
        EXT_ICONS.put("rar", R.mipmap.type_package);
        EXT_ICONS.put("tar", R.mipmap.type_package);
        EXT_ICONS.put("jar", R.mipmap.type_package);

        // Image
        EXT_ICONS.put("bmp", R.mipmap.type_pic);
        EXT_ICONS.put("cgm", R.mipmap.type_pic);
        EXT_ICONS.put("g3", R.mipmap.type_pic);
        EXT_ICONS.put("gif", R.mipmap.type_pic);
        EXT_ICONS.put("ief", R.mipmap.type_pic);
        EXT_ICONS.put("jpe", R.mipmap.type_pic);
        EXT_ICONS.put("jpeg", R.mipmap.type_pic);
        EXT_ICONS.put("jpg", R.mipmap.type_pic);
        EXT_ICONS.put("png", R.mipmap.type_pic);
        EXT_ICONS.put("btif", R.mipmap.type_pic);
        EXT_ICONS.put("svg", R.mipmap.type_pic);
        EXT_ICONS.put("svgz", R.mipmap.type_pic);
        EXT_ICONS.put("tif", R.mipmap.type_pic);
        EXT_ICONS.put("tiff", R.mipmap.type_pic);
        EXT_ICONS.put("psd", R.mipmap.type_pic);
        EXT_ICONS.put("dwg", R.mipmap.type_pic);
        EXT_ICONS.put("dxf", R.mipmap.type_pic);
        EXT_ICONS.put("fbs", R.mipmap.type_pic);
        EXT_ICONS.put("fpx", R.mipmap.type_pic);
        EXT_ICONS.put("fst", R.mipmap.type_pic);
        EXT_ICONS.put("mmr", R.mipmap.type_pic);
        EXT_ICONS.put("rlc", R.mipmap.type_pic);
        EXT_ICONS.put("mdi", R.mipmap.type_pic);
        EXT_ICONS.put("npx", R.mipmap.type_pic);
        EXT_ICONS.put("wbmp", R.mipmap.type_pic);
        EXT_ICONS.put("xif", R.mipmap.type_pic);
        EXT_ICONS.put("ras", R.mipmap.type_pic);
        EXT_ICONS.put("ico", R.mipmap.type_pic);
        EXT_ICONS.put("pcx", R.mipmap.type_pic);
        EXT_ICONS.put("pct", R.mipmap.type_pic);
        EXT_ICONS.put("pic", R.mipmap.type_pic);
        EXT_ICONS.put("xbm", R.mipmap.type_pic);
        EXT_ICONS.put("xwd", R.mipmap.type_pic);
        EXT_ICONS.put("bpg", R.mipmap.type_pic);

        // Audio
        EXT_ICONS.put("aac", R.mipmap.type_music);
        EXT_ICONS.put("adp", R.mipmap.type_music);
        EXT_ICONS.put("aif", R.mipmap.type_music);
        EXT_ICONS.put("aifc", R.mipmap.type_music);
        EXT_ICONS.put("aiff", R.mipmap.type_music);
        EXT_ICONS.put("amr", R.mipmap.type_music);
        EXT_ICONS.put("ape", R.mipmap.type_music);
        EXT_ICONS.put("au", R.mipmap.type_music);
        EXT_ICONS.put("dts", R.mipmap.type_music);
        EXT_ICONS.put("eol", R.mipmap.type_music);
        EXT_ICONS.put("flac", R.mipmap.type_music);
        EXT_ICONS.put("kar", R.mipmap.type_music);
        EXT_ICONS.put("lvp", R.mipmap.type_music);
        EXT_ICONS.put("m2a", R.mipmap.type_music);
        EXT_ICONS.put("m3a", R.mipmap.type_music);
        EXT_ICONS.put("m3u", R.mipmap.type_music);
        EXT_ICONS.put("m4a", R.mipmap.type_music);
        EXT_ICONS.put("mid", R.mipmap.type_music);
        EXT_ICONS.put("mid", R.mipmap.type_music);
        EXT_ICONS.put("mka", R.mipmap.type_music);
        EXT_ICONS.put("mp2", R.mipmap.type_music);
        EXT_ICONS.put("mp3", R.mipmap.type_music);
        EXT_ICONS.put("mpga", R.mipmap.type_music);
        EXT_ICONS.put("oga", R.mipmap.type_music);
        EXT_ICONS.put("ogg", R.mipmap.type_music);
        EXT_ICONS.put("pya", R.mipmap.type_music);
        EXT_ICONS.put("ram", R.mipmap.type_music);
        EXT_ICONS.put("rmi", R.mipmap.type_music);
        EXT_ICONS.put("snd", R.mipmap.type_music);
        EXT_ICONS.put("spx", R.mipmap.type_music);
        EXT_ICONS.put("wav", R.mipmap.type_music);
        EXT_ICONS.put("wax", R.mipmap.type_music);
        EXT_ICONS.put("wma", R.mipmap.type_music);
        EXT_ICONS.put("xmf", R.mipmap.type_music);

        // Video
        EXT_ICONS.put("3gp", R.mipmap.type_video);
        EXT_ICONS.put("3gpp", R.mipmap.type_video);
        EXT_ICONS.put("3g2", R.mipmap.type_video);
        EXT_ICONS.put("3gpp2", R.mipmap.type_video);
        EXT_ICONS.put("h261", R.mipmap.type_video);
        EXT_ICONS.put("h263", R.mipmap.type_video);
        EXT_ICONS.put("h264", R.mipmap.type_video);
        EXT_ICONS.put("jpgv", R.mipmap.type_video);
        EXT_ICONS.put("jpgm", R.mipmap.type_video);
        EXT_ICONS.put("jpm", R.mipmap.type_video);
        EXT_ICONS.put("mj2", R.mipmap.type_video);
        EXT_ICONS.put("mp4", R.mipmap.type_video);
        EXT_ICONS.put("mp4v", R.mipmap.type_video);
        EXT_ICONS.put("mpg4", R.mipmap.type_video);
        EXT_ICONS.put("m1v", R.mipmap.type_video);
        EXT_ICONS.put("m2v", R.mipmap.type_video);
        EXT_ICONS.put("mpa", R.mipmap.type_video);
        EXT_ICONS.put("mpe", R.mipmap.type_video);
        EXT_ICONS.put("mpg", R.mipmap.type_video);
        EXT_ICONS.put("mpeg", R.mipmap.type_video);
        EXT_ICONS.put("ogv", R.mipmap.type_video);
        EXT_ICONS.put("mov", R.mipmap.type_video);
        EXT_ICONS.put("qt", R.mipmap.type_video);
        EXT_ICONS.put("fvt", R.mipmap.type_video);
        EXT_ICONS.put("m4u", R.mipmap.type_video);
        EXT_ICONS.put("pyv", R.mipmap.type_video);
        EXT_ICONS.put("viv", R.mipmap.type_video);
        EXT_ICONS.put("f4v", R.mipmap.type_video);
        EXT_ICONS.put("fli", R.mipmap.type_video);
        EXT_ICONS.put("flv", R.mipmap.type_video);
        EXT_ICONS.put("m4v", R.mipmap.type_video);
        EXT_ICONS.put("asf", R.mipmap.type_video);
        EXT_ICONS.put("asx", R.mipmap.type_video);
        EXT_ICONS.put("avi", R.mipmap.type_video);
        EXT_ICONS.put("wmv", R.mipmap.type_video);
        EXT_ICONS.put("wmx", R.mipmap.type_video);
        EXT_ICONS.put("mkv", R.mipmap.type_video);
        EXT_ICONS.put("divx", R.mipmap.type_video);

        // Application
        EXT_ICONS.put("apk", R.mipmap.type_apk);

        /*
         * ================= MIME TYPES ====================
         */
        MIME_TYPES.put("asm", "text/x-asm");
        MIME_TYPES.put("def", "text/plain");
        MIME_TYPES.put("in", "text/plain");
        MIME_TYPES.put("rc", "text/plain");
        MIME_TYPES.put("list", "text/plain");
        MIME_TYPES.put("log", "text/plain");
        MIME_TYPES.put("pl", "text/plain");
        MIME_TYPES.put("prop", "text/plain");
        MIME_TYPES.put("properties", "text/plain");
        MIME_TYPES.put("rc", "text/plain");

        MIME_TYPES.put("epub", "application/epub+zip");
        MIME_TYPES.put("ibooks", "application/x-ibooks+zip");

        MIME_TYPES.put("ifb", "text/calendar");
        MIME_TYPES.put("eml", "message/rfc822");
        MIME_TYPES.put("msg", "application/vnd.ms-outlook");

        MIME_TYPES.put("ace", "application/x-ace-compressed");
        MIME_TYPES.put("bz", "application/x-bzip");
        MIME_TYPES.put("bz2", "application/x-bzip2");
        MIME_TYPES.put("cab", "application/vnd.ms-cab-compressed");
        MIME_TYPES.put("gz", "application/x-gzip");
        MIME_TYPES.put("lrf", "application/octet-stream");
        MIME_TYPES.put("jar", "application/java-archive");
        MIME_TYPES.put("xz", "application/x-xz");
        MIME_TYPES.put("Z", "application/x-compress");

        MIME_TYPES.put("bat", "application/x-msdownload");
        MIME_TYPES.put("ksh", "text/plain");
        MIME_TYPES.put("sh", "application/x-sh");

        MIME_TYPES.put("db", "application/octet-stream");
        MIME_TYPES.put("db3", "application/octet-stream");

        MIME_TYPES.put("otf", "x-font-otf");
        MIME_TYPES.put("ttf", "x-font-ttf");
        MIME_TYPES.put("psf", "x-font-linux-psf");

        MIME_TYPES.put("cgm", "image/cgm");
        MIME_TYPES.put("btif", "image/prs.btif");
        MIME_TYPES.put("dwg", "image/vnd.dwg");
        MIME_TYPES.put("dxf", "image/vnd.dxf");
        MIME_TYPES.put("fbs", "image/vnd.fastbidsheet");
        MIME_TYPES.put("fpx", "image/vnd.fpx");
        MIME_TYPES.put("fst", "image/vnd.fst");
        MIME_TYPES.put("mdi", "image/vnd.ms-mdi");
        MIME_TYPES.put("npx", "image/vnd.net-fpx");
        MIME_TYPES.put("xif", "image/vnd.xiff");
        MIME_TYPES.put("pct", "image/x-pict");
        MIME_TYPES.put("pic", "image/x-pict");

        MIME_TYPES.put("adp", "audio/adpcm");
        MIME_TYPES.put("au", "audio/basic");
        MIME_TYPES.put("snd", "audio/basic");
        MIME_TYPES.put("m2a", "audio/mpeg");
        MIME_TYPES.put("m3a", "audio/mpeg");
        MIME_TYPES.put("oga", "audio/ogg");
        MIME_TYPES.put("spx", "audio/ogg");
        MIME_TYPES.put("aac", "audio/x-aac");
        MIME_TYPES.put("mka", "audio/x-matroska");

        MIME_TYPES.put("jpgv", "video/jpeg");
        MIME_TYPES.put("jpgm", "video/jpm");
        MIME_TYPES.put("jpm", "video/jpm");
        MIME_TYPES.put("mj2", "video/mj2");
        MIME_TYPES.put("mjp2", "video/mj2");
        MIME_TYPES.put("mpa", "video/mpeg");
        MIME_TYPES.put("ogv", "video/ogg");
        MIME_TYPES.put("flv", "video/x-flv");
        MIME_TYPES.put("mkv", "video/x-matroska");
    }

    public static int getIconForExt(String ext) {
        final Integer res = EXT_ICONS.get(ext);
        return res == null ? 0 : res;
    }

    public static String getMimeType(File file) {
        if (file.isDirectory()) {
            return null;
        }

        String type = null;
        final String extension = FileUtil.getExtension(file.getName());

        if (extension != null && !extension.isEmpty()) {
            final String extensionLowerCase = extension.toLowerCase(Locale.getDefault());
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extensionLowerCase);
            if (type == null) {
                type = MIME_TYPES.get(extensionLowerCase);
            }
        }

        if (type == null)
            type = "*/*";
        return type;
    }

    private static boolean mimeTypeMatch(String mime, String input) {
        return Pattern.matches(mime.replace("*", ".*"), input);
    }

    public static boolean isPicture(File f) {
        final String mime = getMimeType(f);
        return mime != null && mimeTypeMatch("image/*", mime);
    }

    public static boolean isVideo(File f) {
        final String mime = getMimeType(f);
        return mime != null && mimeTypeMatch("video/*", mime);
    }

    public static boolean isDoc(File f){
        final String mime = getMimeType(f);
        return mime != null && (mime.equals("text/plain")
                || mime.equals("application/pdf")
                || mime.equals("application/msword")
                || mime.equals("application/vnd.ms-excel"));
    }

    public static boolean isApk(File f) {
        String path = FileUtil.getFileName(f);
        return path.endsWith(".apk");
    }

    public static boolean isZip(File f) {
        String path = FileUtil.getFileName(f);
        return path.endsWith(".zip");
    }

    public static boolean isMusic(File f) {
        final String REGEX = "(.*/)*.+\\.(mp3|m4a|ogg|wav|aac)$";
        return f.getName().matches(REGEX);
    }

    public static boolean isBigFile(File f) {
        return f.length() > BIG_FILE;
    }

    public static boolean isTempFile(File f) {
        String name = FileUtil.getFileName(f);
        return name.endsWith(".tmp") || name.endsWith(".temp");
    }

    public static boolean isLog(File f) {
        String name = FileUtil.getFileName(f);
        return name.endsWith(".log");
    }

}
