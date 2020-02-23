package sunnn.filehub.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import sunnn.filehub.exception.FileNotFoundException;
import sunnn.filehub.exception.NoAccessException;
import sunnn.filehub.service.AuthorizeService;
import sunnn.filehub.util.AccessManager;

@Aspect
@Component
public class AccessAspect {

    private final AuthorizeService service;

    @Autowired
    public AccessAspect(AuthorizeService service) {
        this.service = service;
    }

    @Pointcut("execution(public * sunnn.filehub.controller.FileController.*(..))")
    public void fileAccessPoint() {
    }

    @Pointcut("execution(public * sunnn.filehub.controller.ShareController.*(..))")
    public void shareAccessPoint() {
    }

//    @Before("fileAccessPoint() || shareAccessPoint()")
    @Before("fileAccessPoint()")
    public void accessCheck(JoinPoint joinPoint) throws NoAccessException, FileNotFoundException {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        String session = ra.getSessionId();

        tryRefreshAccess(session, joinPoint);

        long seq = (long) joinPoint.getArgs()[0];
        if (service.checkAccess(session, String.valueOf(seq), AccessManager.FILE_ACCESS)) {
            return;
        }

        if (joinPoint.getSignature().getName().equals("download"))
            throw new FileNotFoundException();
        throw new NoAccessException();
    }

    @Before("shareAccessPoint()")
    public void shareAccessCheck(JoinPoint joinPoint) throws NoAccessException, FileNotFoundException {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        String session = ra.getSessionId();

        if (joinPoint.getSignature().getName().equals("check"))
            return;

        tryRefreshAccess(session, joinPoint);

        String id = (String) joinPoint.getArgs()[0];
        if (service.checkAccess(session, id, AccessManager.SHARE_ACCESS)) {
            return;
        }

        if (joinPoint.getSignature().getName().equals("download"))
            throw new FileNotFoundException();
        throw new NoAccessException();
    }

    private void tryRefreshAccess(String session, JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        if (method.equals("getFile") || method.equals("getShare")) {
            String seq = String.valueOf(joinPoint.getArgs()[0]);
            String key = (String) joinPoint.getArgs()[1];
            int type = method.equals("getFile") ? AccessManager.FILE_ACCESS : AccessManager.SHARE_ACCESS;

            service.authorize(session, String.valueOf(seq), key, type);
        }
    }
}
