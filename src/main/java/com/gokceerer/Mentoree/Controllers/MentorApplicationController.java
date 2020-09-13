package com.gokceerer.Mentoree.Controllers;

import com.gokceerer.Mentoree.Models.SQL.MentorApplication;
import com.gokceerer.Mentoree.Models.SQL.Subtopic;
import com.gokceerer.Mentoree.Models.SQL.Topic;
import com.gokceerer.Mentoree.Models.SQL.User;
import com.gokceerer.Mentoree.Repositories.SQL.*;
import com.gokceerer.Mentoree.Services.UserDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
public class MentorApplicationController {
    private final MentorApplicationRepository mentorApplicationRepository;
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;
    private final UserDetailService userDetailService;

    public MentorApplicationController(MentorApplicationRepository mentorApplicationRepository, TopicRepository topicRepository,
                                       SubtopicRepository subtopicRepository, UserDetailService userDetailService)
    {
        this.mentorApplicationRepository = mentorApplicationRepository;
        this.topicRepository = topicRepository;
        this.subtopicRepository = subtopicRepository;
        this.userDetailService = userDetailService;
    }


    @GetMapping("/mentorApplication")
    public ModelAndView mentorApplication() {
        User u = userDetailService.getLoggedInUser();

        Iterable<Topic> topics = topicRepository.findAll();
        Iterable<Subtopic> subtopics = subtopicRepository.findAll();
        ModelAndView mv = new ModelAndView("/mentorApplication");
        mv.addObject("user", u);
        mv.addObject("topics", topics);
        mv.addObject("subtopics", subtopics);

        return mv;
    }

    @PostMapping("/createApplication")
    public String createApplication(HttpServletRequest request, @RequestParam List<Integer> subtopicCheckbox) {
        User u = userDetailService.getLoggedInUser();

        Optional<Topic> topic = topicRepository.findById(Integer.parseInt(request.getParameter("main-topics")));
        if (topic.isPresent()) {
            for (Integer id : subtopicCheckbox) {
                //TODO:Alt konu yoksa ne olacak?
                Optional<Subtopic> subtopic = subtopicRepository.findById(id);
                if (subtopic.isPresent()) {
                    MentorApplication application = new MentorApplication();
                    application.setTopic(topic.get());
                    application.setSubtopic(subtopic.get());
                    application.setUser(u);
                    application.setState(MentorApplication.ApplicationState.PENDING);
                    application.setDescription(request.getParameter("mentor-description"));
                    if (!mentorApplicationRepository.existsMentorApplicationByUserAndTopicAndSubtopic(u, topic.get(), subtopic.get()))
                        mentorApplicationRepository.save(application);
                }
            }
        }

        return "redirect:/mentorApplication";

    }
}
