package sunnn.filehub.dao;

import org.apache.ibatis.annotations.Param;
import sunnn.filehub.entity.Share;

import java.util.List;

public interface ShareDao {

    void insert(Share share);

    Share find(String id);

    List<Share> findAll();

    Share findByIdAndKey(@Param("id") String id, @Param("key") String key);

    void delete(String id);
}
