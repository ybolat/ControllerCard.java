package kz.edu.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "AnswersEntity")
@Table(name = "answers")
public class Answers implements Serializable {
    private int a_id;
    private String answer;
    private int q_id;
    private int counter;

    @Id
    @Column(name = "a_id")
    public int getA_id() {
        return a_id;
    }
    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    @Column(name = "answer")
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Column(name = "q_id")
    public int getQ_id() {
        return q_id;
    }
    public void setQ_id(int q_id) {
        this.q_id = q_id;
    }

    @Column(name = "counter")
    public int getCounter() {
        return counter;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }
}