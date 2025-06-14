/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen.operations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import mephi.b22901.ekzamen.Group;
import mephi.b22901.ekzamen.Student;
import mephi.b22901.ekzamen.Task;
import mephi.b22901.ekzamen.TaskRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.List;
/**
 *
 * @author ivis2
 */
public class ReportGenerator {

    public enum Format {
        TXT,
        EXCEL
    }

    public static void generateStudentReport(Student student, Group group, File file, Format format, int passingGrade) throws IOException {
        if (format == Format.TXT) {
            writeStudentTxtReport(student, group, file, passingGrade);
        } else {
            writeStudentExcelReport(student, group, file, passingGrade);
        }
    }

    public static void generateGroupReport(Group group, File file, Format format, int passingGrade) throws IOException {
        if (format == Format.TXT) {
            writeGroupTxtReport(group, file, passingGrade);
        } else {
            writeGroupExcelReport(group, file, passingGrade);
        }
    }

    public static void generateAllGroupsReport(List<Group> groups, File file, Format format, int passingGrade) throws IOException {
        if (format == Format.TXT) {
            writeAllGroupsTxtReport(groups, file, passingGrade);
        } else {
            writeAllGroupsExcelReport(groups, file, passingGrade);
        }
    }

    private static void writeStudentTxtReport(Student student, Group group, File file, int passingGrade) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Индивидуальный отчет по студенту: " + student.getFIO());
            writer.println("Группа: " + group.getName());
            writer.println("Вариант: " + student.getVariant());
            writer.println("Минимальный балл для зачёта: " + passingGrade);
            writer.println();

