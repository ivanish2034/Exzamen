/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen.operations;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import mephi.b22901.ekzamen.*;
import static mephi.b22901.ekzamen.operations.ImportTask.importTask;
import static mephi.b22901.ekzamen.operations.ImportCondition.getCondition;

/**
 * Класс для импорта студентов и вариантов из Excel-файлов.
 * Использует Apache POI для чтения файлов Excel.
 * @author Иван Исаев
 */
public class ExcelImport  {
    /**
     * Импортирует студентов из Excel-файла.
     * Для каждой страницы создается группа,
     * а строки страницы преобразуются в студентов.
     *
     * @param file Excel-файл со списками студентов по группам.
     * @return Список всех групп с их студентами.
     * @throws IOException Если файл не найден или не может быть прочитан.
     * @throws InvalidFormatException Если формат файла не поддерживается.
     */
    public static ArrayList<Group> importStudents(File file) throws IOException,  InvalidFormatException{
        XSSFWorkbook book = new XSSFWorkbook(file);
        ArrayList<Group> groups = new ArrayList<>();
        for (int i = 0; i<book.getNumberOfSheets(); i++){
            XSSFSheet sheet = book.getSheetAt(i);
            Group group = new Group();
            ArrayList<Student> students = new ArrayList<>();
            for (int j = 1; j<=sheet.getLastRowNum(); j++){
                Student student = new Student();
                XSSFRow row = sheet.getRow(j);
                Random rand = new Random();
                student.setVariant((int) row.getCell(2).getNumericCellValue());
                student.setFIO(row.getCell(1).getStringCellValue());
                students.add(student);
            }
            group.setStudents(students);
            group.setName(sheet.getSheetName());
            groups.add(group);
        }
        return groups;
    }
    /**
     * Импортирует вариант заданий из Excel-файла.
     * Для каждого листа задачи формируется Task,
     * для каждой задачи загружается условие и максимальный балл.
     *
     * @param fileName Путь к файлу Excel.
     * @return Вариант с полным списком задач.
     * @throws IOException Если файл не найден или не может быть прочитан.
     */
    public static Variant importVariant(String fileName) throws IOException{
        XSSFWorkbook book = new XSSFWorkbook(fileName);
        Variant variant = new Variant();
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i<book.getNumberOfSheets()-1; i++){

            Task task = importTask(book, i);
            XSSFSheet lastSheet = book.getSheetAt(book.getNumberOfSheets()-1);

            getCondition(lastSheet, task);
            tasks.add(task);
        }
        variant.setTasks(tasks);
        return variant;
    }
}