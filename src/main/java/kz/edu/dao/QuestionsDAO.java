package kz.edu.dao;


import kz.edu.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QuestionsDAO {

    private final SessionFactory sessionFactory;
    List<Questions> questionsList;
    List<Votes> votesList;
    List<Answers> answersList;

    @Autowired
    public QuestionsDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Questions> getQuestionsList() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Questions> criteria = builder.createQuery(Questions.class);
            Root<Questions> root = criteria.from(Questions.class);

            criteria.select(root);
            Query<Questions> query = session.createQuery(criteria);
            this.questionsList = query.getResultList();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return questionsList;
    }

    public List<Answers> getAnswers(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Answers> criteria = builder.createQuery(Answers.class);
            Root<Answers> root = criteria.from(Answers.class);
            criteria.select(root);
            Query<Answers> query = session.createQuery(criteria);
            this.answersList = query.getResultList();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return answersList;
    }

    public List<Votes> getVotes(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Votes> criteria = builder.createQuery(Votes.class);
            Root<Votes> root = criteria.from(Votes.class);
            criteria.select(root);
            Query<Votes> query = session.createQuery(criteria);
            this.votesList = query.getResultList();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return votesList;
    }

    public String vote(int u_id, int q_id, int a_id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String message = "";
        try {
            List<Votes> votes = getVotes();
            for (int i = 0; i < votes.size(); i++) {
                if (votes.get(i).getUser_id() == u_id && votes.get(i).getQ_id() == q_id) {
                    message = "You already answered to this questions!";
                    break;
                }
            }
            if (message == "") {
                Votes vote = new Votes();
                vote.setQ_id(q_id);
                vote.setA_id(a_id);
                vote.setUser_id(u_id);
                session.merge(vote);
                session.getTransaction().commit();
                message = "Successfully answered to the question!";
            }
        } finally {
            session.close();
            if(message == "Successfully answered to the question!"){
                Answers answers = find_answer_with_id(a_id);
                answer(answers);
            }
        }
        return message;
    }

    public String addQuestion(Questions questions){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String message = "";
        try{
            session.merge(questions);
            message = "Added!";
            session.getTransaction().commit();
        }
        finally
        {
            session.close();
        }
        return message;
    }

    public String addAnswers(Answers answers){
        String message = "";
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            session.merge(answers);
            message = "Added!";
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return message;
    }

    public void answer(Answers answers){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            answers.setCounter(answers.getCounter() + 1);
            session.merge(answers);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public Answers find_answer_with_id(int id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Answers answer;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Answers> query = builder.createQuery(Answers.class);
            Root<Answers> root = query.from(Answers.class);

            Predicate predicate = builder.equal(root.get("a_id"), id);
            answer = session.createQuery(query.where(predicate)).getSingleResult();
        }
        catch (NoResultException noResultException) {
            answer = null;
        }
        finally{
            session.close();
        }
        return answer;
    }

    public Questions find_question_with_id(int id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Questions questions;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Questions> query = builder.createQuery(Questions.class);
            Root<Questions> root = query.from(Questions.class);

            Predicate predicate = builder.equal(root.get("q_id"), id);
            questions = session.createQuery(query.where(predicate)).getSingleResult();
        }
        catch (NoResultException noResultException) {
            questions = null;
        }
        finally{
            session.close();
        }
        return questions;
    }

    public String change_Answer(String answers, int id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Answers answer = find_answer_with_id(id);
        String message = "";
        try {
            answer.setAnswer(answers);
            session.merge(answer);
            message = "Changed";
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return message;
    }

    public Questions find_question(String quest_ions){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Questions questions;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Questions> query = builder.createQuery(Questions.class);
            Root<Questions> root = query.from(Questions.class);

            Predicate predicate = builder.equal(root.get("questions"), quest_ions);
            questions = session.createQuery(query.where(predicate)).getSingleResult();
        }
        catch (NoResultException noResultException) {
            questions = null;
        }
        finally{
            session.close();
        }
        return questions;
    }

    public String change_Question(String question, int id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Questions questions = find_question_with_id(id);
        String message = "";
        try {
            questions.setQuestions(question);
            session.merge(questions);
            message = "Changed";
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return message;
    }

    public void delete_Answers(int id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Answers> answersList;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Answers> query = builder.createQuery(Answers.class);
            Root<Answers> root = query.from(Answers.class);

            Predicate predicate = builder.equal(root.get("q_id"), id);
            answersList = session.createQuery(query.where(predicate)).getResultList();
            for (int i = 0; i < answersList.size(); i++){
                session.delete(answersList.get(i));
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public String delete_Question(int id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String message = "";
        try {
            deleteVote(id);
            delete_Answers(id);
            Questions questions = find_question_with_id(id);
            session.delete(questions);
            message = "Deleted";
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return message;
    }

    public List<Answers> find_answer_to_question(Questions questions){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Answers> answersList;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Answers> query = builder.createQuery(Answers.class);
            Root<Answers> root = query.from(Answers.class);

            Predicate predicate = builder.equal(root.get("q_id"), questions.getQ_id());
            answersList = session.createQuery(query.where(predicate)).getResultList();

        } catch (NoResultException noResultException) {
            answersList = null;
        }finally {
            session.close();
        }
        return answersList;
    }

    public HashMap<Answers, Integer> getFullStatistic(List<User> userList, User user){
        HashMap<Answers, Integer> statistic = new HashMap<>();
        List<Questions> questionsList = getQuestionsList();
        List<Votes> votesList = getVotes();
        try {
            for(int i = 0; i < questionsList.size(); i++){
                List<Answers> answersList = find_answer_to_question(questionsList.get(i));
                for (int j = 0; j < answersList.size(); j++){
                    int counter = 0;
                    for (int q = 0; q < votesList.size(); q++){
                        if(votesList.get(q).getA_id() == answersList.get(j).getA_id()){
                            for (int w = 0; w < userList.size(); w++){
                                if((user.getAge() == userList.get(w).getAge()) ||
                                        (user.getGroup_name() == userList.get(w).getGroup_name()) ||
                                        (user.getInterest() == userList.get(w).getInterest())){
                                    if(votesList.get(q).getUser_id() == userList.get(w).getId()){
                                        counter++;
                                    }
                                }
                            }
                        }
                    }
                    statistic.put(answersList.get(j), counter);
                }
            }
        }catch (Exception e){
            System.out.println("Exception");
        }
        for (Map.Entry entry : statistic.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: "
                    + entry.getValue());
        }
        return statistic;
    }

    public void deleteVote(int q_id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Votes> votes;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Votes> query = builder.createQuery(Votes.class);
            Root<Votes> root = query.from(Votes.class);

            Predicate predicate = builder.equal(root.get("q_id"), q_id);
            votes = session.createQuery(query.where(predicate)).getResultList();
            for (int i = 0; i < votes.size(); i++){
                session.delete(votes.get(i));
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public double getStatistic(Questions questions, Answers answers){
        double counter = 0;
        List<Answers> answersList = find_answer_to_question(questions);
        for (int i = 0; i < answersList.size(); i++){
            counter += answersList.get(i).getCounter();
        }
        double percent = answers.getCounter()*100 / counter;
        return percent;
    }
}

