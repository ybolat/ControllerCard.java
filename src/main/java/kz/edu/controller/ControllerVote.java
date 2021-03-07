package kz.edu.controller;

import kz.edu.dao.QuestionsDAO;
import kz.edu.dao.UserDAO;
import kz.edu.model.Answers;
import kz.edu.model.Questions;
import kz.edu.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/question")
public class ControllerVote {

    private final QuestionsDAO questionsDAO;
    private final UserDAO userDAO;
    @Autowired
    public ControllerVote(QuestionsDAO questionsDAO, UserDAO userDAO)
    { this.questionsDAO = questionsDAO;
        this.userDAO = userDAO;}

    @GetMapping()
    public String welcome(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        model.addAttribute("questions", questionsDAO.getQuestionsList());
        model.addAttribute("answers", questionsDAO);
        model.addAttribute("votes", questionsDAO.getVotes());
        return "page-1";
    }

    @PostMapping("/vote")
    public String vote(@RequestParam("id") int id, @RequestParam("question_id") int q_id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userDAO.findByUserName(currentPrincipalName);
        model.addAttribute("message", questionsDAO.vote(user.getId(), q_id, id));
        model.addAttribute("stat", questionsDAO.getStatistic(questionsDAO.find_question_with_id(q_id),
                questionsDAO.find_answer_with_id(id)));
        return "vote";
    }

    @GetMapping("/create")
    public String create(Model model){
        return "create";
    }

    @PostMapping("/create")
    public String createQuestion(@RequestParam("question") String question, @RequestParam("answer") String answer, @RequestParam("answer1") String answer1, @RequestParam("answer2") String answer2){
        Questions questions = new Questions();
        questions.setQuestions(question);
        questionsDAO.addQuestion(questions);
        int q_id = questionsDAO.find_question(question).getQ_id();
        Answers answers1 = new Answers();
        answers1.setAnswer(answer);
        answers1.setQ_id(q_id);
        questionsDAO.addAnswers(answers1);
        Answers answers2 = new Answers();
        answers2.setAnswer(answer1);
        answers2.setQ_id(q_id);
        questionsDAO.addAnswers(answers2);
        Answers answers3 = new Answers();
        answers3.setAnswer(answer2);
        answers3.setQ_id(q_id);
        questionsDAO.addAnswers(answers3);
        return "home";
    }
    @GetMapping("/change")
    public String change(Model model , @RequestParam("quest") String question, @RequestParam("ans") String answer, @RequestParam("ans1") String answer1, @RequestParam("ans2") String answer2){
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        model.addAttribute("answer1", answer1);
        model.addAttribute("answer2", answer2);
        return "change";
    }

    @PostMapping("/change")
    public String change_Q(@RequestParam("question") String question, @RequestParam("q_id") int q_id, @RequestParam("answer") String answer, @RequestParam("a_id") int a_id, @RequestParam("a_id1") int a_id1, @RequestParam("a_id2") int a_id2, @RequestParam("answer1") String answer1, @RequestParam("answer2") String answer2){
        questionsDAO.change_Question(question, q_id);
        questionsDAO.change_Answer(answer, a_id);
        questionsDAO.change_Answer(answer1, a_id1);
        questionsDAO.change_Answer(answer2, a_id2);
        return "home";
    }

    @PostMapping("/delete")
    public String deleteQ(@RequestParam("q_id") int q_id){
        questionsDAO.delete_Question(q_id);
        return "home";
    }

    @GetMapping("/fullStatistic")
    public String fullStatistic(Model model){
        List<User> usersList = userDAO.getAllUsers();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userDAO.findByUserName(currentPrincipalName);
        HashMap<Answers, Integer> statistic = questionsDAO.getFullStatistic(usersList, user);
        model.addAttribute("statistic", statistic);
        return "fullStatistic";
    }
}
