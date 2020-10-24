package com.ithema.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/13 0013 19:43
 */
@Data
public class QueryVo {
    private List<Account> accountList;
     private Map<String,Account> accountMap;
}
