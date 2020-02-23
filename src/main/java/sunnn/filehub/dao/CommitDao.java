package sunnn.filehub.dao;

import org.apache.ibatis.annotations.Param;
import sunnn.filehub.entity.Commit;

import java.util.List;

public interface CommitDao {

    void insert(Commit commit);

    Commit find(long sequence);

    List<Commit> findAll(@Param("skip") int skip, @Param("limit") int limit);

    List<Commit> findAllByFilter(@Param("skip") int skip, @Param("limit") int limit,
                                 @Param("name") String name, @Param("start") String start,  @Param("end") String end,
                                 @Param("type") List type);

    Commit findBySeqAndKey(@Param("sequence") long sequence, @Param("key") String key);

    int count();

    int countByFilter(@Param("name") String name, @Param("start") String start,
                      @Param("end") String end, @Param("type") List type);

    void delete(long sequence);
}
