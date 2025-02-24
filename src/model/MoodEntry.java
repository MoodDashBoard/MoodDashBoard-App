package model;

import model.Enums.MoodState;

import java.util.Date;

public class MoodEntry {
    private MoodState mood;
    private Date date;
    private String text;

    public MoodEntry() {}

    public MoodEntry(MoodState mood, Date date, String text) {
        this.mood = mood;
        this.date = date;
        this.text = text;
    }

    public MoodState getMood() {
        return mood;
    }

    public void setMood(MoodState mood) {
        this.mood = mood;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MoodEntry{" +
                "mood=" + mood +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}