package com.gokceerer.Mentoree.Controllers;

import com.gokceerer.Mentoree.Models.SQL.Subtopic;
import com.gokceerer.Mentoree.Models.SQL.Topic;
import com.gokceerer.Mentoree.Repositories.SQL.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class TopicController {

    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;
    private final MentorRepository mentorRepository;
    private final MentorshipRepository mentorshipRepository;

    public TopicController(TopicRepository topicRepository,
                          SubtopicRepository subtopicRepository,
                          MentorRepository mentorRepository,
                          MentorshipRepository mentorshipRepository) {
        this.topicRepository = topicRepository;
        this.subtopicRepository = subtopicRepository;
        this.mentorRepository = mentorRepository;
        this.mentorshipRepository = mentorshipRepository;
    }

    @GetMapping("/deleteTopic/{id}")
    public String deleteTopic(@PathVariable String id){
        Iterable<Subtopic> subtopicsToDelete = subtopicRepository.findByMainTopicId(id);
        Optional<Topic> topic = topicRepository.findById(Integer.parseInt(id));
        if(topic.isPresent()){
            if(!mentorshipRepository.existsMentorshipByTopic(topic.get()) && !mentorRepository.existsMentorByTopic(topic.get())){
                for(Subtopic subtopic : subtopicsToDelete){
                    subtopicRepository.deleteById(subtopic.getId());
                }
                topicRepository.deleteById(Integer.parseInt(id));
            }
        }
        return "redirect:/editTopics";
    }

    @PostMapping("/addTopic")
    public String addTopic(HttpServletRequest request){
        Topic topic = new Topic();
        topic.setName(request.getParameter("name").trim());

        if(!topicRepository.existsTopicByName(topic.getName()))
            topicRepository.save(topic);

        return "redirect:/editTopics";
    }

    @PostMapping("/checkTopic")
    @ResponseBody
    public String checkTopic(HttpServletRequest request){
        if(topicRepository.existsTopicByName(request.getParameter("topicname"))){
            return "Var olan bir konu ekleyemezsiniz.";
        }
        return "";
    }

    @PostMapping("/deleteTopicCheck")
    @ResponseBody
    public String deleteTopicCheck(HttpServletRequest request){
        Optional<Topic> topic = topicRepository.findById(Integer.parseInt(request.getParameter("topicid")));
        if(topic.isPresent()){
            if(mentorshipRepository.existsMentorshipByTopic(topic.get())|| mentorRepository.existsMentorByTopic(topic.get())){
                return "Bu konuya ait mentörlükler mevcuttur. Konu silinemez.";
            }
        }
        return "";
    }

}
