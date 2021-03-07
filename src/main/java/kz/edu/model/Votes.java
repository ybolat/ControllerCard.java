package kz.edu.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "VotesEntity")
@Table(name = "votes")
public class Votes implements Serializable {
    private int v_id;
    private int user_id;
    private int q_id;
    private int a_id;

    @Id
    @Column(name = "v_id")
    public int getV_id() {
        return v_id;
    }
    public void setV_id(int v_id) {
        this.v_id = v_id;
    }

    @Column(name = "user_id")
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Column(name = "q_id")
    public int getQ_id() {
        return q_id;
    }
    public void setQ_id(int q_id) {
        this.q_id = q_id;
    }

    @Column(name = "a_id")
    public int getA_id() {
        return a_id;
    }
    public void setA_id(int a_id) {
        this.a_id = a_id;
    }
}
