/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen;

import java.util.ArrayList;

/**
 * Класс для представления задания варианта.
 * Содержит номер задания, условие, максимальную оценку,
 * входные данные и отчёт по заданию.
 * @author Иван Исаев
 */
public class Task {
    int taskNumber;
    String condition;
    int maxGrade;
    ArrayList<ArrayList<String>> Data;
    TaskRecord report;
    
    public TaskRecord getReport() {
        return report;
    }
    
    public void setReport(TaskRecord report) {
        this.report = report;
    }
    
    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }
    
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(int maxGrade) {
        this.maxGrade = maxGrade;
    }

    public ArrayList<ArrayList<String>> getData() {
        return Data;
    }

    public void setData(ArrayList<ArrayList<String>> data) {
        Data = data;
    }
    
    @Override
    public String toString() {
        if (report != null){
            return "Задание " + (taskNumber+1) + ", оценено, оценка: " +report.getGrade();
        } else return "Задание " + (taskNumber+1);
    }
}
