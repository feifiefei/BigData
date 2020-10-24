package com.itheima.count;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Author: 飞
 * Date: 2020/8/24 0024 17:44
 * FileName: countmain
 * Description:
 */
public class countmain implements Writable {
    //定义参数
    private int upPack;
    private int downPack;
    private int upPayload;
    private int downPayload;
    //定义无参构造和get，set


    public countmain() {
    }

    public void setAll(int upPack, int downPack, int upPayload, int downPayload) {
        this.setUpPack(upPack);
        this.setDownPack(downPack);
        this.setUpPayload(upPayload);
        this.setDownPayload(downPayload);
    }
    //重写toString


    @Override
    public String toString() {
        return this.upPack + "\t" + this.downPack + "\t" + this.upPayload + "\t" + this.downPayload;
    }

    public int getUpPack() {
        return upPack;
    }

    public void setUpPack(int upPack) {
        this.upPack = upPack;
    }

    public int getDownPack() {
        return downPack;
    }

    public void setDownPack(int downPack) {
        this.downPack = downPack;
    }

    public int getUpPayload() {
        return upPayload;
    }

    public void setUpPayload(int upPayload) {
        this.upPayload = upPayload;
    }

    public int getDownPayload() {
        return downPayload;
    }

    public void setDownPayload(int downPayload) {
        this.downPayload = downPayload;
    }

    //重写write(序列化)
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.upPack);
        dataOutput.writeInt(this.downPack);
        dataOutput.writeInt(this.upPayload);
        dataOutput.writeInt(this.downPayload);
    }

    //反序列化
    public void readFields(DataInput dataInput) throws IOException {
        this.upPack = dataInput.readInt();
        this.downPack = dataInput.readInt();
        this.upPayload = dataInput.readInt();
        this.downPayload = dataInput.readInt();
    }
}
