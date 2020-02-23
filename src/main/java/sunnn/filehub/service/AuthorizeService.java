package sunnn.filehub.service;

import org.springframework.stereotype.Service;
import sunnn.filehub.dao.CommitDao;
import sunnn.filehub.dao.ShareDao;
import sunnn.filehub.util.AccessManager;

import javax.annotation.Resource;

@Service
public class AuthorizeService {

    @Resource
    private CommitDao commitDao;

    @Resource
    private ShareDao shareDao;

    private AccessManager manager = new AccessManager();

    /**
     * 对访问权限进行认证
     * 若认证通过，则会尝试新建一个access
     * 否则会将原有的access删除（如果有的话）
     */
    public boolean authorize(String id, String seq, String key, int type) {
        if (type == AccessManager.FILE_ACCESS) {
            if (commitDao.findBySeqAndKey(Long.parseLong(seq), key) != null) {
                return manager.newAccess(id, seq, type);
            }
        } else if (type == AccessManager.SHARE_ACCESS) {
            if (shareDao.findByIdAndKey(seq, key) != null) {
                return manager.newAccess(id, seq, type);
            }
        }
        manager.removeAccess(id, seq, type);
        return false;
    }

    public boolean checkAccess(String id, String seq, int type) {
        return manager.findAccess(id, seq, type);
    }
}