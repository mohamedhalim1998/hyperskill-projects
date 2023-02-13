package platform.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity(name = "codes")
@Table(name = "codes")
public class CodeSnippet {
    @JsonIgnore
    @Id
    private String id;
    private String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime date;
    @JsonIgnore
    private long timeLimit;
    @JsonProperty("views")
    private int viewLimit;
    @JsonIgnore
    private boolean hasViewLimit;
    @JsonProperty("time")
    private long remainTime;


    public CodeSnippet() {
    }

    public CodeSnippet(String code, LocalDateTime date,  long timeLimit, int viewLimit) {
        this.code = code;
        this.date = date;
        this.timeLimit = timeLimit;
        this.viewLimit = viewLimit;
        this.remainTime = timeLimit;
    }

    public CodeSnippet(String id, String code, LocalDateTime date,int timeLimit, int viewLimit) {
        this.id = id;
        this.code = code;
        this.date = date;
        this.timeLimit = timeLimit;
        this.viewLimit = viewLimit;
        this.remainTime = timeLimit;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String formatDateTime() {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public void setDate(LocalDateTime date) {
        this.date = date;
    }



    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getViewLimit() {
        return viewLimit;
    }

    public void setViewLimit(int viewLimit) {
        this.viewLimit = viewLimit;
    }

    public long getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime;
    }

    public boolean hasViewLimit() {
        return hasViewLimit;
    }

    public void setHasViewLimit(boolean hasViewLimit) {
        this.hasViewLimit = hasViewLimit;
    }

    @Override
    public String toString() {
        return "CodeSnippet{" +
               "id='" + id + '\'' +
               ", code='" + code + '\'' +
               ", date=" + date +
               ", timeLimit=" + timeLimit +
               ", viewLimit=" + viewLimit +
               ", remainTime=" + remainTime +
               '}';
    }
}
