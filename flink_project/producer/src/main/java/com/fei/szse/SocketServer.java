package com.fei.szse;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 深市行情实时行情广播数据
 */
public class SocketServer {
    private static final int PORT = 4444;

    public static void main(String[] args) {
        ServerSocket server ;
        Socket socket ;
        DataOutputStream out ;
        try {
            server = new ServerSocket(PORT, 1000, InetAddress.getByName("localhost"));
            socket = server.accept();
            out = new DataOutputStream(socket.getOutputStream());
            while (true) {
                Thread.sleep(1000);
                List<String> list = parseIndexFile();
                for (Object ling : list) {
                    out.writeUTF(ling.toString());
                }

                List<String> list1 = parseStockFile();
                for (Object line : list1) {
                    out.writeUTF(line.toString());
                }
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 个股行情
     *
     * @return
     */
    private static List<String> parseStockFile() {
        ArrayList<String> list = new ArrayList<>();
        try {
            //本地磁盘路径
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("G:\\bianchengxuexi\\GitWarehouse\\BigData\\flink_project\\datas\\szse-stock.txt")),"UTF-8"));
            //服务器磁盘路径
            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("//export//servers//tmp//socket//szse-stock.txt")),"UTF-8"));

            String lineTxt ;
            while ((lineTxt = br.readLine()) != null) {
                list.add(lineTxt);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("errors :" + e);
        }
        return list;
    }

    /**
     * 指数行情
     *
     * @return
     */
    public static List<String> parseIndexFile() {

        ArrayList<String> list = new ArrayList<>();
        try {
            //本地磁盘路径
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("G:\\bianchengxuexi\\GitWarehouse\\BigData\\flink_project\\datas\\szse-index.txt")),"UTF-8"));
            //服务器磁盘路径
            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("//export//servers//tmp//socket//szse-index.txt")),"UTF-8"));
            String lineTxt ;
            while ((lineTxt = br.readLine()) != null) {
                list.add(lineTxt);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("errors :" + e);
        }
        return list;
    }
}