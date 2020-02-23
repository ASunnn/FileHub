package sunnn.filehub.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import sunnn.filehub.util.FileHubConstant;
import sunnn.filehub.util.FileHubProperties;
import sunnn.filehub.util.FileUtils;

@Component
public class InitRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        deleteTempFolder();
    }

    private void deleteTempFolder() {
        FileUtils.deletePathForce(FileHubProperties.savePath + "temp");
    }
}