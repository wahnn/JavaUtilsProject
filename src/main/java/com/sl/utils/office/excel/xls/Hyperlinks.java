package com.sl.utils.office.excel.xls;

import com.sl.utils.date.DateUtils;
import com.sl.utils.office.word.WordUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Demonstrates how to create hyperlinks.
 */
public class Hyperlinks {

    public static void main(String[] args) throws IOException {
        URL basePath = WordUtils.class.getClassLoader().getResource("");
        String outPutPath = basePath.getPath() + "/temp/"+ DateUtils.getCurrentTime_yyyyMMddHHmmssSSS();
        try (HSSFWorkbook wb = new HSSFWorkbook()) {
            HSSFCreationHelper helper = wb.getCreationHelper();

            //cell style for hyperlinks
            //by default hyperlinks are blue and underlined
            HSSFCellStyle hlink_style = wb.createCellStyle();
            HSSFFont hlink_font = wb.createFont();
            hlink_font.setUnderline(Font.U_SINGLE);
            hlink_font.setColor(HSSFColorPredefined.BLUE.getIndex());
            hlink_style.setFont(hlink_font);

            HSSFCell cell;
            HSSFSheet sheet = wb.createSheet("Hyperlinks");

            //URL
            cell = sheet.createRow(0).createCell(0);
            cell.setCellValue("URL Link");
            HSSFHyperlink link = helper.createHyperlink(HyperlinkType.URL);
            link.setAddress("http://poi.apache.org/");
            cell.setHyperlink(link);
            cell.setCellStyle(hlink_style);

            //link to a file in the current directory
            cell = sheet.createRow(1).createCell(0);
            cell.setCellValue("File Link");
            link = helper.createHyperlink(HyperlinkType.FILE);
            link.setAddress("link1.xls");
            cell.setHyperlink(link);
            cell.setCellStyle(hlink_style);

            //e-mail link
            cell = sheet.createRow(2).createCell(0);
            cell.setCellValue("Email Link");
            link = helper.createHyperlink(HyperlinkType.EMAIL);
            //note, if subject contains white spaces, make sure they are url-encoded
            link.setAddress("mailto:poi@apache.org?subject=Hyperlinks");
            cell.setHyperlink(link);
            cell.setCellStyle(hlink_style);

            //link to a place in this workbook

            //create a target sheet and cell
            HSSFSheet sheet2 = wb.createSheet("Target Sheet");
            sheet2.createRow(0).createCell(0).setCellValue("Target Cell");

            cell = sheet.createRow(3).createCell(0);
            cell.setCellValue("Worksheet Link");
            link = helper.createHyperlink(HyperlinkType.DOCUMENT);
            link.setAddress("'Target Sheet'!A1");
            cell.setHyperlink(link);
            cell.setCellStyle(hlink_style);


            File file = new File(outPutPath);
            if(!file.exists()){
                file.mkdirs();
            }
            String filePath = outPutPath+ File.separator+"hssf-links.xls";

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                wb.write(out);
            }
            if(new File(filePath).exists()){
                System.out.println("文件生成成功！");
            }
        }
    }
}