package sunnn.filehub.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sunnn.filehub.dto.response.Response;
import sunnn.filehub.exception.FileNotFoundException;
import sunnn.filehub.service.ShareService;

import java.io.File;
import java.io.IOException;

@RestController
public class ShareController {

    private final ShareService shareService;

    @Autowired
    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    @GetMapping("/share/check")
    public Response check(@RequestParam("id") String id) {
        return shareService.checkShare(id);
    }

    @PostMapping("/share/{id}")
    public Response getShare(@PathVariable("id") String id, @RequestParam("key") String key) {
        return shareService.getShareInfo(id);
    }

    @GetMapping("/share/download/{id}")
    public ResponseEntity download(@PathVariable("id") String id) throws FileNotFoundException, IOException {
        File file = shareService.downloadFiles(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",
                new String(file.getName().getBytes(), "ISO-8859-1"));
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    @GetMapping("/share/download/{id}/{name}")
    public ResponseEntity download(@PathVariable("id") String id, @PathVariable("name") String name) throws FileNotFoundException, IOException {
        File file = shareService.downloadFile(id, name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",
                new String(file.getName().getBytes(), "ISO-8859-1"));
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }
}
