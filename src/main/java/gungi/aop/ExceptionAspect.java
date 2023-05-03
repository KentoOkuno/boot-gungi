package gungi.aop;

import gungi.api.Connection;
import gungi.api.GungiController;
import gungi.api.response.GungiErrorResponse;
import gungi.exception.GungiException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ExceptionAspect {
    @Autowired
    GungiController gungiController;

    @Around("execution(* gungi.service..*(..))")
    public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (RuntimeException e) {
            Object[] argNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
            Object[] argValues = joinPoint.getArgs();

            int userIdArgsIndex = Arrays.asList(argNames).indexOf("userId");
            if (userIdArgsIndex == -1) {
                throw new GungiException(null, null, e);
            } else {
                int userId = (int) argValues[userIdArgsIndex];
                Integer opponentId = Connection.getOpponentUserId(userId);
                if (opponentId != null) {
                    throw new GungiException(userId, opponentId, e);
                } else {
                    throw new GungiException(userId, null, e);
                }
            }
        }
    }
}
