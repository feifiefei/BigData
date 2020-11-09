package cn.fei.controller;

import cn.fei.bean.QuotRes;
import cn.fei.service.QuotService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * @Date 2020/11/7
 * 控制层：开发请求接口
 */
@RestController //包含@Controller和@ResponseBody（表示返回数据会以json格式返回）
@RequestMapping("quot")
public class QuotController {

    @Autowired
    QuotService quotService;


    /**
     * 1.国内指数查询
     */
    @RequestMapping("index/all")
    public QuotRes indexQuery() throws SQLException {

        return quotService.indexQuery();
    }

    /**
     * 2.板块指数
     */
    @RequestMapping("sector/all")
    public QuotRes sectorQuery() throws SQLException {
        return quotService.sectorQuery();
    }

    /**
     * 3.涨幅数据查询
     */
    @RequestMapping("stock/increase")
    public QuotRes increaseQuery() throws SQLException {
        return quotService.increaseQuery();
    }

    /**
     * 4.涨停跌停数
     */
    @RequestMapping("stock/updown/count")
    public JSONObject upDownCount() throws SQLException {
        return quotService.upDownCount();
    }

    /**
     * 5.成交量对比
     */
    @RequestMapping("stock/tradevol")
    public JSONObject compareTradeVol() throws SQLException {

        return quotService.compareTradeVol();
    }

    /**
     * 6.个股涨跌幅度查询
     */
    @RequestMapping("stock/updown")
    public QuotRes increaseRangeQuery() throws SQLException {

        return quotService.increaseRangeQuery();
    }

    /**
     * 7.外盘指数查询，查询mysql
     */

    @RequestMapping("external/index")
    public QuotRes externalQuery(){
        return quotService.externalQuery();
    }

    /**
     * 8.个股分时列表行情
     */
    @RequestMapping("stock/all")
    public QuotRes stockAll() throws SQLException {

        return quotService.stockAll();
    }

    /**
     * 9.个股代码模糊查询
     */
    @RequestMapping("stock/search")
    public QuotRes searchCode(String searchStr) throws SQLException {

        return quotService.searchCode(searchStr);
    }

    /**
     * 10.个股分时详情数据
     */
    @RequestMapping("stock/screen/time-sharing")
    public QuotRes timeSharingQuery(String code) throws SQLException {

        return quotService.timeSharingQuery(code);
    }


    /**
     * 11.个股日K数据查询
     * @param code ：表示是传进来的个股代码
     * @return
     */
    @RequestMapping("stock/screen/dkline")
    public QuotRes dklineQuery(String code){

        return quotService.dklineQuery(code);
    }

    /**
     * 12.个股分时详情-指定个股代码下的最新一条数据
     */
    @RequestMapping("stock/screen/second/detail")
    public JSONObject stockMinDetail(String code) throws SQLException {

        return quotService.stockMinDetail(code);
    }

    /**
     * 13.秒级数据查询
     */
    @RequestMapping("stock/screen/second")
    public QuotRes stockSecondQuery(String code){

        return quotService.stockSecondQuery(code);
    }

    /**
     * 14.个股主营业务描述
     */
    @RequestMapping("stock/describe")
    public JSONObject stockDesc(String code){

        return quotService.stockDesc(code);
    }


}
