/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen;

/**
 * Класс для представления отчёта по выполненному заданию.
 * Содержит оценку и комментарий преподавателя.
 * @author Иван Исаев
 */
public class TaskRecord {
    int grade;
    String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}