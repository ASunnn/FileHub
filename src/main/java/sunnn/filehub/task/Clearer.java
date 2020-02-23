package sunnn.filehub.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sunnn.filehub.service.FileService;
import sunnn.filehub.service.ShareService;

@Component
public class Clearer {

    private final FileService fileService;

    private final ShareService shareService;

    @Autowired
    public Clearer(FileService service, ShareService shareService) {
        this.fileService = service;
        this.shareService = shareService;
    }

    @Scheduled(cron = "0 0/15 * * * *")
    public void clearCommit() {
        fileService.clearCommit();
    }

    @Scheduled(cron = "0 0/10 * * * *")
    public void clearShare() {
        shareService.clearShare();
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void clearTemp() {
        fileService.clearTemp();
    }
}