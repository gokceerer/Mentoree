package com.gokceerer.Mentoree.Controllers;

import com.gokceerer.Mentoree.Models.SQL.Subtopic;
import com.gokceerer.Mentoree.Models.SQL.Topic;
import com.gokceerer.Mentoree.Models.SQL.User;
import com.gokceerer.Mentoree.Repositories.SQL.*;
import com.gokceerer.Mentoree.Services.UserDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MentorController {
    private final MentorApplicationRepository mentorApplicationRepository;
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;
    private final MentorRepository mentorRepository;
    private final UserDetailService userDetailService;

    public MentorController(MentorApplicationRepository mentorApplicationRepository,
                           TopicRepository topicRepository,
                           SubtopicRepository subtopicRepository,
                           MentorRepository mentorRepository,
                            UserDetailService userDetailService)
    {
        this.mentorApplicationRepository = mentorApplicationRepository;
        this.topicRepository = topicRepository;
        this.subtopicRepository = subtopicRepository;
        this.mentorRepository = mentorRepository;
        this.userDetailService = userDetailService;

    }


    @PostMapping("/checkIfMentoringExists")
    @ResponseBody
    public String checkIfMentoringExists(HttpServletRequest request) {
        User u = userDetailService.getLoggedInUser();
        Optional<Topic> topic = topicRepository.findById(Integer.parseInt(request.getParameter("topicid")));
        List<Optional<Subtopic>> subtopicList = new ArrayList<>();

        String subtopic_ids = request.getParameter("subtopicid").replace("[", "");
        subtopic_ids = subtopic_ids.replace("]", "");
        subtopic_ids = subtopic_ids.replace("\"", "");
        subtopic_ids = subtopic_ids.replace(",", " ");
        String[] subtopicIds = subtopic_ids.split(" ");

        for (String id : subtopicIds) {
            subtopicList.add(subtopicRepository.findById(Integer.parseInt(id)));
        }

        for (Optional<Subtopic> subtopic : subtopicList) {
            if (subtopic.isPresent() && topic.isPresent()) {
                if (mentorRepository.existsMentorByUserAndTopicAndSubtopic(u, topic.get(), subtopic.get())) {
                    return "Var olan bir mentörlüğünüz için tekrar başvuru yapamazsınız";
                } else if (mentorApplicationRepository.existsMentorApplicationByUserAndTopicAndSubtopic(u, topic.get(), subtopic.get())) {
                    return "Var olan bir mentörlük başvurusunu tekrarlayamazsınız";
                }
            }
        }
        return "";
    }
}