            if (student.getReport() == null || student.getReport().isHasNoWork()) {
                writer.println("Нет выполненной работы");
                writer.println("Итог: Незачёт");
            } else {
                writer.println("Задания:");
                int total = 0;
                for (Task task : student.getReport().getTasks()) {
                    TaskRecord tr = task.getReport();
                    writer.printf("  %d. %d/%d", 
                        task.getTaskNumber() + 1, tr.getGrade(), task.getMaxGrade());
                    if (tr.getComment() != null) {
                        writer.printf(" (Комментарий: %s)", tr.getComment());
                    }
                    writer.println();
                    total += tr.getGrade();
                }
                writer.println("Сумма баллов: " + total);
                writer.println("Итог: " + (total >= passingGrade ? "Зачёт" : "Незачёт"));
            }
        }
    }

    private static void writeStudentExcelReport(Student student, Group group, File file, int passingGrade) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(student.getFIO());
            int rowNum = 0;

            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("Студент:");
            headerRow.createCell(1).setCellValue(student.getFIO());
            
            Row groupRow = sheet.createRow(rowNum++);
            groupRow.createCell(0).setCellValue("Группа:");
            groupRow.createCell(1).setCellValue(group.getName());
            
            Row variantRow = sheet.createRow(rowNum++);
            variantRow.createCell(0).setCellValue("Вариант:");
            variantRow.createCell(1).setCellValue(student.getVariant());
            
            Row passingRow = sheet.createRow(rowNum++);
            passingRow.createCell(0).setCellValue("Мин. балл:");
            passingRow.createCell(1).setCellValue(passingGrade);

            if (student.getReport() == null || student.getReport().isHasNoWork()) {
                Row resultRow = sheet.createRow(rowNum++);
                resultRow.createCell(0).setCellValue("Результат:");
                resultRow.createCell(1).setCellValue("Нет работы");
            } else {
                Row tableHeader = sheet.createRow(rowNum++);
                tableHeader.createCell(0).setCellValue("№");
                tableHeader.createCell(1).setCellValue("Задание");
                tableHeader.createCell(2).setCellValue("Оценка");
                tableHeader.createCell(3).setCellValue("Макс. балл");
                tableHeader.createCell(4).setCellValue("Комментарий");

                int total = 0;
                for (Task task : student.getReport().getTasks()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(task.getTaskNumber() + 1);
                    row.createCell(1).setCellValue("Задание " + (task.getTaskNumber() + 1));
                    row.createCell(2).setCellValue(task.getReport().getGrade());
                    row.createCell(3).setCellValue(task.getMaxGrade());
                    if (task.getReport().getComment() != null) {
                        row.createCell(4).setCellValue(task.getReport().getComment());
                    }
                    total += task.getReport().getGrade();
                }

                Row totalRow = sheet.createRow(rowNum++);
                totalRow.createCell(0).setCellValue("Итого:");
                totalRow.createCell(2).setCellValue(total);
                
                Row resultRow = sheet.createRow(rowNum);
                resultRow.createCell(0).setCellValue("Результат:");
                resultRow.createCell(2).setCellValue(total >= passingGrade ? "Зачёт" : "Незачёт");
            }

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        }
    }

    private static void writeGroupTxtReport(Group group, File file, int passingGrade) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Отчет по группе: " + group.getName());
            writer.println("Минимальный балл для зачёта: " + passingGrade);
            writer.println();
            int studentNum = 1;
            for (Student student : group.getStudents()) {
                writer.println(studentNum++ + ". " + student.getFIO());
                writer.println("  Вариант: " + student.getVariant());

                if (student.getReport() == null || student.getReport().isHasNoWork()) {
                    writer.println("  Нет работы");
                    writer.println("  Итог: Незачёт");
                } else {
                    int total = 0;
                    for (Task task : student.getReport().getTasks()) {
                        TaskRecord tr = task.getReport();
                        writer.printf("  Задание %d: %d/%d\n", 
                            task.getTaskNumber() + 1, tr.getGrade(), task.getMaxGrade());
                        total += tr.getGrade();
                    }
                    writer.println("  Сумма баллов: " + total);
                    writer.println("  Итог: " + (total >= passingGrade ? "Зачёт" : "Незачёт"));
                }
                writer.println();
            }
        }
    }

    private static void writeGroupExcelReport(Group group, File file, int passingGrade) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Группа " + group.getName());
            int rowNum = 0;

            int maxTasks = group.getStudents().stream()
                .filter(s -> s.getReport() != null && !s.getReport().isHasNoWork())
                .mapToInt(s -> s.getReport().getTasks().size())
                .max()
                .orElse(0);

            Row infoRow = sheet.createRow(rowNum++);
            infoRow.createCell(0).setCellValue("Минимальный балл для зачёта: " + passingGrade);

            rowNum++;

            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("№");
            header.createCell(1).setCellValue("ФИО");
            header.createCell(2).setCellValue("Вариант");

            for (int i = 0; i < maxTasks; i++) {
                header.createCell(i + 3).setCellValue("Зад. " + (i + 1));
            }

            header.createCell(maxTasks + 3).setCellValue("Сумма");
            header.createCell(maxTasks + 4).setCellValue("Итог");
            int studentNum = 1;
            for (Student student : group.getStudents()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(studentNum++);
                row.createCell(1).setCellValue(student.getFIO());
                row.createCell(2).setCellValue(student.getVariant());

                if (student.getReport() == null || student.getReport().isHasNoWork()) {
                    for (int i = 0; i < maxTasks; i++) {
                        row.createCell(i + 3).setCellValue("Н/Р");
                    }
                    row.createCell(maxTasks + 3).setCellValue(0);
                    row.createCell(maxTasks + 4).setCellValue("Незачёт");
                } else {
                    int total = 0;
                    List<Task> tasks = student.getReport().getTasks();

                    for (int i = 0; i < maxTasks; i++) {
                        if (i < tasks.size()) {
                            int grade = tasks.get(i).getReport().getGrade();
                            row.createCell(i + 3).setCellValue(grade);
                            total += grade;
                        } else {
                            row.createCell(i + 3).setCellValue("-");
                        }
                    }

                    row.createCell(maxTasks + 3).setCellValue(total);
                    row.createCell(maxTasks + 4).setCellValue(total >= passingGrade ? "Зачёт" : "Незачёт");
                }
            }

            for (int i = 0; i < maxTasks + 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        }
    }

    private static void writeAllGroupsTxtReport(List<Group> groups, File file, int passingGrade) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Сводный отчет по всем группам");
            writer.println("Минимальный балл для зачёта: " + passingGrade);
            writer.println();
            
            for (Group group : groups) {
                writer.println("=== Группа " + group.getName() + " ===");
                
                int studentNum = 1;
                for (Student student : group.getStudents()) {
                    writer.println();
                    writer.println(studentNum++ + ". " + student.getFIO());
                    writer.println("  Вариант: " + student.getVariant());

                    if (student.getReport() == null || student.getReport().isHasNoWork()) {
                        writer.println("  Нет работы");
                        writer.println("  Итог: Незачёт");
                    } else {
                        int total = 0;
                        for (Task task : student.getReport().getTasks()) {
                            TaskRecord tr = task.getReport();
                            writer.printf("  Задание %d: %d/%d\n", 
                                task.getTaskNumber() + 1, tr.getGrade(), task.getMaxGrade());
                            total += tr.getGrade();
                        }
                        writer.println("  Сумма баллов: " + total);
                        writer.println("  Итог: " + (total >= passingGrade ? "Зачёт" : "Незачёт"));
                    }
                }
                writer.println();
            }
        }
    }

    private static void writeAllGroupsExcelReport(List<Group> groups, File file, int passingGrade) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            for (Group group : groups) {
                Sheet sheet = workbook.createSheet(group.getName());
                int rowNum = 0;

                int maxTasks = group.getStudents().stream()
                    .filter(s -> s.getReport() != null && !s.getReport().isHasNoWork())
                    .mapToInt(s -> s.getReport().getTasks().size())
                    .max()
                    .orElse(0);

                Row infoRow = sheet.createRow(rowNum++);
                infoRow.createCell(0).setCellValue("Группа: " + group.getName());
                infoRow.createCell(1).setCellValue("Мин. балл: " + passingGrade);

                rowNum++;

                Row header = sheet.createRow(rowNum++);
                header.createCell(0).setCellValue("№");
                header.createCell(1).setCellValue("ФИО");
                header.createCell(2).setCellValue("Вариант");

                for (int i = 0; i < maxTasks; i++) {
                    header.createCell(i + 3).setCellValue("Зад. " + (i + 1));
                }

                header.createCell(maxTasks + 3).setCellValue("Сумма");
                header.createCell(maxTasks + 4).setCellValue("Итог");

                int studentNum = 1;
                for (Student student : group.getStudents()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(studentNum++);
                    row.createCell(1).setCellValue(student.getFIO());
                    row.createCell(2).setCellValue(student.getVariant());

                    if (student.getReport() == null || student.getReport().isHasNoWork()) {
                        for (int i = 0; i < maxTasks; i++) {
                            row.createCell(i + 3).setCellValue("Н/Р");
                        }
                        row.createCell(maxTasks + 3).setCellValue(0);
                        row.createCell(maxTasks + 4).setCellValue("Незачёт");
                    } else {
                        int total = 0;
                        List<Task> tasks = student.getReport().getTasks();

                        for (int i = 0; i < maxTasks; i++) {
                            if (i < tasks.size()) {
                                int grade = tasks.get(i).getReport().getGrade();
                                row.createCell(i + 3).setCellValue(grade);
                                total += grade;
                            } else {
                                row.createCell(i + 3).setCellValue("-");
                            }
                        }

                        row.createCell(maxTasks + 3).setCellValue(total);
                        row.createCell(maxTasks + 4).setCellValue(total >= passingGrade ? "Зачёт" : "Незачёт");
                    }
                }
                for (int i = 0; i < maxTasks + 5; i++) {
                    sheet.autoSizeColumn(i);
                }
            }
            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        }
    }
}
