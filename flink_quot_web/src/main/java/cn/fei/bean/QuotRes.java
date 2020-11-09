package cn.fei.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @description 行情服务公共返回体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotRes {

    private List<Map<String, Object>> items;//返回集合数据

    private JSONObject json;//返回单条数据

    private Integer code;//返回码

    private String exMsg ;//异常信息

}
