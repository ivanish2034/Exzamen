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
import java.util.List;
import mephi.b22901.ekzamen.Group;
import mephi.b22901.ekzamen.Student;
import mephi.b22901.ekzamen.Task;
import mephi.b22901.ekzamen.TaskRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ivis2
 */
public class ReportGenerator {

    public enum Format {
        TXT,
        EXCEL
    }

    public static void generateGroupReport(Group group, File file, Format format, int passingGrade) throws IOException {
        if (format == Format.TXT) {
            writeGroupTxtReport(group, file, passingGrade);
        } else {
            writeGroupExcelReport(group, file, passingGrade);
        }
    }

    private static void writeGroupTxtReport(Group group, File file, int passingGrade) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Отчет по группе: " + group.getName());
            writer.println("Минимальный балл для зачёта: " + passingGrade);
            writer.println();
            
            for (Student student : group.getStudents()) {
                writer.println("ФИО: " + student.getFIO());
                writer.println("Вариант: " + student.getVariant());

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
            header.createCell(0).setCellValue("ФИО");
            header.createCell(1).setCellValue("Вариант");

            for (int i = 0; i < maxTasks; i++) {
                header.createCell(i + 2).setCellValue("Задание " + (i + 1));
            }

            header.createCell(maxTasks + 2).setCellValue("Сумма баллов");
            header.createCell(maxTasks + 3).setCellValue("Итог");

            for (Student student : group.getStudents()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getFIO());
                row.createCell(1).setCellValue(student.getVariant());

                if (student.getReport() == null || student.getReport().isHasNoWork()) {
                    for (int i = 0; i < maxTasks; i++) {
                        row.createCell(i + 2).setCellValue("Н/Р");
                    }
                    row.createCell(maxTasks + 2).setCellValue(0);
                    row.createCell(maxTasks + 3).setCellValue("Незачёт");
                } else {
                    int total = 0;
                    List<Task> tasks = student.getReport().getTasks();

                    for (int i = 0; i < maxTasks; i++) {
                        if (i < tasks.size()) {
                            int grade = tasks.get(i).getReport().getGrade();
                            row.createCell(i + 2).setCellValue(grade);
                            total += grade;
                        } else {
                            row.createCell(i + 2).setCellValue("-");
                        }
                    }

                    row.createCell(maxTasks + 2).setCellValue(total);
                    row.createCell(maxTasks + 3).setCellValue(total >= passingGrade ? "Зачёт" : "Незачёт");
                }
            }

            for (int i = 0; i < maxTasks + 4; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        }
    }
}