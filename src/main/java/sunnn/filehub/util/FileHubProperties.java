package sunnn.filehub.util;

import java.io.File;

public class FileHubProperties {

    public static int port = 80;

    public static String verifyCode;

    public static String savePath;

    public static int loginTimeout = 120;

    public static String host;

    public static String database;

    public static String username;

    public static String password;

    public static void setPort(int port) {
        FileHubProperties.port = port;
    }

    public static void setVerifyCode(String verifyCode) {
        FileHubProperties.verifyCode = verifyCode;
    }

    public static void setSavePath(String savePath) {
        if (!savePath.endsWith("/") && !savePath.endsWith("\\"))
            FileHubProperties.savePath = savePath + File.separator;
        else
            FileHubProperties.savePath = savePath;
    }

    public static void setLoginTimeout(int loginTimeout) {
        FileHubProperties.loginTimeout = loginTimeout * 3600;
    }

    public static void setHost(String host) {
        FileHubProperties.host = host;
    }

    public static void setDatabase(String database) {
        FileHubProperties.database = database;
    }

    public static void setUsername(String username) {
        FileHubProperties.username = username;
    }

    public static void setPassword(String password) {
        FileHubProperties.password = password;
    }
}
