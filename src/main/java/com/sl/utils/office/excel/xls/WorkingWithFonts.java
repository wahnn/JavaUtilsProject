package com.sl.utils.office.excel.xls;


import com.sl.utils.date.DateUtils;
import com.sl.utils.office.word.WordUtils;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Demonstrates how to create and use fonts.
 */
public class WorkingWithFonts {
    public static void main(String[] args) throws IOException {
        URL basePath = WordUtils.class.getClassLoader().getResource("");
        String outPutPath = basePath.getPath() + "/temp/"+ DateUtils.getCurrentTime_yyyyMMddHHmmssSSS();
        try (HSSFWorkbook wb = new HSSFWorkbook()) {
            HSSFSheet sheet = wb.createSheet("new sheet");

            // Create a row and put some cells in it. Rows are 0 based.
            HSSFRow row = sheet.createRow(1);

            // Create a new font and alter it.
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 24);
            font.setFontName("Courier New");
            font.setItalic(true);
            font.setStrikeout(true);

            // Fonts are set into a style so create a new one to use.
            HSSFCellStyle style = wb.createCellStyle();
            style.setFont(font);

            // Create a cell and put a value in it.
            HSSFCell cell = row.createCell(1);
            cell.setCellValue("This is a test of fonts");
            cell.setCellStyle(style);

            File file = new File(outPutPath);
            if(!file.exists()){
                file.mkdirs();
            }
            String filePath = outPutPath+ File.separator+"workbook.xls";
            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                wb.write(fileOut);
            }
            if(new File(filePath).exists()){
                System.out.println("文件生成成功！");
            }
        }
    }
}