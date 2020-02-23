package sunnn.filehub.dao;

import sunnn.filehub.entity.Files;

import java.util.List;

public interface FilesDao {

    void insertAll(List files);

    List<Files> findAllBySeq(long sequence);

    void deleteAllBySeq(long sequence);
}
