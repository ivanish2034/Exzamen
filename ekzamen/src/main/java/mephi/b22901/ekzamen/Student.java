/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen;

/**
 *
 * @author ivis2
 */
public class Student {
    int variant;
    String fio;
    StudentResult report;

    public StudentResult getReport() {
        return report;
    }

    public void setReport(StudentResult report) {
        this.report = report;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public String getFIO() {
        return fio;
    }

    public void setFIO(String fio) {
        this.fio = fio;
    }

    @Override
    public String toString() {
        String rate;
        if (report==null){
            rate = "не оценен";
        }
        else if (report.hasNoWork){
            rate = "нет работы";
        }
        else rate = "оценен";
        return "" + fio + ", " + variant + " вариант, " + rate;
    }
}