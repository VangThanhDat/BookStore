package com.poly.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.poly.model.MailModel;

@Service
public class MailService {
    @Autowired
    JavaMailSender sender;

    List<MimeMessage> queue = new ArrayList<>();

    @Scheduled(fixedDelay = 1000)
    public void run() {
        int success = 0, error = 0;
        while (!queue.isEmpty()) {
            MimeMessage message = queue.remove(0);
            try {
                sender.send(message);
                success++;
            } catch (Exception e) {
                error++;
                // TODO: handle exception
            }
            System.out.println("Success: " + success + " Error: " + error);
        }

    }

    public void sendMailWelcome(String emailUser, String nameUser) {
        MailModel mail = new MailModel();
        mail.setTo(emailUser);
        mail.setSubject("Thư chào mừng");
        mail.setBody("<h1>Chúc mừng!</h1>\r\n"
                + "    <p>Xin chào, " + nameUser + "</p>\r\n"
                + "    <p>Cảm ơn bạn đã đăng ký thành công tài khoản trên website của chúng tôi. Bây giờ bạn có thể truy cập và sử dụng các tính năng của chúng tôi.</p>\r\n"
                + "    <p>Hãy khám phá và tận hưởng trải nghiệm tuyệt vời trên website của chúng tôi!</p>\r\n"
                + "    <p>Trân trọng,</p>\r\n"
                + "    <p>Đội ngũ quản trị website</p>");
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getBody(), true);
            helper.setReplyTo(mail.getFrom());
            for (String email : mail.getCc()) {
                helper.addCc(email);
            }

