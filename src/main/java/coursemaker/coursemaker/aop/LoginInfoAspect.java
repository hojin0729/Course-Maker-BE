package coursemaker.coursemaker.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LoginInfoAspect {
    @ModelAttribute

    /*로그인 한 유저 정보 가져오기*/
    @Around("@annotation(coursemaker.coursemaker.aop.LoginedUser)")
    public Object loginedUserAspect(ProceedingJoinPoint proceedingJoinPoint)throws Throwable {

        /*http request 긁어옴*/
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();// AOP에서는 RequestContextHolder에서 요청을 끄집어 내야 함

        /*로그인 정보 추출*/
        Object userInfo = request.getAttribute("user");// 로그인 한 유저 정보

        /*결과값 반환*/

        proceedingJoinPoint.proceed();
        return userInfo;

    }

    /*로그인 한 유저 권한 확인*/
    @Around("@annotation(coursemaker.coursemaker.aop.Authorized)")
    public Object authorizedAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        return result;
    }

}
