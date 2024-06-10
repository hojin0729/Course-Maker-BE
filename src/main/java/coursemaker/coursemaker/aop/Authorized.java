package coursemaker.coursemaker.aop;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author isaiah
 * 어노테이션을 사용해 해당 메소드 혹은 클래스의 접근권한을 설정하고, 해당 접근권한에 위배되면 예외를 던짐
 */
@Target({ElementType.METHOD, ElementType.TYPE})// METHOD와 클래스, enum에 사용 가능
@Retention(RetentionPolicy.RUNTIME)// 어플리케이션 실행 시점에 정보를 가져옴
public @interface Authorized {
    @AliasFor("role")
    String role() default "ROLE_USER";
}
