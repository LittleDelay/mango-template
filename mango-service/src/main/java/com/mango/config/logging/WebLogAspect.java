package com.mango.config.logging;

import com.bzn.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 日志拦截器
 *
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    /**
     * 打印请求响应日志
     *
     * @param point 切点
     * @return 响应结果
     * @throws Throwable e
     */
    @Around("execution(public * com.bzn.backend.*.controller..*.*(..))")
    public Object interceptor(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        final String className = point.getTarget().getClass().getName();
        final String methodName = point.getSignature().getName();
        final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        log.info("id {} 方法 {}.{}() 入参: {}", uuid, className, methodName, this.printRequest(point.getArgs()));
        Object result = point.proceed();
        final String responseLog = this.printResponse(result);
        log.info("id {} 方法 {}.{}() 出参: {}", uuid, className, methodName, StringUtils.length(responseLog) > 500 ?
                StringUtils.substring(responseLog, 0, 500) + "..." : responseLog);
        log.info("id {} 方法 {}.{}() 耗时: {}", uuid, className, methodName, System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 打印入参
     *
     * @param params 入参
     * @return 日志
     */
    private String printRequest(Object... params) {
        if (params == null) {
            return "[]";
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Object param : params) {
                if (param instanceof HttpServletRequest
                        || param instanceof HttpServletResponse
                        || param instanceof MultipartFile
                        || param instanceof BindResult
                        || param instanceof MultipartFile[]
                        || param instanceof ModelMap
                        || param instanceof Model
                        || param instanceof ExtendedServletRequestDataBinder
                        || param instanceof byte[]) {
                    continue;
                }
                if (param instanceof ResponseEntity) {
                    sb.append(JsonUtil.toJson(((ResponseEntity<?>) param).getHeaders()));
                } else {
                    sb.append(JsonUtil.toJson(param));
                }
                sb.append(",");
            }
            if (sb.lastIndexOf(",") != -1) {
                sb.deleteCharAt(sb.lastIndexOf(","));
            }
            sb.append("]");
            return sb.toString();
        } catch (Exception e) {
            return "error happen while print log";
        }
    }

    /**
     * 出参
     *
     * @param param 出参
     * @return 日志
     */
    private String printResponse(Object param) {
        if (param == null) {
            return Strings.EMPTY;
        }
        try {
            if (param instanceof ResponseEntity) {
                return JsonUtil.toJson(((ResponseEntity<?>) param).getHeaders());
            }
            return JsonUtil.toJson(param);
        } catch (Exception e) {
            return "error happen while print log";
        }
    }
}
