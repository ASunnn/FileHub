package sunnn.filehub.dao;

import sunnn.filehub.entity.Commit;

import java.util.List;

public interface CommitDao {

    void insert(Commit commit);

    Commit find(long sequence);

    List<Commit> findAll(@Param("skip") int skip, @Param("limit") int limit);

    void delete(long sequence);
}
