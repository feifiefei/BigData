package cn.fei.service;

import cn.fei.bean.QuotRes;
import com.alibaba.fastjson.JSONObject;

import java.sql.SQLException;

public interface QuotService {


    QuotRes indexQuery() throws SQLException;

    QuotRes sectorQuery() throws SQLException;

    QuotRes increaseQuery() throws SQLException;

    JSONObject upDownCount() throws SQLException;

    JSONObject compareTradeVol() throws SQLException;

    QuotRes increaseRangeQuery() throws SQLException;

    QuotRes externalQuery();

    QuotRes stockAll() throws SQLException;

    QuotRes searchCode(String searchStr) throws SQLException;

    QuotRes timeSharingQuery(String code) throws SQLException;

    QuotRes dklineQuery(String code);

    JSONObject stockMinDetail(String code) throws SQLException;

    QuotRes stockSecondQuery(String code);

    JSONObject stockDesc(String code);
}
