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

/**
 *
 * @author ivis2
 */
public class ReportGenerator {

    public enum Format {
        TXT,
        EXCEL
    }

    private static final int PASSING_GRADE = 15; 

    public static void generateGroupReport(Group group, File file, Format format) throws IOException {
        if (format == Format.TXT) {
            writeGroupTxtReport(group, file);
        } else {
            writeGroupExcelReport(group, file);
        }
    }

    private static void writeGroupTxtReport(Group group, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Отчет по группе: " + group.getName());
            writer.println("Минимальный балл для зачёта: " + PASSING_GRADE);
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
                    if (total >= PASSING_GRADE) {
                        writer.println("  Итог: Зачёт");
                    } else {
                        writer.println("  Итог: Незачёт");
                    }
                }
                writer.println();
            }
        }
    }
    
    private static void writeGroupExcelReport(Group group, File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Группа " + group.getName());
            int rowNum = 0;

            Row infoRow = sheet.createRow(rowNum++);
            infoRow.createCell(0).setCellValue("Минимальный балл для зачёта: " + PASSING_GRADE);

            sheet.createRow(rowNum++);

            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("ФИО");
            header.createCell(1).setCellValue("Вариант");
            header.createCell(2).setCellValue("Статус");
            header.createCell(3).setCellValue("Сумма баллов");
            header.createCell(4).setCellValue("Итог");

            for (Student student : group.getStudents()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getFIO());
                row.createCell(1).setCellValue(student.getVariant());

                if (student.getReport() == null || student.getReport().isHasNoWork()) {
                    row.createCell(2).setCellValue("Нет работы");
                    row.createCell(3).setCellValue(0);
                    row.createCell(4).setCellValue("Незачёт");
                } else {
                    int total = 0;
                    for (Task task : student.getReport().getTasks()) {
                        total += task.getReport().getGrade();
                    }
                    
                    row.createCell(2).setCellValue("Оценено");
                    row.createCell(3).setCellValue(total);
                    String result;
                    if (total >= PASSING_GRADE) {
                        result = "Зачёт";
                    } else {
                        result = "Незачёт";
                    }
                    row.createCell(4).setCellValue(result);
                }
            }
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        }
    }
}
