package com.gokceerer.Mentoree.Controllers;

import com.gokceerer.Mentoree.Models.SQL.*;
import com.gokceerer.Mentoree.Repositories.SQL.*;
import com.gokceerer.Mentoree.Services.MailService;
import com.gokceerer.Mentoree.Services.UserDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MentorshipController {
    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;
    private final MentorshipRepository mentorshipRepository;
    private final PhaseRepository phaseRepository;
    private final UserDetailService userDetailService;
    private final MailService mailService;
    public MentorshipController(MentorRepository mentorRepository, MenteeRepository menteeRepository,
                              MentorshipRepository mentorshipRepository, PhaseRepository phaseRepository, UserDetailService userDetailService, MailService mailService) {
        this.mentorRepository = mentorRepository;
        this.menteeRepository = menteeRepository;
        this.mentorshipRepository = mentorshipRepository;
        this.phaseRepository = phaseRepository;
        this.userDetailService = userDetailService;
        this.mailService = mailService;
    }

    @PostMapping("/createMentorship")
    @ResponseBody
    public String createMentorship(HttpServletRequest request) {
        User u = userDetailService.getLoggedInUser();
        String mentorNOSQLId = request.getParameter("mentorNOSQLId");
        Mentor mentor = mentorRepository.findByNoSqlId(mentorNOSQLId);

        int totalMenteeCount = 0;
        for (Mentor m : mentor.getUser().getMentoring()) {
            for(Mentorship mentorship : m.getMentorships()){
                if(!mentorship.isFinished()){
                    totalMenteeCount += 1;
                }
            }
        }
        if (totalMenteeCount >= 2) {
            return "Mentör birlikte çalışabileceği mentee limitine ulaşmıştır.";
        } else {
            mentor.setMenteeCount(mentor.getMenteeCount() + 1);
        }

        Mentee mentee;
        if (!menteeRepository.existsByUser(u)) {
            mentee = new Mentee();
            u.setMentee(true);
            mentee.setUser(u);
            menteeRepository.save(mentee);
        } else {
            mentee = menteeRepository.findByUser(u);
        }
        if (mentorshipRepository.existsMentorshipByMenteeAndTopicAndFinished(mentee, mentor.getTopic(),false)) {
            return "Seçtiğiniz mentörün ana konusuyla devam etmekte olan bir mentörlük ilişkiniz bulunmaktadır.";
        }
        Mentorship m = mentorshipRepository.findByMentorAndMenteeAndTopicAndSubtopic(mentor, mentee, mentor.getTopic(), mentor.getSubtopic());
        if(m != null && !m.isFinished()){
            return "Seçtiğiniz mentörle aynı konu ve alt konu üstüne devam etmekte olan bir süreciniz bulunmaktadır.";
        }

        Mentorship newMentorship = new Mentorship();
        newMentorship.setMentee(mentee);
        newMentorship.setMentor(mentor);
        newMentorship.setTopic(mentor.getTopic());
        newMentorship.setSubtopic(mentor.getSubtopic());
        newMentorship.setStartDate(new java.util.Date());
        mentorshipRepository.save(newMentorship);

        return "Mentörlük ilişkisi oluşturuldu.";
    }

    @GetMapping("/mentorshipDetail/{id}")
    public ModelAndView mentorshipDetail(@PathVariable String id, HttpServletResponse response) {
        ModelAndView mv = null;
        Optional<Mentorship> mentorship = mentorshipRepository.findById(Integer.parseInt(id));
        if (mentorship.isPresent()) {
            User user = userDetailService.getLoggedInUser();
            if(mentorship.get().getMentor().getUser() == user || mentorship.get().getMentee().getUser() == user){
                mv = new ModelAndView("/mentorshipDetail");
                mv.addObject("mentorship", mentorship.get());
                mv.addObject("mentor", mentorship.get().getMentor().getUser());
                mv.addObject("mentee", mentorship.get().getMentee().getUser());
                mv.addObject("user", user);
            }
            else{
                response.setStatus(403);
            }
        }
        return mv;

    }

    @PostMapping("/createPhases")
    public String createPhases(HttpServletRequest request) throws ParseException {
        Enumeration<String> inputs = request.getParameterNames();
        String mentorshipId = request.getParameter(inputs.nextElement());
        while(inputs.hasMoreElements()){
            Phase p = new Phase();
            p.setName(request.getParameter(inputs.nextElement()));
            SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy");
            Date endTime = sdt.parse(request.getParameter(inputs.nextElement()));

            Calendar cal = Calendar.getInstance();
            cal.setTime(endTime);
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(request.getParameter(inputs.nextElement())));
            cal.set(Calendar.MINUTE, Integer.parseInt(request.getParameter(inputs.nextElement())));
            cal.set(Calendar.SECOND, 0);

            p.setEndTime(cal.getTime());
            p.setStatus("Başlamadı");
            p.setMentorship(mentorshipRepository.findById(Integer.parseInt(mentorshipId)).get());
            phaseRepository.save(p);
            Timer t = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Optional<Phase> phase = phaseRepository.findById(p.getId());
                    if(phase.isPresent() && phase.get().getStatus().equals("Devam ediyor")){
                        String mentorMail = p.getMentorship().getMentor().getUser().getUsername();
                        String menteeMail = p.getMentorship().getMentee().getUser().getUsername();
                        mailService.sendEmail(mentorMail,menteeMail,p.getMentorship(),p);
                    }
                }
            };
            Calendar timercal = Calendar.getInstance();
            timercal.setTime(p.getEndTime());
            timercal.add(Calendar.HOUR_OF_DAY, -1);
            t.schedule(task, timercal.getTime());
        }
        return "redirect:/mentorshipDetail/" + mentorshipId;
    }

    @PostMapping("/startPhases")
    @ResponseBody
    public String startPhases(HttpServletRequest request){
        Optional<Mentorship> mentorship = mentorshipRepository.findById(Integer.parseInt(request.getParameter("mentorshipId")));
        if(mentorship.isPresent()){
            if(mentorship.get().getMentorshipPhases().isEmpty()){
                return "Faz tanımlamadan süreci başlatamazsınız.";
            }
            else{
                if(mentorship.get().getCurrentPhase() == null){
                    mentorship.get().getMentorshipPhases().get(0).setStatus("Devam ediyor");
                    mentorship.get().setCurrentPhase(mentorship.get().getMentorshipPhases().get(0).getName());

                    mentorship.get().getMentorshipPhases().get(0).setStartTime(new java.util.Date());

                    mentorshipRepository.save(mentorship.get());
                    phaseRepository.save(mentorship.get().getMentorshipPhases().get(0));
                    return "";
                }
                else{
                    return "Başlamış bir süreci tekrar başlatamazsınız.";
                }
            }
        }
        return "Mentörlük bulunamadı";
    }

    @PostMapping("/finishPhase")
    public ModelAndView finishPhase(HttpServletRequest request){
        String mentorshipId = request.getParameter("mentorshipId");
        ModelAndView mv = new ModelAndView("redirect:/mentorshipDetail/" + mentorshipId);
        String phaseId = request.getParameter("phaseId");
        Optional<Phase> phase = phaseRepository.findById(Integer.parseInt(phaseId));
        if(phase.isPresent()){
            phase.get().setStatus("Bitti");
            phaseRepository.save(phase.get());
            Optional<Mentorship> mentorship = mentorshipRepository.findById(Integer.parseInt(mentorshipId));
            if(mentorship.isPresent()){
                boolean allDone = true;

                for(Phase p: mentorship.get().getMentorshipPhases()){
                    if(p.getStatus().equals("Başlamadı")){
                        allDone = false;
                        p.setStatus("Devam ediyor");
                        p.setStartTime(new java.util.Date());
                        mentorship.get().setCurrentPhase(p.getName());
                        mentorshipRepository.save(mentorship.get());
                        break;
                    }
                }

                if(allDone){
                    mentorship.get().setFinished(true);
                    mentorship.get().setCurrentPhase("Süreç Tamamlandı");
                    mentorshipRepository.save(mentorship.get());
                    mentorship.get().getMentor().setMenteeCount( mentorship.get().getMentor().getMenteeCount() - 1);
                    mentorRepository.save(mentorship.get().getMentor());

                }

            }

        }
        return mv;
    }

    @PostMapping("/createEvaluationOnPhase")
    public String createEvaluationOnPhase(HttpServletRequest request){

        String mentorshipId = request.getParameter("mentorshipId");
        Integer phaseId = Integer.parseInt(request.getParameter("phaseId"));
        Integer rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("evaluation-text");
        System.out.println(comment);
        Optional<Phase> p = phaseRepository.findById(phaseId);
        Optional<Mentorship> m = mentorshipRepository.findById(Integer.parseInt(mentorshipId));

        if(p.isPresent()){
            if(m.isPresent()){
                User user = userDetailService.getLoggedInUser();
                if(m.get().getMentor().getUser() == user){
                    p.get().setMentorRating(rating);
                    p.get().setMentorEvaluation(comment);
                    p.get().setMentorEvaluated(true);
                }
                else if(m.get().getMentee().getUser() == user){
                    p.get().setMenteeRating(rating);
                    p.get().setMenteeEvaluation(comment);
                    p.get().setMenteeEvaluated(true);
                }
                phaseRepository.save(p.get());
            }
        }
        return "redirect:mentorshipDetail/" + mentorshipId;
    }


}
