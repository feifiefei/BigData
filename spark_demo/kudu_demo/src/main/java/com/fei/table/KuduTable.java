package com.fei.table;


import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.junit.After;
import org.junit.Before;

/**
 * @description:测试对kudu表的创建删除
 */
public class KuduTable {
    private KuduClient kuduClient = null;

    @Before
    public void init() {
        //构建KuduClient实例对象
        kuduClient = new KuduClient.KuduClientBuilder("node2.itcast.cn:7051")
                //设置超时时间间隔，默认值10s
                .defaultSocketReadTimeoutMs(6000)
                //采用建造者模式构建实例对象
                .build();
    }
    
    @After
    public void clean() throws KuduException{
        //关闭KuduClient对象，释放资源
        if (null != kuduClient) kuduClient.close();
    }
}
