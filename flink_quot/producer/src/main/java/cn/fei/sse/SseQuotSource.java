package cn.fei.sse;

import cn.fei.avro.SseAvro;
import cn.fei.util.DateTimeUtil;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;

import java.io.*;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Date 2020/10/29
 */
//1.实现自定义source接口
public class SseQuotSource extends AbstractSource implements PollableSource, Configurable {
    private String host;
    private Integer port;
    private String userName;
    private String password;
    private String ftpDirectory;
    private String fileName;
    private String localDirectory;
    private Integer delay;
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer keepAliveTime;
    private Integer capacity;
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * （1）实现目标
     * 使用flume自定义source从FTP服务器采集实时行情文本文件
     * （2）实现步骤
     * 1.实现自定义source接口
     * 2.初始化参数（初始化source参数和线程池）
     * 3.判断是否是交易时间段
     * 4.异步处理
     * 5.设置延时时间
     */
    @Override
    public Status process() throws EventDeliveryException {
        //3.判断是否是交易时间段 9:30-15
        long time = new Date().getTime();
        if(time < DateTimeUtil.closeTime && time > DateTimeUtil.openTime){

            //业务逻辑处理在时间判断里面
            //4.异步处理
            threadPoolExecutor.execute(new AsyncTask());
            //5.设置延时时间
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Status.READY;//有数据
        }
        return Status.BACKOFF;//无数据
    }

    /**
     * 2.初始化参数（初始化source参数和线程池）
     */
    @Override
    public void configure(Context context) {

        host = context.getString("host");
        port = context.getInteger("port");
        userName = context.getString("userName");
        password = context.getString("password");
        ftpDirectory = context.getString("ftpDirectory");
        fileName = context.getString("fileName");
        localDirectory = context.getString("localDirectory");
        delay = context.getInteger("delay");
        corePoolSize = context.getInteger("corePoolSize");
        maxPoolSize = context.getInteger("maxPoolSize");
        keepAliveTime = context.getInteger("keepAliveTime");
        capacity = context.getInteger("capacity");
        /**
         * 创建线程池
         */
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<>(capacity));

    }

    //开启一个线程
    //1.创建异步线程task
    private class AsyncTask implements Runnable {
        @Override
        public void run() {
            /**
             * 开发步骤：
             * 1.创建异步线程task
             * 2.下载行情文件
             * 3.解析并发送数据
             *   数据转换成avro
             *   数据序列化
             * 4.发送数据到channel
             */
            //2.下载行情文件
            download();

            try {
                //3.解析并发送数据
                //解析本地下载好得ftp文件
                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(localDirectory + "/" + fileName))));
                String str;
                int i =0; //文件行下标
                while ((str = bf.readLine())!=null){

                    String[] arr = str.split("\\|");
                    if(i == 0){ //说明是首行
                        //取行情标识字段
                        String status = arr[8];
                        if(status.startsWith("E")){
                            break; //直接跳出
                        }
                    }else{ //非首行情

                        //数据转换成avro
                        SseAvro sseAvro = parseAvro(arr);

                        //数据序列化
                        byte [] bte = serializer(sseAvro);
                        //4.发送数据到channel
                        getChannelProcessor().processEvent(EventBuilder.withBody(bte));
                    }
                    i++;//文件行下标自增
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //序列化转换机
        private byte[] serializer(SseAvro sseAvro) {

            //定义schema约束
            SpecificDatumWriter<SseAvro> datumWriter = new SpecificDatumWriter<>(SseAvro.class);
            //获取二进制编码对象
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BinaryEncoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(bos, null);

            try {
                //将avro对象编码程字节输出流
                datumWriter.write(sseAvro,binaryEncoder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bos.toByteArray();
        }

        private SseAvro parseAvro(String[] arr) {
            //封装avro对象
            SseAvro sseAvro = new SseAvro();
            sseAvro.setMdStreamID(arr[0].trim());
            sseAvro.setSecurityID(arr[1].trim());
            sseAvro.setSymbol(arr[2].trim());
            sseAvro.setTradeVolume(Long.valueOf(arr[3].trim()));
            sseAvro.setTotalValueTraded(Long.valueOf(arr[4].trim()));
            sseAvro.setPreClosePx(Double.valueOf(arr[5].trim()));
            sseAvro.setOpenPrice(Double.valueOf(arr[6].trim()));
            sseAvro.setHighPrice(Double.valueOf(arr[7].trim()));
            sseAvro.setLowPrice(Double.valueOf(arr[8].trim()));
            sseAvro.setTradePrice(Double.valueOf(arr[9].trim()));
            sseAvro.setClosePx(Double.valueOf(arr[10].trim()));
            sseAvro.setTradingPhaseCode("T11");
            sseAvro.setTimestamp(new Date().getTime());
            return sseAvro;
        }

        private void download() {

            /**
             * 开发步骤：
             * 1.初始化ftp连接
             * （1）设置IP和Port
             * （2）设置登陆用户名和密码
             * (3) 设置编码格式
             * （4）判断是否连接成功（FTPReply）
             * 2.切换工作目录，设置被动模式
             * 3.获取工作目录的文件信息列表
             * 4.输出文件
             * 5.退出，返回成功状态
             */
            //创建FTPClient对象
            FTPClient ftpClient = new FTPClient();
            try {
                //1.初始化ftp连接
                ftpClient.connect(host,port);
                //(2）设置登陆用户名和密码
                ftpClient.login(userName,password);
                //(3) 设置编码格式
                ftpClient.setControlEncoding("UTF-8");
                //（4）判断是否连接成功（FTPReply）
                //获取ftp连接是否成功得返回码
                int replyCode = ftpClient.getReplyCode();
                if(FTPReply.isPositiveCompletion(replyCode)){
                    //2.切换工作目录，设置被动模式（了解）
                    //被动模式，服务端开放端口，用于数据传输
                    ftpClient.enterLocalPassiveMode();
                    //禁用服务端参与的验证，如果不禁用服务端会获取主机IP与提交的host进行匹配，不一致时会报错
                    ftpClient.setRemoteVerificationEnabled(false);
                    //2.切换工作目录
                    ftpClient.changeWorkingDirectory(ftpDirectory);
                    //3.获取工作目录的文件信息列表
                    FTPFile[] ftpFiles = ftpClient.listFiles();
                    for (FTPFile ftpFile : ftpFiles) {

                        //4.输出文件
                        ftpClient.retrieveFile(ftpFile.getName(),new FileOutputStream(new File(localDirectory+"/"+fileName)));
                    }

                }
                // 5.退出，返回成功状态
                ftpClient.logout();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
