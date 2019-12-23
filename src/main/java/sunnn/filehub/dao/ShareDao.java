package sunnn.filehub.dao;

import sunnn.filehub.entity.Share;

import java.util.List;

public interface ShareDao {

    void insert(Share share);

    Share find(String id);

    List<Share> findAll();

    void delete(String id);
}
