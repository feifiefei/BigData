package cn.fei.exception;

import cn.fei.constant.BspCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常，请求参数有误，
 * 遇到该错误需抛出终止请求，不可try catch
 */
@Data
@NoArgsConstructor
public class ParameterException extends CommException{
    /**
     * 错误码，由BspCode中获取
     * */
    private Integer errorCode;

    public ParameterException(String message){
        super(message);
    }
    public ParameterException(Integer errorCode, String message){
        super(message);
        this.errorCode=errorCode;
    }
    public ParameterException(BspCode bspCode){
        super(bspCode.getMsg());
        this.errorCode=bspCode.getSubCode();
    }
}
