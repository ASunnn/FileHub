package sunnn.filehub.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class AccessManager {

    private Set<Access> accesses = new HashSet<>(32);

    private static final int MAX_ACCESS_SIZE = 32;

    public static final int FILE_ACCESS = 0;

    public static final int SHARE_ACCESS = 1;

    public boolean newAccess(String id, String seq, int type) {
        if (!checkSize())
            return false;

        if (findAccess(id, seq, type)) {
            accesses.remove(new Access(id, seq, type));
        }
        Access na = new Access(id, seq, type);
        na.setAccessTime(System.currentTimeMillis());
        accesses.add(na);
        return true;
    }

    public boolean findAccess(String id, String seq, int type) {
//        return accesses.contains(new Access(id, seq, type));
        Access target = new Access(id, seq, type);

        Iterator<Access> iterator = accesses.iterator();
        while (iterator.hasNext()) {
            Access a = iterator.next();
            if (a.equals(target)) {
                if (a.isExpired()) {
                    iterator.remove();
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public void removeAccess(String id, String seq, int type) {
        accesses.remove(new Access(id, seq, type));
    }

    private boolean checkSize() {
        if (accesses.size() >= MAX_ACCESS_SIZE)
            trtCleanAccessSet();
        return accesses.size() < MAX_ACCESS_SIZE;
    }

    private synchronized void trtCleanAccessSet() {
        Iterator<Access> iterator = accesses.iterator();

        while (iterator.hasNext()) {
            Access a = iterator.next();
            if (a.isExpired())
                iterator.remove();
        }
    }
}

class Access {

    String sessionId;

    String seq;

    int type;

    long accessTime;

    Access(String sessionId, String seq, int type) {
        this.sessionId = sessionId;
        this.seq = seq;
        this.type = type;
    }

    Access setAccessTime(long accessTime) {
        this.accessTime = accessTime;
        return this;
    }

    public boolean isExpired() {
        return now() - accessTime > FileHubConstant.ACCESS_EXPIRE;
    }

    private long now() {
        return System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Access access = (Access) o;
        return type == access.type &&
                Objects.equals(sessionId, access.sessionId) &&
                Objects.equals(seq, access.seq);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sessionId, type, seq);
    }
}