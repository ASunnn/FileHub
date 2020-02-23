package sunnn.filehub.util;

import sun.java2d.opengl.WGLSurfaceData;
import sunnn.filehub.entity.Word;
import sunnn.filehub.exception.UnSupportSystemException;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * 根据不同系统获取配置文件路径
     */
    public static String getPropertiesPath() throws UnSupportSystemException {
        String sys = System.getProperty("os.name");

        if (sys.contains("Windows")) {
            return "C:\\ProgramData\\Sunnn\\filehub\\";
        } else if (sys.contains("Linux"))
            return "/usr/Sunnn/filehub/";

        throw new UnSupportSystemException(sys);
    }

    public static String getCommitFolder(long sequence) {
        return FileHubProperties.savePath + sequence;
    }

    public static String getCommitFile(long sequence, String name) {
        return FileHubProperties.savePath + sequence + File.separator + name;
    }

    public static String getZipFile(long sequence) {
        return FileHubProperties.savePath + "temp" + File.separator + sequence + ".zip";
    }

    public static Word checkFileName(String fileName) {
        return checkIsImg(fileName);
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    public static String formatSize(long size) {
        if (size < (1 << 10)) {
            double res = (double) size / (1 << 10);
            long mid = Math.round(res * 100);
            return  ((double) mid / 100) + "KB";
        } else if (size < 1 << 20) {
            return  (Math.round(size / (1 << 10))) + "KB";
        } else if (size < 1 << 30) {
            double res = (double) size / (1 << 20);
            long mid = Math.round(res * 100);
            return ((double) mid / 100) + "MB";
        } else {
            double res = (double) size / (1 << 30);
            long mid = Math.round(res * 100);
            return ((double) mid / 100) + "GB";
        }
    }

    private static Word checkIsImg(String fileName) {
        Matcher extensionNameMatcher = Pattern.compile("\\.(jpg|jpeg|png|bmp|gif|psd|psb|dng)$")
                .matcher(fileName);

        if (extensionNameMatcher.find())
            return Word.Img;
        return checkIsMov(fileName);
    }

    private static Word checkIsMov(String fileName) {
        Matcher extensionNameMatcher = Pattern.compile("\\.(mp4|mkv|flv|avi|ass)$")
                .matcher(fileName);

        if (extensionNameMatcher.find())
            return Word.Mov;
        return checkIsAudio(fileName);
    }

    private static Word checkIsAudio(String fileName) {
        Matcher extensionNameMatcher = Pattern.compile("\\.(mp3|flac|alac|aac|ape|wav|cue|ogg)$")
                .matcher(fileName);

        if (extensionNameMatcher.find())
            return Word.Audio;
        return checkIsPdf(fileName);
    }

    private static Word checkIsPdf(String fileName) {
        Matcher extensionNameMatcher = Pattern.compile("\\.(pdf)$")
                .matcher(fileName);

        if (extensionNameMatcher.find())
            return Word.Pdf;
        return checkIsDoc(fileName);
    }

    private static Word checkIsDoc(String fileName) {
        Matcher extensionNameMatcher = Pattern.compile("\\.(doc|docx|xls|xlsx|ppt|pptx)$")
                .matcher(fileName);

        if (extensionNameMatcher.find())
            return Word.Doc;
        return checkIsZip(fileName);
    }

    private static Word checkIsZip(String fileName) {
        // 考虑了分part的压缩文件，但是xxxx.7z.这种格式也会被匹配上
        Matcher extensionNameMatcher = Pattern.compile("\\.(zip|rar|7z)[\\.a-zA-Z|\\d]*$")
                .matcher(fileName);

        if (extensionNameMatcher.find())
            return Word.Zip;
        return mustIsOther();
    }

    private static Word mustIsOther() {
        return Word.Other;
    }
}
