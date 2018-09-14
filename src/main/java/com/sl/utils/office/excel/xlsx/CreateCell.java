package com.sl.utils.office.excel.xlsx;

import com.sl.utils.date.DateUtils;
import com.sl.utils.office.word.WordUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * Illustrates how to create cell and set values of different types.
 *
 *
 *  具体参考 http://poi.apache.org/spreadsheet/examples.html#business-plan
 */
public class CreateCell {

    public static void main(String[] args) throws IOException {
        URL basePath = WordUtils.class.getClassLoader().getResource("");
        String outPutPath = basePath.getPath() + "/temp/"+ DateUtils.getCurrentTime_yyyyMMddHHmmssSSS();
        try (Workbook wb = new XSSFWorkbook()) { //or new HSSFWorkbook();
            CreationHelper creationHelper = wb.getCreationHelper();
            Sheet sheet = wb.createSheet("new sheet");

            // Create a row and put some cells in it. Rows are 0 based.
            Row row = sheet.createRow((short) 0);
            // Create a cell and put a value in it.
            Cell cell = row.createCell((short) 0);
            cell.setCellValue(1);

            //numeric value
            row.createCell(1).setCellValue(1.2);

            //plain string value
            row.createCell(2).setCellValue("This is a string cell");

            //rich text string
            RichTextString str = creationHelper.createRichTextString("Apache");
            Font font = wb.createFont();
            font.setItalic(true);
            font.setUnderline(Font.U_SINGLE);
            str.applyFont(font);
            row.createCell(3).setCellValue(str);

            //boolean value
            row.createCell(4).setCellValue(true);

            //formula
            row.createCell(5).setCellFormula("SUM(A1:B1)");

            //date
            CellStyle style = wb.createCellStyle();
            style.setDataFormat(creationHelper.createDataFormat().getFormat("m/d/yy h:mm"));
            cell = row.createCell(6);
            cell.setCellValue(new Date());
            cell.setCellStyle(style);

            //hyperlink
            row.createCell(7).setCellFormula("SUM(A1:B1)");
            cell.setCellFormula("HYPERLINK(\"http://google.com\",\"Google\")");

            File file = new File(outPutPath);
            if(!file.exists()){
                file.mkdirs();
            }
            // Write the output to a file
            String filePath = outPutPath+ File.separator+"ooxml-cell.xlsx";
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                wb.write(fileOut);
            }
            if(new File(filePath).exists()){
                System.out.println("文件生成成功！");
            }
        }
    }
}