            for (String email : mail.getBcc()) {
                helper.addBcc(email);
            }

        } catch (MessagingException | IllegalStateException ex) {
            ex.printStackTrace();
            System.out.println("message: Gửi mail thất bại");
        }
        queue.add(message);
        System.out.println("message: Gửi mail thành công");
    }

    public void sendMailChangeInfo(String emailUser, String nameUser) {
        MailModel mail = new MailModel();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String formattedDateTime = sdf.format(date);

        System.out.println("Chuỗi kết hợp ngày giờ: " + formattedDateTime);
        String emailBody = "<!DOCTYPE html>\r\n" +
                "<html lang=\"en\">\r\n" +
                "<head>\r\n" +
                "    <meta charset=\"UTF-8\">\r\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" +
                "    <title>Thông báo thay đổi thông tin cá nhân</title>\r\n" +
                "    <style>\r\n" +
                "        body {\r\n" +
                "            font-family: Arial, sans-serif;\r\n" +
                "            background-color: #f2f2f2;\r\n" +
                "            margin: 0;\r\n" +
                "            padding: 0;\r\n" +
                "        }\r\n" +
                "        .container {\r\n" +
                "            max-width: 600px;\r\n" +
                "            margin: 0 auto;\r\n" +
                "            padding: 20px;\r\n" +
                "            background-color: #ffffff;\r\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\r\n" +
                "            border-radius: 10px;\r\n" +
                "        }\r\n" +
                "        .header {\r\n" +
                "            background-color: #007bff;\r\n" +
                "            color: #ffffff;\r\n" +
                "            padding: 10px;\r\n" +
                "            text-align: center;\r\n" +
                "            border-top-left-radius: 10px;\r\n" +
                "            border-top-right-radius: 10px;\r\n" +
                "        }\r\n" +
                "        .content {\r\n" +
                "            padding: 20px;\r\n" +
                "        }\r\n" +
                "        .footer {\r\n" +
                "            background-color: #f2f2f2;\r\n" +
                "            padding: 10px;\r\n" +
                "            text-align: center;\r\n" +
                "            border-bottom-left-radius: 10px;\r\n" +
                "            border-bottom-right-radius: 10px;\r\n" +
                "        }\r\n" +
                "        h1, p {\r\n" +
                "            margin: 10px 0;\r\n" +
                "        }\r\n" +
                "        p {\r\n" +
                "            line-height: 1.6;\r\n" +
                "        }\r\n" +
                "    </style>\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "    <div class=\"container\">\r\n" +
                "        <div class=\"header\">\r\n" +
                "            <h1>Thông báo thay đổi thông tin cá nhân</h1>\r\n" +
                "        </div>\r\n" +
                "        <div class=\"content\">\r\n" +
                "            <p>Xin chào, " + nameUser + "</p>\r\n" +
                "            <p>Chúng tôi nhận thấy có sự thay đổi trong thông tin cá nhân của bạn trên hệ thống của chúng tôi vào lúc <strong>"
                + formattedDateTime + "<strong/>.</p>\r\n"
                +
                "            <p>Nếu đây là hành động của bạn, xin hãy bỏ qua email này.</p>\r\n" +
                "            <p>Nếu bạn không thực hiện thay đổi này, vui lòng liên hệ với chúng tôi ngay để kiểm tra và xác nhận tài khoản của bạn.</p>\r\n"
                +
                "            <p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>\r\n" +
                "            <p>Trân trọng,</p>\r\n" +
                "            <p>Đội ngũ hỗ trợ</p>\r\n" +
                "        </div>\r\n" +
                "        <div class=\"footer\">\r\n" +
                "            <p>DAT-CHAN BOOK STORE</p>\r\n" +
                "            <p>bookstore.datchan@gmail.com</p>\r\n" +
                "        </div>\r\n" +
                "    </div>\r\n" +
                "</body>\r\n" +
                "</html>";

        mail.setTo(emailUser);
        mail.setSubject("Thông báo thay đổi thông tin cá nhân");
        mail.setBody(emailBody);
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getBody(), true);
            helper.setReplyTo(mail.getFrom());
            for (String email : mail.getCc()) {
                helper.addCc(email);
            }

            for (String email : mail.getBcc()) {
                helper.addBcc(email);
            }

        } catch (MessagingException | IllegalStateException ex) {
            ex.printStackTrace();
            System.out.println("message: Gửi mail thất bại");
        }
        queue.add(message);
        System.out.println("message: Gửi mail thành công");
    }

    public void sendMailOTP(String emailUser, String OTP) {
        MailModel mail = new MailModel();
        mail.setTo(emailUser);
        mail.setSubject("Mã xác nhận");
        mail.setBody("<!DOCTYPE html>\r\n"
                + "<html lang=\"en\">\r\n"
                + "<head>\r\n"
                + "    <meta charset=\"UTF-8\">\r\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                + "    <title>OTP</title>\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "    <h1>Mã OTP của bạn</h1>\r\n"
                + "    <p>Xin chào,</p>\r\n"
                + "    <p>Dưới đây là mã OTP của bạn:</p>\r\n"
                + "    <h3 style=\" color: cyan; width: fit-content;\"  >" + OTP + "</h3>\r\n"
                + "    <p>Vui lòng sử dụng mã này để xác nhận đổi mật khẩu. <span style=\"color: red;\">Lưu ý mã có thời hạn trong 60 giây, không chia sẽ mã cho bất kì ai!</span></p>\r\n"
                + "    <p>Trân trọng,</p>\r\n"
                + "    <p>Đội ngũ hỗ trợ</p>\r\n"
                + "</body>\r\n"
                + "</html>");
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getBody(), true);
            helper.setReplyTo(mail.getFrom());
            for (String email : mail.getCc()) {
                helper.addCc(email);
            }

            for (String email : mail.getBcc()) {
                helper.addBcc(email);
            }

        } catch (MessagingException | IllegalStateException ex) {
            ex.printStackTrace();
            System.out.println("message: Gửi mail thất bại");
        }
        queue.add(message);
        System.out.println("message: Gửi mail thành công");
    }

    public static String maskEmail(String email) {
        StringBuilder maskedEmailBuilder = new StringBuilder(email);
        int atIndex = email.indexOf("@");

        if (atIndex >= 3) {
            for (int i = 3; i < atIndex; i++) {
                maskedEmailBuilder.setCharAt(i, '*');
            }
        }

        return maskedEmailBuilder.toString();
    }

    public String generateOTP(int length) {
        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Tạo số ngẫu nhiên từ 0 đến 9
            otpBuilder.append(digit); // Thêm số vào chuỗi OTP
        }
        System.out.println(otpBuilder.toString());
        return otpBuilder.toString();
    }

    public void sendMailPay(String emailUser, String nameUser){
        MailModel mail = new MailModel();
        mail.setTo(emailUser);
        mail.setSubject("Đặt hàng thành công");
        mail.setBody("<h1>Cảm ơn quý khách!</h1>\r\n"
            + "         <p>Xin chào, " +nameUser+"</p>\r\n"
            + "         <p>Cảm ơn quý khách đã đặt hàng tại BookStore chúng tôi</p>\r\n"
            + "         <p>Hẹn gặp lại quý khách vào lần tới <3</p>\r\n"
            + "         <p>Trân trọng cảm ơn quý khách</p>");
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getBody(), true);
            helper.setReplyTo(mail.getFrom());
            for (String email : mail.getCc()) {
                helper.addCc(email);
            }

            for (String email : mail.getBcc()) {
                helper.addBcc(email);
            }

        } catch (MessagingException | IllegalStateException ex) {
            ex.printStackTrace();
            System.out.println("message: Gửi mail thất bại");
        }
        queue.add(message);
        System.out.println("message: Gửi mail thành công");
    }
}
