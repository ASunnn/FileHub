package sunnn.filehub.util;

import sunnn.filehub.exception.UnSupportSystemException;

public class Utils {

    /**
     * 根据不同系统获取配置文件路径
     */
    public static String getPropertiesPath() throws UnSupportSystemException {
        String sys = System.getProperty("os.name");

        if (sys.contains("Windows")) {
            return "C:\\ProgramData\\Sunnn\\filehub\\";
        } else if (sys.contains("Linux"))
            return "";

        throw new UnSupportSystemException(sys);
    }
}
