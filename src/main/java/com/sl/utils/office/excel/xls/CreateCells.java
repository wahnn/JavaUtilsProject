package com.sl.utils.office.excel.xls;


import com.sl.utils.date.DateUtils;
import com.sl.utils.office.word.WordUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Illustrates how to create cell values.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public class CreateCells {
    public static void main(String[] args) throws IOException {
        URL basePath = WordUtils.class.getClassLoader().getResource("");
        String outPutPath = basePath.getPath() + "/temp/"+ DateUtils.getCurrentTime_yyyyMMddHHmmssSSS();
        try (HSSFWorkbook wb = new HSSFWorkbook()) {
            HSSFSheet sheet = wb.createSheet("new sheet");

            // Create a row and put some cells in it. Rows are 0 based.
            HSSFRow row = sheet.createRow(0);
            // Create a cell and put a value in it.
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(1);

            // Or do it on one line.
            row.createCell(1).setCellValue(1.2);
            row.createCell(2).setCellValue("This is a string");
            row.createCell(3).setCellValue(true);


            File file = new File(outPutPath);
            if(!file.exists()){
                file.mkdirs();
            }

            // Write the output to a file
            String filePath = outPutPath+ File.separator+"workbook.xls";
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                wb.write(fileOut);
            }
            if(new File(filePath).exists()){
                System.out.println("文件生成成功！");
            }
        }
    }
}