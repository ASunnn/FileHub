package sunnn.filehub.dao;

import sunnn.filehub.entity.Type;

import java.util.List;

public interface TypeDao {

    void insertAll(List types);

    void deleteAllBySeq(long sequence);
}
