/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen;

import java.util.ArrayList;

/**
 * Класс для представления варианта заданий.
 * Содержит список заданий данного варианта.
 * @author Иван Исаев
 */
public class Variant {
    ArrayList<Task> tasks;

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Variant{" + "tasks=" + tasks + "}";
    }
}
