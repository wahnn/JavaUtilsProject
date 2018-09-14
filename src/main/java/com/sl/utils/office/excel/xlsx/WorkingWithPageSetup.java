package com.sl.utils.office.excel.xlsx;

import com.sl.utils.date.DateUtils;
import com.sl.utils.office.word.WordUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Demonstrates various settings avaiable in the Page Setup dialog
 *
 *  具体参考 http://poi.apache.org/spreadsheet/examples.html#business-plan
 */
public class WorkingWithPageSetup {

    public static void main(String[]args) throws Exception {
        URL basePath = WordUtils.class.getClassLoader().getResource("");
        String outPutPath = basePath.getPath() + "/temp/"+ DateUtils.getCurrentTime_yyyyMMddHHmmssSSS();
        try (Workbook wb = new XSSFWorkbook()) {  //or new HSSFWorkbook();

        /*
         * It's possible to set up repeating rows and columns in your printouts by using the setRepeatingRowsAndColumns() function in the Workbook object.
         *
         * This function Contains 5 parameters:
         * The first parameter is the index to the sheet (0 = first sheet).
         * The second and third parameters specify the range for the columns to repreat.
         * To stop the columns from repeating pass in -1 as the start and end column.
         * The fourth and fifth parameters specify the range for the rows to repeat.
         * To stop the columns from repeating pass in -1 as the start and end rows.
         */
            Sheet sheet1 = wb.createSheet("new sheet");
            Sheet sheet2 = wb.createSheet("second sheet");

            // Set the columns to repeat from column 0 to 2 on the first sheet
            Row row1 = sheet1.createRow(0);
            row1.createCell(0).setCellValue(1);
            row1.createCell(1).setCellValue(2);
            row1.createCell(2).setCellValue(3);
            Row row2 = sheet1.createRow(1);
            row2.createCell(1).setCellValue(4);
            row2.createCell(2).setCellValue(5);


            Row row3 = sheet2.createRow(1);
            row3.createCell(0).setCellValue(2.1);
            row3.createCell(4).setCellValue(2.2);
            row3.createCell(5).setCellValue(2.3);
            Row row4 = sheet2.createRow(2);
            row4.createCell(4).setCellValue(2.4);
            row4.createCell(5).setCellValue(2.5);

            // Set the columns to repeat from column 0 to 2 on the first sheet
            sheet1.setRepeatingColumns(CellRangeAddress.valueOf("A:C"));
            // Set the the repeating rows and columns on the second sheet.
            CellRangeAddress cra = CellRangeAddress.valueOf("E2:F3");
            sheet2.setRepeatingColumns(cra);
            sheet2.setRepeatingRows(cra);

            //set the print area for the first sheet
            wb.setPrintArea(0, 1, 2, 0, 3);

            File file = new File(outPutPath);
            if(!file.exists()){
                file.mkdirs();
            }
            String filePath = outPutPath+ File.separator+"xssf-printsetup.xlsx";
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                wb.write(fileOut);
            }
            if(new File(filePath).exists()){
                System.out.println("文件生成成功！");
            }
        }
    }
}