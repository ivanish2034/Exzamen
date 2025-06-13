/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen.operations;

import mephi.b22901.ekzamen.Task;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author ivis2
 */
public class ImportCondition {
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
