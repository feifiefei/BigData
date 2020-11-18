package cn.fei;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Date 2020/11/4
 * 正则表达式
 */
public class PatternTest {

    public static void main(String[] args) {

        String str = "adsfasd";

        //通过正则表达式，判断是否包含数字
        //定义规则
        Pattern compile = Pattern.compile("\\d+"); //d表示数字0-9
        //规则匹配数据
        Matcher matcher = compile.matcher(str);
        if(matcher.find()){
            System.out.println("有数字");
        }else{
            System.out.println("无数字");
        }
    }
}
