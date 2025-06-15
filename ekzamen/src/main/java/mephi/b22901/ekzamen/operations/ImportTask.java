/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ekzamen.operations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import mephi.b22901.ekzamen.Task;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Класс для импорта данных конкретной задачи из Excel.
 * Формирует объект Task с данными таблицы.
 * @author Иван Исаев
 */
public class ImportTask {
    /**
     * Импортирует задачу из Excel по её номеру.
     * Сначала выбирает лист по номеру, затем считывает все строки и ячейки.
     * Результат сохраняется в объект Task.
     *
     * @param book Excel, содержащая листы с задачами.
     * @param taskNumber Номер задачи (лист в книге).
     * @return Объект Task с таблицей данных.
     */
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static Task importTask(XSSFWorkbook book, int taskNumber) {
        XSSFSheet sheet = book.getSheetAt(taskNumber);
        Task task = new Task();
        task.setTaskNumber(taskNumber);
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            int j = 0;
            ArrayList<String> dataRow = new ArrayList<>();

            while (row.getCell(j) != null) {
                String rec = null;
                if (row.getCell(j).getCellType() == CellType.STRING) {
                    rec = row.getCell(j).getStringCellValue();
                } else if (DateUtil.isCellDateFormatted(row.getCell(j))){
                    rec = dateFormat.format(row.getCell(j).getDateCellValue());
                } else if (row.getCell(j).getCellType() == CellType.NUMERIC){
                    rec = String.valueOf(row.getCell(j).getNumericCellValue());
                }
                dataRow.add(rec);
                j++;
            }
            data.add(dataRow);
        }
        task.setData(data);
        return task;
    }
}
