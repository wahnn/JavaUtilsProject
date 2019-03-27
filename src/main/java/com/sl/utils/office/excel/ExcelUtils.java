package com.sl.utils.office.excel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtils {
    public static void main(String[] args) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDhhmmss");
        String now = dateFormat.format(new Date());
        //导出文件路径
        //文件名
        String[] title = {"序号", "姓名", "学号", "性别", "入学日期"};
        String sheelName = "学生信息";
        String fileName = "数据_" + now+".xlsx" ;
        String suffix = "";
        String filePath = "D:"+ File.separator+"excelExport"+ File.separator;
        //需要导出的数据
        List<String[]> dataList = new ArrayList<String[]>();
        dataList.add(new String[]{"东邪", "17232401001", "男", "2015年9月"});
        dataList.add(new String[]{"西毒", "17232401002", "女", "2016年9月"});
        dataList.add(new String[]{"南帝", "17232401003", "男", "2017年9月"});
        dataList.add(new String[]{"北丐", "17232401004", "男", "2015年9月"});
        dataList.add(new String[]{"中神通", "17232401005", "女", "2017年9月"});
        exportExcelXlsx(title,sheelName,dataList,fileName,filePath);
//        fileName = "数据_" + now + ".xls";
//        exportExcelXls(title,sheelName,dataList,fileName,filePath);
    }

    public static File exportExcelXlsx(String[] title, String sheelName, List<String[]> dataList, String fileName, String filePath) throws IOException {

        // 声明一个工作薄
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workBook.createSheet();
        workBook.setSheetName(0, sheelName);
        // 创建表格标题行 第一行
        XSSFRow titleRow = sheet.createRow(0);
        for (int i = 0; i < title.length; i++) {
            titleRow.createCell(i).setCellValue(title[i]);
        }
        //插入需导出的数据
        for (int i = 0; i <dataList.size(); i++) {
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(dataList.get(i)[0]);
            row.createCell(2).setCellValue(dataList.get(i)[1]);
            row.createCell(3).setCellValue(dataList.get(i)[2]);
            row.createCell(4).setCellValue(dataList.get(i)[3]);
        }
        File checkPath = new File(filePath);
        if(!checkPath.exists()){
            checkPath.mkdirs();
        }
        File file = new File(filePath + fileName);
//        File file = File.createTempFile(fileName,"xlsx");
        //文件输出流
        FileOutputStream outStream = new FileOutputStream(file);
        workBook.write(outStream);
        outStream.flush();
        outStream.close();
        if(file.exists()){
            //删除临时文件
//            file.delete();
            System.out.println("导出文件成功！文件导出路径：--" + filePath + fileName);
        }

        return file;
    }

    public static void exportExcelXls(String[] title, String sheelName, List<String[]> dataList, String fileName, String filePath) throws IOException {

        try (HSSFWorkbook wb = new HSSFWorkbook()) {
            HSSFSheet sheet = wb.createSheet(sheelName);

            // Create a row and put some cells in it. Rows are 0 based.
            HSSFRow titleRow = sheet.createRow(0);
            for (int i = 0; i < title.length; i++) {
                titleRow.createCell(i).setCellValue(title[i]);
            }
            // Create a cell and put a value in it.
            //插入需导出的数据
            for (int i = 0; i <dataList.size(); i++) {
                HSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(dataList.get(i)[0]);
                row.createCell(2).setCellValue(dataList.get(i)[1]);
                row.createCell(3).setCellValue(dataList.get(i)[2]);
                row.createCell(4).setCellValue(dataList.get(i)[3]);
            }

            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream(filePath+fileName)) {
                wb.write(          );
                fileOut.close();
            }

            System.out.println("导出文件成功！文件导出路径：--" + filePath + fileName);
        }
    }


}
