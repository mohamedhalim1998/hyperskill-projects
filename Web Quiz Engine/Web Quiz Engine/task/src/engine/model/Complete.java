package engine.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Complete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    @JsonProperty("id")
    private int quizId;
    @JsonIgnore
    private String userEmail;
    @JsonProperty("completedAt")
    private LocalDateTime time;

    public Complete(int quizId, String userEmail, LocalDateTime time) {
        this.quizId = quizId;
        this.userEmail = userEmail;
        this.time = time;
    }

    public Complete(int id, int quizId, String userEmail, LocalDateTime time) {
        this.id = id;
        this.quizId = quizId;
        this.userEmail = userEmail;
        this.time = time;
    }

    public Complete() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
