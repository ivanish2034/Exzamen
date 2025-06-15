/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen.operations;

import mephi.b22901.ekzamen.Task;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;


/**
 * Класс для импорта условий задачи из Excel.
 * Используется для поиска и установки условия и максимального балла задачи.
 * @author Иван Исаев
 */
public class ImportCondition {
    /**
     * Импортирует условие задачи и максимальный балл из последнего листа Excel.
     * Сначала проходит по строкам листа в поиске строки с номером задания.
     * Если найдено, извлекает максимальный балл и текст условия.
     *
     * @param lastSheet Лист Excel, содержащий все условия задач.
     * @param task Объект задачи, в который будет установлено условие и максимальный балл.
     */
    public static void getCondition(XSSFSheet lastSheet, Task task) {
        int rowIndex = 0;
        String foundCondition = "";
        int maxGrade = 0;

        while (rowIndex < lastSheet.getLastRowNum()) {
            XSSFRow row = lastSheet.getRow(rowIndex);
            if (row != null && row.getCell(0) != null &&
                    row.getCell(0).getStringCellValue().equals("Задание " + (task.getTaskNumber() + 1))) {

                maxGrade = Integer.parseInt(lastSheet.getRow(rowIndex + 3).getCell(1).getStringCellValue());
                foundCondition = lastSheet.getRow(rowIndex + 6).getCell(0).getStringCellValue();
                break;
            }
            rowIndex++;
        }
        task.setCondition(foundCondition);
        task.setMaxGrade(maxGrade);
    }

}
