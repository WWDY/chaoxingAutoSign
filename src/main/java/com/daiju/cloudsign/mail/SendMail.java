package com.daiju.cloudsign.mail;

import com.daiju.cloudsign.entity.CourseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author WDY
 * @Date 2021-01-06 15:25
 * @Description 发送邮件通知
 */
@Component
public class SendMail {
    @Autowired
    JavaMailSender javaMailSender;

    public String getTemplate(CourseInfo courseInfo){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return  "\n" +
                "<body style=\"margin: 0; padding: 0;\">\n" +
                "  <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\"> 　\n" +
                "    <tr>\n" +
                "      <td>\n" +
                "        <div style=\"margin: 20px;text-align: center;margin-top: 50px\">  </div>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td>\n" +
                "        <div style=\"border: #36649d 1px dashed;margin: 30px;padding: 20px\"> <label\n" +
                "            style=\"font-size: 22px;color: #36649d;font-weight: bold\">用户："+courseInfo.getPhone()+"</label>\n" +
                "          <p style=\"font-size: 16px\">科目&nbsp;<label style=\"font-weight: bold\"> "+courseInfo.getName()+"</label> </p>\n" +
                "          <p style=\"font-size: 16px\">您已于"+simpleDateFormat.format(new Date())+"签到成功！</p>\n" +
                "        </div>\n" +
                "      </td>\n" +
                "    </tr> 　 <tr>\n" +
                "      <td>\n" +
                "        <div style=\"margin: 40px\">\n" +
                "          <p style=\"font-size: 16px\">呆橘云签到团队</p>\n" +
                "          <p style=\"color:red;font-size: 14px \">（这是一封自动发送的邮件，请勿回复。）</p>\n" +
                "        </div>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td>\n" +
                "        <div align=\"right\" style=\"margin: 40px;border-top: solid 1px gray\" id=\"bottomTime\">\n" +
                "          <p style=\"margin-right: 20px\">呆橘云签到</p> <label style=\"margin-right: 20px\">"+simpleDateFormat.format(new Date())+"</label>\n" +
                "        </div>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </table>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
    }

    public boolean sendSignSuccessMail(CourseInfo courseInfo,String to){

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, true);
            String template = this.getTemplate(courseInfo);
            mailMessage.setSubject("签到成功通知");
            mailMessage.setText(template,true);
            mailMessage.setFrom("wdy668@vip.qq.com");
            mailMessage.setTo(to);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            sendSignFailureMail(courseInfo, to);
        }
        return true;
    }

    public boolean sendSignFailureMail(CourseInfo courseInfo,String to){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, true);
            String template = this.getTemplate(courseInfo);
            mailMessage.setSubject("签到失败通知");
            mailMessage.setText(template,true);
            mailMessage.setFrom("wdy668@vip.qq.com");
            mailMessage.setTo(to);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            sendSignFailureMail(courseInfo, to);
        }
        return true;
    }
    public boolean sendExceptionMail(String message,String to){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            String template = "\n" +
                    "<body style=\"margin: 0; padding: 0;\">\n" +
                    "  <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\"> 　\n" +
                    "    <tr>\n" +
                    "      <td>\n" +
                    "        <div style=\"margin: 20px;text-align: center;margin-top: 50px\">  </div>\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "      <td>\n" +
                    "        <div style=\"border: #36649d 1px dashed;margin: 30px;padding: 20px\"> <label\n" +
                    "            style=\"font-size: 22px;color: #36649d;font-weight: bold\">用户：管理员</label>\n" +
                    "          <p style=\"font-size: 16px\">异常信息&nbsp;<label style=\"font-weight: bold\"> "+message+"</label> </p>\n" +
                    "          <p style=\"font-size: 16px\">系统于"+simpleDateFormat.format(new Date())+"发生异常！</p>\n" +
                    "          <p style=\"font-size: 22px;color:red;\">请您及时处理错误</p>\n" +
                    "        </div>\n" +
                    "      </td>\n" +
                    "    </tr> 　 <tr>\n" +
                    "      <td>\n" +
                    "        <div style=\"margin: 40px\">\n" +
                    "          <p style=\"font-size: 16px\">呆橘云签到团队</p>\n" +
                    "          <p style=\"color:red;font-size: 14px \">（这是一封自动发送的邮件，请勿回复。）</p>\n" +
                    "        </div>\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "      <td>\n" +
                    "        <div align=\"right\" style=\"margin: 40px;border-top: solid 1px gray\" id=\"bottomTime\">\n" +
                    "          <p style=\"margin-right: 20px\">呆橘云签到</p> <label style=\"margin-right: 20px\">"+simpleDateFormat.format(new Date())+"</label>\n" +
                    "        </div>\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "  </table>\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>";
            mailMessage.setSubject("系统异常通知");
            mailMessage.setText(template,true);
            mailMessage.setFrom("wdy668@vip.qq.com");
            mailMessage.setTo(to);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            sendExceptionMail(message, to);
        }
        return true;
    }
}
