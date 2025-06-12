/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen;

import java.util.ArrayList;

/**
 *
 * @author ivis2
 */
public class StudentResult {
    ArrayList<Task> tasks;
    boolean hasNoWork = false;

    public boolean isHasNoWork() {
        return hasNoWork;
    }

    public void setHasNoWork(boolean hasNoWork) {
        this.hasNoWork = hasNoWork;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
