package com.gokceerer.Mentoree.Controllers;

import com.gokceerer.Mentoree.Models.NOSQL.MentorNOSQL;
import com.gokceerer.Mentoree.Models.SQL.*;
import com.gokceerer.Mentoree.Repositories.NOSQL.MentorRepositoryNOSQL;
import com.gokceerer.Mentoree.Repositories.SQL.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class AdminController {
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;
    private final MentorRepository mentorRepository;
    private final MentorApplicationRepository mentorApplicationRepository;

    @Autowired
    private MentorRepositoryNOSQL mentorRepositoryNOSQL;

    public AdminController(TopicRepository topicRepository, SubtopicRepository subtopicRepository,
                           MentorRepository mentorRepository, MentorApplicationRepository mentorApplicationRepository)
    {
        this.topicRepository = topicRepository;
        this.subtopicRepository = subtopicRepository;
        this.mentorRepository = mentorRepository;
        this.mentorApplicationRepository = mentorApplicationRepository;
    }
    @GetMapping("/editTopics")
    public ModelAndView editTopics(){
        Iterable<Topic> topics = topicRepository.findAll();
        Iterable<Subtopic> subtopics = subtopicRepository.findAll();
        ModelAndView mv = new ModelAndView("/editTopics");
        mv.addObject("topics",topics);
        mv.addObject("subtopics", subtopics);

        return mv;
    }

    @GetMapping("/acceptApplication/{id}")
    public String acceptApplication(@PathVariable String id){
        Optional<MentorApplication> application = mentorApplicationRepository.findById(Integer.parseInt(id));
        if(application.isPresent()){
            MentorApplication acceptedApplication = application.get();
            acceptedApplication.setState(MentorApplication.ApplicationState.ACCEPTED);
            User mentorUser = acceptedApplication.getUser();
            Topic mentorTopic =acceptedApplication.getTopic();
            Subtopic mentorSubtopic = acceptedApplication.getSubtopic();
            String description = acceptedApplication.getDescription();

            Mentor newMentor = new Mentor();
            mentorUser.setMentor(true);
            newMentor.setUser(mentorUser);
            newMentor.setTopic(mentorTopic);
            newMentor.setSubtopic(mentorSubtopic);
            newMentor.setMenteeCount(0);
            newMentor.setDescription(description);
            MentorNOSQL newMentorNOSQL = new MentorNOSQL(mentorUser.getName(),mentorUser.getSurname(),
                                                        mentorUser.getUsername(),mentorTopic.getName(),
                                                        mentorSubtopic.getName(),description);
            mentorRepositoryNOSQL.save(newMentorNOSQL);
            newMentor.setNosql_id(newMentorNOSQL.get_id());
            mentorRepository.save(newMentor);

        }
        return "redirect:/dashboard/admin";
    }

    @GetMapping("/rejectApplication/{id}")
    public String rejectApplication(@PathVariable String id){
        Optional<MentorApplication> application = mentorApplicationRepository.findById(Integer.parseInt(id));
        if(application.isPresent()){
            MentorApplication acceptedApplication = application.get();
            acceptedApplication.setState(MentorApplication.ApplicationState.REJECTED);
            mentorApplicationRepository.save(acceptedApplication);
        }
        return "redirect:/dashboard/admin";
    }
}
