package com.sp.utils;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    public static void main(String[] args) throws IOException {

//        String filePath = "C:\\Users\\wb-gxy329407\\Desktop\\海口路口配时方案20180119.xls";
        String filePath = "C:\\Users\\wb-gxy329407\\Desktop\\海口路口配时方案20180119.xlsx";
        String outPutFilePath = "C:\\Users\\wb-gxy329407\\Desktop";
        int sheetNum = 0;
        String outPutFileName = "海口路口配时方案20180119_t"+sheetNum+".txt";
        readXlsx(filePath,0,outPutFileName,outPutFilePath);
//        readXls(filePath);

    }

    /**
     *  读取xlsx文件 并生成文件
     * @param filePath 读取文件的地址
     * @param sheetNum 读取文件的sheetNum
     * @param outPutFileName 生成文件的名称
     * @param outPutFilePath 生成文件的地址
     * @throws IOException
     */
    public static void readXlsx(String filePath,int sheetNum,String outPutFileName,String outPutFilePath) throws IOException
    {
        XSSFWorkbook xwb = new XSSFWorkbook(filePath);
        //读取第一个sheet
        XSSFSheet sheet = xwb.getSheetAt(sheetNum);
        XSSFRow row;
        String cell;
        int rowLen = 0;
        List<String> list = new ArrayList<String>();

        StringBuffer sf = new StringBuffer();
        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++)
        {
            row = sheet.getRow(i);
            if(i == row.getFirstCellNum()){
                rowLen = row.getPhysicalNumberOfCells();
            }

            for (int j = row.getFirstCellNum(); j < rowLen; j++)
            {
                if(row.getCell(j)!=null){
                    cell = row.getCell(j).toString();
                    if(!"".equals(cell)){
//                        System.out.print(cell + "\t");
                        sf.append(cell + "\t");
                    }else{
//                        System.out.print("null" + "\t");
                        sf.append("null" + "\t");
                    }
                }else {
//                    System.out.print("null" + "\t");
                    sf.append("null" + "\t");
                }
            }
//            System.out.println("");
            list.add(sf.toString());
            sf = new StringBuffer();

        }
        TextUtils.writeText(list,outPutFileName,outPutFilePath);
    }


    /**
     * 读取xlsx文件
     * @throws IOException
     */
    public static void readXlsx(String filePath) throws IOException
    {
        XSSFWorkbook xwb = new XSSFWorkbook(filePath);
        XSSFSheet sheet = xwb.getSheetAt(0);
        XSSFRow row;
        String cell;
        int rowLen = 0;
        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++)
        {

            row = sheet.getRow(i);
            if(i == row.getFirstCellNum()){
                rowLen = row.getPhysicalNumberOfCells();
            }
            for (int j = row.getFirstCellNum(); j < rowLen; j++)
            {
                if(row.getCell(j)!=null){
                    cell = row.getCell(j).toString();
                    if(!"".equals(cell)){
                        System.out.print(cell + "\t");
                    }else{
                        System.out.print("null" + "\t");
                    }
                }else {
                    System.out.print("null" + "\t");
                }
            }
            System.out.println("");
        }
    }

    /**
     * 读取xls文件
     * @throws IOException
     */
    public static void readXls(String filePath) throws IOException
    {
        FileInputStream in = new FileInputStream( filePath);
        HSSFWorkbook book = new HSSFWorkbook(in);

        HSSFSheet sheet = book.getSheetAt(0);
        HSSFRow row;
        String cell;
        int rowLen = 0;
        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++)
        {
            row = sheet.getRow(i);
            if(i == row.getFirstCellNum()){
                rowLen = row.getPhysicalNumberOfCells();
            }
            for (int j = row.getFirstCellNum(); j < rowLen; j++)
            {
                if(row.getCell(j)!=null){
                    cell = row.getCell(j).toString();
                    if(!"".equals(cell)){
                        System.out.print(cell + "\t");
                    }else{
                        System.out.print("null" + "\t");
                    }
                }else {
                    System.out.print("null" + "\t");
                }
            }
            System.out.println("");
        }
    }


}