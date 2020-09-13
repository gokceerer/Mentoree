package com.gokceerer.Mentoree.Controllers;

import com.gokceerer.Mentoree.Models.NOSQL.MentorNOSQL;
import com.gokceerer.Mentoree.Models.SQL.*;
import com.gokceerer.Mentoree.Repositories.NOSQL.MentorRepositoryNOSQL;
import com.gokceerer.Mentoree.Repositories.SQL.*;
import com.gokceerer.Mentoree.Services.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
public class UserController {
    private final MentorApplicationRepository mentorApplicationRepository;
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;
    private final UserDetailService userDetailService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MentorRepositoryNOSQL mentorRepositoryNOSQL;

    public UserController(MentorApplicationRepository mentorApplicationRepository, TopicRepository topicRepository,
                          SubtopicRepository subtopicRepository, UserDetailService userDetailService)
    {
        this.mentorApplicationRepository = mentorApplicationRepository;
        this.topicRepository = topicRepository;
        this.subtopicRepository = subtopicRepository;
        this.userDetailService = userDetailService;
    }

    @GetMapping("/dashboard/{userRole}")
    public ModelAndView showDashboard(@PathVariable String userRole) {
        User u = userDetailService.getLoggedInUser();

        if (userRole.equals("admin")) {
            Iterable<MentorApplication> applications = mentorApplicationRepository.findAll();
            ModelAndView mv = new ModelAndView("/dashboard");
            mv.addObject("user", u);
            mv.addObject("applications", applications);
            return mv;
        } else {
            return new ModelAndView("/dashboard", "user", u);
        }
    }

    @GetMapping("/mentorSearch")
    public ModelAndView mentorSearch() {
        ModelAndView mv = new ModelAndView("/mentorSearch");
        String username = userDetailService.getLoggedInUser().getUsername();
        List<MentorNOSQL> result = mongoTemplate.findAll(MentorNOSQL.class);
        result.removeIf(mentor -> mentor.getUsername().equals(username));

        Iterable<Topic> topics = topicRepository.findAll();
        Iterable<Subtopic> subtopics = subtopicRepository.findAll();
        mv.addObject("mentors", result);
        mv.addObject("topics", topics);
        mv.addObject("subtopics", subtopics);

        return mv;
    }

    @GetMapping("/searchResultsByText")
    @ResponseBody
    public Iterable<MentorNOSQL> searchResultsByText(@RequestParam String textQuery) {
        String username = userDetailService.getLoggedInUser().getUsername();
        List<MentorNOSQL> result;

        if (!textQuery.equals("")) {
            TextQuery query = TextQuery.queryText(TextCriteria.forLanguage("tr").caseSensitive(false).matchingPhrase(textQuery));
            result = mongoTemplate.find(query, MentorNOSQL.class, "Mentor");
        } else {
            result = mongoTemplate.findAll(MentorNOSQL.class);
        }
        result.removeIf(mentor -> mentor.getUsername().equals(username));

        return result;
    }

    @GetMapping("/searchResultsByTopic")
    @ResponseBody
    public Iterable<MentorNOSQL> searchResultsByTopic(HttpServletRequest request) {
        String username = userDetailService.getLoggedInUser().getUsername();

        int topicId = Integer.parseInt(request.getParameter("topicid"));
        List<MentorNOSQL> result;

        if (topicId == -1) {
            result = mongoTemplate.findAll(MentorNOSQL.class);
        } else {
            Optional<Topic> topic = topicRepository.findById(topicId);
            if (topic.isPresent()) {
                result = mentorRepositoryNOSQL.findAllByTopic(topic.get().getName());
            } else {
                result = mongoTemplate.findAll(MentorNOSQL.class);
            }
        }
        result.removeIf(mentor -> mentor.getUsername().equals(username));

        return result;
    }

    @GetMapping("/searchResultsBySubtopic")
    @ResponseBody
    public Iterable<MentorNOSQL> searchResultsBySubtopic(HttpServletRequest request) {
        String username = userDetailService.getLoggedInUser().getUsername();
        List<MentorNOSQL> result = null;

        int subtopicId = Integer.parseInt(request.getParameter("subtopicid"));
        int topicId = Integer.parseInt(request.getParameter("topicid"));
        if (subtopicId == -1) {
            Optional<Topic> mainTopic = topicRepository.findById(topicId);
            if (mainTopic.isPresent()) {
                result = mentorRepositoryNOSQL.findAllByTopic(mainTopic.get().getName());
            }
        }
        else {
            Optional<Subtopic> subtopic = subtopicRepository.findById(subtopicId);
            if (subtopic.isPresent()) {
                result = mentorRepositoryNOSQL.findAllBySubtopic(subtopic.get().getName());
            } else {
                result = mongoTemplate.findAll(MentorNOSQL.class);
            }
        }
        assert result != null;
        result.removeIf(mentor -> mentor.getUsername().equals(username));

        return result;
    }











}
