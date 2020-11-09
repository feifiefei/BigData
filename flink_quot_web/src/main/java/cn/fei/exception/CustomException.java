package cn.fei.exception;

import cn.fei.bean.QuotRes;
import cn.fei.constant.HttpCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @Date 2020/9/24
 * 自定义异常
 */
//3.自定义异常处理类
@Component
@Qualifier("customException")
public class CustomException {

    public QuotRes dealExec(Exception ex){

        QuotRes quotRes = new QuotRes();
        //判断异常类型
        if(ex instanceof ParameterException){
            quotRes.setCode(((ParameterException) ex).getErrorCode());
        }else if(ex instanceof UnAuthorizedException){
            quotRes.setCode(((UnAuthorizedException) ex).getErrorCode());
        }else{
            quotRes.setCode(HttpCode.ERR_SERV_500.getCode());
        }
        quotRes.setExMsg(ex.getMessage());
        return quotRes;
    }
}
