package sunnn.filehub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sunnn.filehub.dao.CommitDao;
import sunnn.filehub.dao.FilesDao;
import sunnn.filehub.dao.TypeDao;
import sunnn.filehub.dto.request.CommitListRequest;
import sunnn.filehub.dto.request.CommitFilesRequest;
import sunnn.filehub.dto.response.Response;
import sunnn.filehub.dto.response.CommitFilesResponse;
import sunnn.filehub.dto.response.CommitInfoResponse;
import sunnn.filehub.entity.Commit;
import sunnn.filehub.entity.Files;
import sunnn.filehub.entity.Type;
import sunnn.filehub.entity.Word;
import sunnn.filehub.exception.FileNotFoundException;
import sunnn.filehub.util.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FileService {

    @Resource
    private CommitDao commitDao;

    @Resource
    private FilesDao filesDao;

    @Resource
    private TypeDao typeDao;

    private HashMap<Long, Long> temp = new HashMap<>();

    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT)
    public Response saveUploadFiles(CommitFilesRequest request) {
        Commit commit = buildUploadEntity(request);

        // 保存至硬盘
        if (!saveFiles(commit, request.getFiles()))
            return new Response(StatusCode.ERROR).setDetail("保存文件至磁盘时发生错误");

        // 存数据库
        commitDao.insert(commit);
        filesDao.insertAll(commit.getFiles());
        typeDao.insertAll(commit.getTypes());

        return new Response(StatusCode.OJBK);
    }

    public Response getCommitList(CommitListRequest request) {
        List<Commit> list;
        int count;

        int size = FileHubConstant.PAGE_SIZE;
        int skip = request.getPage() * size;
        if (request.noFilter()) {
            list = commitDao.findAll(skip, size);
            count = commitDao.count();
        } else {
            String name = request.getName();
            // 处理时间
            String startDate = request.getStartDate();
            String endDate = request.getEndDate();
            LocalDateTime ldts =  startDate.isEmpty() ?
                    LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.of("+8"))
                    : LocalDateTime.of(LocalDate.parse(startDate), LocalTime.of(0, 0, 0));
            LocalDateTime ldte = endDate.isEmpty() ?
                    LocalDateTime.ofEpochSecond(Integer.MAX_VALUE, 0, ZoneOffset.of("+8"))
                    : LocalDateTime.of(LocalDate.parse(endDate), LocalTime.of(23, 59, 59));
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            startDate = ldts.format(formatter);
            endDate = ldte.format(formatter);

            String t = request.getFilter();
            List<Integer> types = new ArrayList<>();
            for (int i = 0; i < t.length(); ++i) {
                if (t.charAt(i) == '1')
                types.add(i);
            }

            list = commitDao.findAllByFilter(skip, size, name, startDate, endDate, types);
            count = commitDao.countByFilter(name, startDate, endDate, types);
        }
        return new CommitFilesResponse(StatusCode.OJBK)
                .setCount((int) Math.ceil((double) count / FileHubConstant.PAGE_SIZE))
                .setList(list);
    }

    public Response getCommitInfo(long sequence) {
        Commit c = commitDao.find(sequence);
        if (c == null)
            return new Response(StatusCode.ILLEGAL_REQUEST).setDetail("无效的seq");
        List<Files> files = filesDao.findAllBySeq(sequence);

        return new CommitInfoResponse(StatusCode.OJBK)
                .setSequence(c.getSequence())
                .setName(c.getName())
                .setTotalSize(c.getTotalSize())
                .setUploadTime(c.getUploadTime())
                .setExpire(c.getExpire())
                .setFiles(files);
    }

    public File downloadFiles(long sequence) throws FileNotFoundException, IOException {
        String path = Utils.getCommitFolder(sequence);

        if (commitDao.find(sequence) == null || !FileUtils.isExists(path))
            throw new FileNotFoundException();

        String zipPath = Utils.getZipFile(sequence);

        if (temp.get(sequence) == null) {
            ZipCompress.compress(path, zipPath);
        }
        temp.put(sequence, Utils.now());

        return new File(zipPath);
    }

    public File downloadFile(long sequence, String name) throws FileNotFoundException {
        String path = Utils.getCommitFile(sequence, name);

        if (commitDao.find(sequence) == null || !FileUtils.isExists(path))
            throw new FileNotFoundException();

        return new File(path);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.DEFAULT)
    public Response deleteCommit(long sequence) {
        if (commitDao.find(sequence) == null)
            return new Response(StatusCode.ILLEGAL_REQUEST).setDetail("无效的seq");

        commitDao.delete(sequence);
        filesDao.deleteAllBySeq(sequence);
        typeDao.deleteAllBySeq(sequence);

        if (!FileUtils.deletePathForce(Utils.getCommitFolder(sequence)))
            return new Response(StatusCode.ERROR).setDetail("从磁盘删除文件时发生错误");

        return new Response(StatusCode.OJBK);
    }

    public synchronized void clearCommit() {
        List<Commit> commits = commitDao.findAll(0, Integer.MAX_VALUE);

        for (Commit c : commits) {
            int expire = c.getExpire();
            if (expire == -1)
                continue;

            long ut = c.getUploadTime().getTime();
            if (Utils.now() - ut > expire * 86400 * 1000) {
                deleteCommit(c.getSequence());
            }
        }
    }

    public synchronized void clearTemp() {
        Iterator<Map.Entry<Long, Long>> i = temp.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<Long, Long> t = i.next();

            long putTime = t.getValue();
            if (Utils.now() - putTime > FileHubConstant.ACCESS_EXPIRE) {
                FileUtils.deleteFile(Utils.getZipFile(t.getKey()));
                i.remove();
            }
        }
    }

    private Commit buildUploadEntity(CommitFilesRequest request) {
        String name = request.getName().trim();
        String key = request.getKey().trim();
        long sequence = generateSequence(name, request.getFiles());
        long totalSize = 0;
        for (MultipartFile f : request.getFiles()) {
            totalSize += f.getSize();
        }
        return new Commit()
                .setSequence(sequence)
                .setName(name)
                .setKey(key)
                .setEncrypt(!key.isEmpty())
                .setExpire(request.getExpire())
                .setFiles(buildUploadFiles(request.getFiles(), sequence))
                .setTypes(analyzeUploadType(request.getFiles(), sequence))
                .setTotalSize(totalSize)
                .setUploadTime(new Timestamp(System.currentTimeMillis()));
    }

    private long generateSequence(String name, MultipartFile[] files) {
        StringBuilder source = new StringBuilder(name);
        for (MultipartFile f : files) {
            source.append(f.getOriginalFilename());
        }
        return MD5s.getMD5Sequence(source.toString());
    }

    private List<Files> buildUploadFiles(MultipartFile[] files, long seq) {
        List<Files> res = new ArrayList<>();
        for (MultipartFile f : files) {
            String src = seq + f.getOriginalFilename();
            String md5 = MD5s.getMD5(src);
            res.add(new Files()
                    .setSequence(seq)
                    .setId(md5)
                    .setName(f.getOriginalFilename())
                    .setSize(f.getSize()));
        }
        return res;
    }

    private List<Type> analyzeUploadType(MultipartFile[] files,  long seq) {
        Set<Word> res = new HashSet<>();
        for (MultipartFile f : files) {
            String name = f.getOriginalFilename().toLowerCase();
            res.add(Utils.checkFileName(name));
        }


        List<Type> types = new ArrayList<>();
        for (Word w : res) {
            types.add(new Type()
                    .setType(w.getCode())
                    .setSequence(seq));
        }
        return types;
    }

    private boolean saveFiles(Commit commit, MultipartFile[] files) {
        String path = Utils.getCommitFolder(commit.getSequence());
        if (FileUtils.createPath(path)) {
            for (MultipartFile f : files) {
                try {
                    FileUtils.storeFile(f, path);
                } catch (IOException e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
