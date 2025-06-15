/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen;

import java.util.ArrayList;

/**
 *
 * Класс для представления группы студентов.
 * Содержит название группы и список студентов.
 * @author Иван Исаев
 */
public class Group {
    String name;
    ArrayList<Student> students;

    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public ArrayList<Student> getStudents(){
        return students;
    }
    
    public void setStudents(ArrayList<Student> students){
        this.students = students;
    }

    @Override
    public String toString(){
        return "Group{" + "name ' " + name + "' " + ", students" + students + "}";
    }
}
