package cn.fei.mail;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;

/**
 * 测试类
 */
public class MailSend {

    /**
     * @param info 包含警告信息描述
     */
    public static void send(String info){
        String host = "smtp.163.com";//网易163邮件传输协议     腾讯 qq的是smtp.qq.com
        /**
         * 这里需要注意一下  如果你想用qq邮箱作为发件人邮箱的话  记得把邮箱传输协议host值改为smtp.qq.com
         * 另外 username登陆名还是 一样  直接写QQ号，不用加后缀
         */
        String username = "cep201912";//发件人邮箱的用户名 这里不要加后缀@163.com

        /**  注意事项
         * 如果是用的QQ邮箱的话  这里的password不能写QQ邮箱的登陆密码  你要去登录到QQ邮箱  点  设置>账户   下面会有一个"POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务"
         * 选项，把"POP3/SMTP服务"  开启来 这时候 如果你之前没开启过 那么会提示你 设置独立密码  设置完成后  password的值就写你刚才设置的独立密码即可 ，否则会验证失败
         * 如果你用的是163或者126的话 就直接写登陆密码即可
         */
        //授权码
        String password = "123456cep";  //发件人邮箱的客户端授权码

        /**
         * 这里发件人 要写全名
         */
        String from = "cep201912@163.com";//发件人的邮箱 全名 加后缀

        /**
         * 收件人 同样要写全名
         */
        String to = "cep6666@163.com";//收件人的邮箱

        /**
         * 主题自定义
         */
        String subject = "邮件测试-CEP";//邮件主题
        /**
         * 自定义
         */
        //String content = "兄台，你好";//邮件的内容

        /**
         * 调用写好的邮件帮助类 MailUtils  直接调用createSession 根据以上（host, username, password三个参数）创建出session
         */
        Session session = MailUtils.createSession(host, username, password);
        /**
         * 创建邮件对象from, to,subject,content 这三个参数
         */
        Mail mail = new Mail(from, to,subject,info);
        try {
            /**
             * 最后一步  调用MailUtils的send方法 将session和创建好的邮件对象传进去  发送就ok了
             */
            MailUtils.send(session, mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
