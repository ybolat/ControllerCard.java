package kz.edu.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "QuestionEntity")
@Table(name = "questions")
public class Questions implements Serializable {
    private int q_id;
    private String questions;


    @Id
    @Column(name = "q_id")
    public int getQ_id()
    {
        return this.q_id;
    }
    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    @Column(name = "question")
    public String getQuestions() {
        return questions;
    }
    public void setQuestions(String questions) {
        this.questions = questions;
    }
}