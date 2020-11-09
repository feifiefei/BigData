package cn.fei.aop;

import cn.fei.bean.QuotRes;
import cn.fei.constant.Constants;
import cn.fei.exception.CustomException;
import cn.fei.until.ClientIPUtils;
import cn.fei.until.FlowidUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

/**
 * @Date 2020/9/24
 * 日志、异常拦截切面对象
 * ControllerAdvice:统一处理异常
 */
//1.创建切面对象
@Aspect
@Component
@ControllerAdvice
public class ControllerAspect {
    Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);
    /**
     * 开发步骤：
     * 1.创建切面对象
     * 2.定义切点
     * 3.新建环绕通知方法
     * （1）RequestContextHolder获取request
     * （2）获取请求参数
     * （3）process执行业务
     * （4）获取耗时
     * （5）定义log方法，设置打印参数
     * （6）封装日志消息体
     * （7）返回结果集
     */

    //2.定义切点
    //定义拦截点
    @Pointcut("execution(public * cn.fei.controller.*.*(..))")
    public void pointcunt(){}

    //3.新建环绕通知方法
    @Around(value = "pointcunt()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        //（1）RequestContextHolder获取request
        //上下文请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //（2）获取请求参数
        Object[] args = joinPoint.getArgs();
        //（3）process执行业务
        Object object = joinPoint.proceed();
        //控制层执行完毕之后，接着处理
        //（4）获取耗时
        long endTime = System.currentTimeMillis();
        long costTime = (endTime - startTime) / 1000;
        logger.info("endtime:"+endTime);
        //（5）定义log方法，设置打印参数
        //  日志内容包含参数：流水id,请求方法，请求URL,客户端请求IP，请求参数，
        //  响应结果，请求开始时间，请求结束时间，耗时，服务标识字段。
        SimpleDateFormat sf = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS2);
        logInfo(
                FlowidUtil.getFlowid(),
                request.getMethod(),
                request.getRequestURL(),
                ClientIPUtils.getClientIp(request),
                args,
                object,
                sf.format(startTime),
                sf.format(endTime),
                costTime,
                "quotService"
        );

        return object;
    }

    private void logInfo(Object ... args) {
        /**
         * 通过格式化模板打印日志数据
         */
        logger.info("flowId[{}];requestMethod[{}];requestUrl[{}];clientIp[{}];" +
                "requestParams[{}];result[{}];startTime[{}];endTime[{}];costTime[{}]," +
                "serverFlag[{}]",args);
    }

    /**
     * 统一异常处理
     */
    @Autowired
    CustomException customException;

    // 1.新建异常统一处理方法
    @ExceptionHandler
    @ResponseBody
    public QuotRes deaxException(HttpServletRequest request,Exception ex){
        /**
         *  开发步骤：
         *  1.新建异常统一处理方法
         *  2.封装日志消息体
         *  3.自定义异常处理类
         *  (1)内存溢出异常
         * （2）请求参数异常
         *  4.封装异常返回结果
         */
       // 2.封装日志消息体
        logInfo(
                FlowidUtil.getFlowid(),
                request.getMethod(),
                request.getRequestURL(),
                ClientIPUtils.getClientIp(request),
                null,
                ex.getMessage(), //异常日志
                null,
                null,
                null,
                "quotService"
        );

        return customException.dealExec(ex);
    }


}
