package sunnn.filehub.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import sunnn.filehub.exception.IllegalPropertiesException;
import sunnn.filehub.exception.UnSupportSystemException;
import sunnn.filehub.util.FileHubConstant;
import sunnn.filehub.util.FileHubProperties;
import sunnn.filehub.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class PropertiesConfiguration {

    @Bean
    public FileHubProperties fileHubProperties() throws UnSupportSystemException, IOException, IllegalPropertiesException {
        try {
            ConfigLoader.loadSystemConfig();
        } catch (UnSupportSystemException | IOException | IllegalPropertiesException e) {
            Logger log = LoggerFactory.getLogger(FileHubProperties.class);
            log.error("Start FileHub Failed : ", e);
            throw e;
        }
        return new FileHubProperties();
    }
}

class ConfigLoader {

    public static void loadSystemConfig() throws UnSupportSystemException, IOException, IllegalPropertiesException {
        File propertiesFile =
                new File(getPropertiesFilePath());
        InputStream is = new FileInputStream(propertiesFile);

        Properties properties = new Properties();
        properties.load(is);
        parseFileHubProperties(properties);
    }

    private static String getPropertiesFilePath() throws UnSupportSystemException {
        return Utils.getPropertiesPath() + FileHubConstant.PROPERTIES_FILE;
    }

    private static void parseFileHubProperties(Properties properties) throws IllegalPropertiesException {
        String port = properties.getProperty("port");
        if (port != null)
            FileHubProperties.setPort(Integer.valueOf(port));

        String verifyCode = properties.getProperty("verifyCode");
        if (verifyCode == null)
            throw new IllegalPropertiesException("Cannot Find Properties 'verifyCode'");
        FileHubProperties.setVerifyCode(verifyCode);

        String savePath = properties.getProperty("savePath");
        if (savePath == null)
            throw new IllegalPropertiesException("Cannot Find Properties 'savePath'");
        FileHubProperties.setSavePath(savePath);

        String localhost = properties.getProperty("path");
        if (localhost == null)
            throw new IllegalPropertiesException("Cannot Find Properties 'path'");
        FileHubProperties.setPath(localhost);


        String loginTimeout = properties.getProperty("loginTimeout");
        if (loginTimeout != null)
            FileHubProperties.setLoginTimeout(Integer.valueOf(loginTimeout));


        String host = properties.getProperty("host");
        if (host == null)
            throw new IllegalPropertiesException("Cannot Find Properties 'host'");
        FileHubProperties.setHost(host);

        String database = properties.getProperty("database");
        if (database == null)
            throw new IllegalPropertiesException("Cannot Find Properties 'database'");
        FileHubProperties.setDatabase(database);

        String username = properties.getProperty("username");
        if (username == null)
            throw new IllegalPropertiesException("Cannot Find Properties 'username'");
        FileHubProperties.setUsername(username);

        String password = properties.getProperty("password");
        if (password == null)
            throw new IllegalPropertiesException("Cannot Find Properties 'password'");
        FileHubProperties.setPassword(password);
    }
}
