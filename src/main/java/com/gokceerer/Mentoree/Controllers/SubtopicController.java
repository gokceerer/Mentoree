package com.gokceerer.Mentoree.Controllers;

import com.gokceerer.Mentoree.Models.SQL.Subtopic;
import com.gokceerer.Mentoree.Models.SQL.Topic;
import com.gokceerer.Mentoree.Repositories.SQL.MentorRepository;
import com.gokceerer.Mentoree.Repositories.SQL.MentorshipRepository;
import com.gokceerer.Mentoree.Repositories.SQL.SubtopicRepository;
import com.gokceerer.Mentoree.Repositories.SQL.TopicRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SubtopicController {
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;
    private final MentorRepository mentorRepository;
    private final MentorshipRepository mentorshipRepository;

    public SubtopicController(TopicRepository topicRepository,
                           SubtopicRepository subtopicRepository,
                           MentorRepository mentorRepository,
                           MentorshipRepository mentorshipRepository) {
        this.topicRepository = topicRepository;
        this.subtopicRepository = subtopicRepository;
        this.mentorRepository = mentorRepository;
        this.mentorshipRepository = mentorshipRepository;
    }

    @PostMapping("/getSubtopics")
    @ResponseBody
    public List<Subtopic> getSubtopicsByTopic(HttpServletRequest request) {
        String topicid = request.getParameter("topicid");
        Optional<Topic> topic = topicRepository.findById(Integer.parseInt(topicid));
        if (topic.isPresent()) {
            return topic.get().getSubtopics();
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/deleteSubtopic/{id}")
    public String deleteSubtopic(@PathVariable String id){
        Optional<Subtopic> subtopic = subtopicRepository.findById(Integer.parseInt(id));
        if(subtopic.isPresent()){
            if(!mentorshipRepository.existsMentorshipBySubtopic(subtopic.get()) && !mentorRepository.existsMentorBySubtopicAndTopic(subtopic.get(),subtopic.get().getMainTopic())){
                subtopicRepository.deleteById(Integer.parseInt(id));
            }
        }
        return "redirect:/editTopics";
    }


    @PostMapping("/addSubtopic")
    public String addSubtopic(HttpServletRequest request, @RequestParam(name="maintopic") int mainTopicId){
        Subtopic subtopic = new Subtopic();
        subtopic.setName(request.getParameter("name"));
        Optional<Topic> mainTopic = topicRepository.findById(mainTopicId);
        if(mainTopic.isPresent())
            subtopic.setMainTopic(mainTopic.get());

        if(!subtopicRepository.existsSubtopicByNameAndMainTopic(subtopic.getName(),subtopic.getMainTopic()))
            subtopicRepository.save(subtopic);

        return "redirect:/editTopics";
    }



    @PostMapping("/checkSubtopic")
    @ResponseBody
    public String checkSubtopic(HttpServletRequest request){
        Optional<Topic> mainTopic;
        if(!request.getParameter("topicid").equals(""))
            mainTopic = topicRepository.findById(Integer.parseInt(request.getParameter("topicid")));
        else{
            return "";
        }
        if(mainTopic.isPresent()){
            if(subtopicRepository.existsSubtopicByNameAndMainTopic(request.getParameter("subtopicname"), mainTopic.get()))
                return "Var olan bir alt konu ekleyemezsiniz.";
            else{
                return "";
            }
        }
        return "";
    }

    @PostMapping("/deleteSubtopicCheck")
    @ResponseBody
    public String deleteSubtopicCheck(HttpServletRequest request){
        Optional<Subtopic> subtopic = subtopicRepository.findById(Integer.parseInt(request.getParameter("subtopicid")));
        if(subtopic.isPresent()){
            if(mentorshipRepository.existsMentorshipBySubtopic(subtopic.get()) || mentorRepository.existsMentorBySubtopicAndTopic(subtopic.get(),subtopic.get().getMainTopic())){
                return "Bu alt konuya ait mentörlükler mevcuttur. Alt konu silinemez.";
            }
        }
        return "";
    }
}
