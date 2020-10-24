package takeTheTopThree;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Author: 飞
 * Date: 2020/8/26 0026 14:27
 * FileName: SgouBean
 * Description: SgouBean
 */
public class SgouBean implements Writable {
    protected String time;
    protected String pid;
    protected String word;

    public SgouBean() {
    }

    public void setAll(String time, String word) {
        this.setPid(time);
        this.setWord(word);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return this.time + "\t" + this.word;
    }


    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.time);
        dataOutput.writeUTF(this.word);
    }

    public void readFields(DataInput dataInput) throws IOException {
        this.time = dataInput.readUTF();
        this.word = dataInput.readUTF();
    }
}
