package kz.edu.controller;

import kz.edu.dao.QuestionsDAO;
import kz.edu.dao.UserDAO;
import kz.edu.model.Answers;
import kz.edu.model.Questions;
import kz.edu.model.User;
import kz.edu.model.Votes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class Controller2
{
    private final UserDAO userDAO;
    private final QuestionsDAO questionsDAO;
    @Autowired
    public Controller2(QuestionsDAO questionsDAO, UserDAO userDAO)
    { this.questionsDAO = questionsDAO;
        this.userDAO = userDAO;}
    PasswordEncoder passwordEncoder;
    @Autowired
    public void PasswordEncoder(PasswordEncoder passwordEncoder)
    { this.passwordEncoder = passwordEncoder;}
    @RequestMapping(value={"", "/", "home"})
    public String home()
    {
        return "home";
    }
    @GetMapping("/login")
    public String login()
    {
        return "login";
    }
    @GetMapping("/registration")
    public String registration()
    {
        return "registration";
    }
    @PostMapping("/registration")
    public String addUser(User user, @RequestParam("login") String email, Model model)
    {
        System.out.println("REGISTRATION:"+email);

        if (userDAO.findByUserName(email) != null)
        {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        else
        {
            user.setLogin(email);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDAO.addUser(user);
            return "redirect:/login";
        }
    }
    @GetMapping("change_password")
    public String change_password(){
        return "change_password";
    }

    @PostMapping("change_password")
    public String changePassword(@RequestParam("password") String password, @RequestParam("age") int age, @RequestParam("interest") String interest, @RequestParam("first_name") String first_name, @RequestParam("last_name") String last_name, @RequestParam("group_name") String group_name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userDAO.findByUserName(currentPrincipalName);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirst_name(first_name);
        user.setGroup_name(group_name);
        user.setInterest(interest);
        user.setLast_name(last_name);
        user.setAge(age);
        userDAO.change_password(user);
        return "redirect:/home";
    }

    @GetMapping("profile")
    public String get_profile(Model model ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userDAO.findByUserName(currentPrincipalName);
        model.addAttribute("user", user);
        Votes vote = userDAO.getLastVote(user);
        if (vote != null){
            Questions questions = questionsDAO.find_question_with_id(vote.getQ_id());
            Answers answers = questionsDAO.find_answer_with_id(vote.getA_id());
            model.addAttribute("quest", questions);
            model.addAttribute("ans", answers);
        }else{
            Questions questions = new Questions();
            questions.setQuestions("no");
            Answers answers = new Answers();
            answers.setAnswer("no");
            model.addAttribute("quest", questions);
            model.addAttribute("ans", answers);
        }
        return "profile";
    }

    @GetMapping("change_role")
    public String change_role(){
        return "change_role";
    }

    @PostMapping("change_role")
    public String change(@RequestParam("name") String name, @RequestParam("login") String login, Model model){
        User user = userDAO.findByUserName(login);
        String message = "";
        if (user == null){
            message = "No such user";
            model.addAttribute("message", message);
            return "change_role";
        }else {
            userDAO.editRole(user, name);
            return "redirect:/home";
        }
    }
}