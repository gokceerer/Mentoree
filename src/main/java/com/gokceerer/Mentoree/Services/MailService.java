package com.gokceerer.Mentoree.Services;

import com.gokceerer.Mentoree.Models.SQL.Mentorship;
import com.gokceerer.Mentoree.Models.SQL.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class MailService {
    @Autowired
    JavaMailSender mailSender;

    public void sendEmail(String mentorEmail, String menteeEmail, Mentorship mentorship, Phase phase){

            String messageToMentee = "Merhaba " + mentorship.getMentee().getUser().getName() + ",\n";
            messageToMentee += mentorship.getMentor().getUser().getName() + " " + mentorship.getMentor().getUser().getSurname() + " ile devam etmekte olduğun ";
            messageToMentee += mentorship.getTopic().getName() + "/" + mentorship.getSubtopic().getName() + " hakkındaki sürecinin " + phase.getName() + " adlı fazının süresinin bitmesine 1 saat kaldı.\nFazı bitirmek istiyorsanız uygulama üzerinden gerekli işlemleri yapmalısınız.";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mentoree.noreply@gmail.com");
            message.setTo(menteeEmail);
            message.setSubject("Mentörlük İlişkiniz Hakkında");
            message.setText(messageToMentee);
            mailSender.send(message);

            String messageToMentor = "Merhaba " + mentorship.getMentor().getUser().getName() + ",\n";
            messageToMentor += mentorship.getMentee().getUser().getName() + " " + mentorship.getMentee().getUser().getSurname() + " ile devam etmekte olduğun ";
            messageToMentor += mentorship.getTopic().getName() + "/" + mentorship.getSubtopic().getName() + " hakkındaki sürecinin " + phase.getName() + " adlı fazının süresinin bitmesine 1 saat kaldı.\nFazı bitirmek istiyorsanız uygulama üzerinden gerekli işlemleri yapmalısınız.";


            SimpleMailMessage message1 = new SimpleMailMessage();
            message1.setFrom("mentoree.noreply@gmail.com");
            message1.setTo(mentorEmail);
            message1.setSubject("Mentörlük İlişkiniz Hakkında");
            message1.setText(messageToMentor);
            mailSender.send(message1);

    }
}
