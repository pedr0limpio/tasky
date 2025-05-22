package br.com.pedr0limpio.models;

import br.com.pedr0limpio.enums.Priority;
import br.com.pedr0limpio.enums.Tag;

import java.util.Date;
import java.util.List;

public class Task {

    private int id;
    private String description;
    private Priority priority;
    private List<Tag> tagList;
    private Date creation;
    private Date conclusion;

    public int getId() {
        return id;
    }

    public Task() {
        // Default constructor required for frameworks
    }

    public Task(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Date getConclusion() {
        return conclusion;
    }

    public void setConclusion(Date conclusion) {
        this.conclusion = conclusion;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", tagList=" + tagList +
                ", creation=" + creation +
                ", conclusion=" + conclusion +
                '}';
    }
}
