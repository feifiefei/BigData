package cn.fei.cron;

import cn.fei.mapper.QuotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Date 2020/11/9
 * 是将周K，月K表中的数据trade_date，更新成当日时间
 */
@EnableScheduling //开启定时任务
@Component
public class CronTask {

    @Autowired
    QuotMapper quotMapper;

    //0/10 * * * * ? 表示每10s执行一次
    @Scheduled(cron = "${cron.pattern.loader}")
    public void cronTask() {

        /**
         * 开发步骤：
         * 1.查询交易日历表获取交易日
         * 2.当前周K数据查询
         * 3.更新周K日期
         * 4.当前月K数据查询
         * 5.更新月K日期
         * 更新规则：
         * 有数据：将日期全部更新为最新日期
         * 无数据：不更新日期
         */
        //1.查询交易日历表获取交易日
        Map<String, Object> map = quotMapper.queryTccDate();
        //获取trade_date
        String tradeDate = map.get("trade_date").toString();
        //获取week_first_txdate
        String weekFirstTxdate = map.get("week_first_txdate").toString();
        //获取month_first_txdate
        String monthFirstTxdate = map.get("month_first_txdate").toString();

        //2.当前周K数据查询
        List<Map<String, Object>> weekList = quotMapper.queryKline("bdp_quot_stock_kline_week", weekFirstTxdate, tradeDate);
        if(weekList != null && weekList.size() > 0){ //周K一定有数据
            //3.更新周K日期
            quotMapper.updateKline("bdp_quot_stock_kline_week", weekFirstTxdate, tradeDate);
        }

        //4.当前月K数据查询
        List<Map<String, Object>> monthList = quotMapper.queryKline("bdp_quot_stock_kline_month", monthFirstTxdate, tradeDate);
        if(monthList != null && monthList.size() >0){
            //5.更新月K日期
            quotMapper.updateKline("bdp_quot_stock_kline_month", monthFirstTxdate, tradeDate);
        }
    }
}
