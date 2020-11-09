package cn.fei.exception;

import cn.fei.constant.BspCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常，用户信息（用户名、密码）凭证无效、越权访问，
 * 遇到该错误需抛出终止请求，不可try catch
 */
@Data
@NoArgsConstructor
public class UnAuthorizedException extends CommException{
    /**
     * 错误码，由BspCode中获取
     * */
    private Integer errorCode;

    public UnAuthorizedException(String message){
        super(message);
    }
    public UnAuthorizedException(Integer errorCode, String message){
        super(message);
        this.errorCode=errorCode;
    }
    public UnAuthorizedException(BspCode bspCode){
        super(bspCode.getMsg());
        this.errorCode=bspCode.getSubCode();
    }
}
