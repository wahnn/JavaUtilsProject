package com.sl.utils.office.excel;

import com.sl.utils.date.DateUtils;
import com.sl.utils.text.TextUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExcelUtils {

    private static String commonsp = "\\|";

    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\wb-gxy329407\\Desktop\\excel\\海口路口配时方案20180119.xlsx";
        filePath = "C:\\Users\\wb-gxy329407\\Desktop\\excel\\ss.xlsx";
        String outPutFilePath = "C:\\Users\\wb-gxy329407\\Desktop\\excel";
        Integer sheetNum = 7;
        String sheelName = "和平二";
        String outPutFileName = "海口路口配时方案20180119_"+sheetNum+"_"+sheelName+"_one.txt";
        getSheel(filePath,outPutFileName,outPutFilePath,sheetNum,sheelName,true);
    }


    /**
     *
     * @param filePath 读取文件的路径
     * @param outPutFileName 生成文件的名称
     * @param outPutFilePath 产出文件的路径
     * @param sheetNum 读取exce的第几个sheel
     * @param sheelName  sheel名称
     * @param show true  显示明细  false 不显示明细
     * @throws IOException
     */
    public static void getSheel(String filePath, String outPutFileName, String outPutFilePath, int sheetNum, String sheelName, boolean show) throws IOException {
        //最终结果
        List<String> final_result_one = new ArrayList<String>();
//        路口名称(inter_name)	方案编号(phase_plan_no)	周期时长(cycle_time)	周几(day_of_week)	时间片序号(step_index)	相位阶段名称(phase_name)	绿信时间

        final_result_one.add("路口名称(inter_name)\t方案编号(phase_plan_no)\t周期时长(cycle_time)\t周几(day_of_week)\t时间片序号(step_index)\t相位阶段名称(phase_name)\t绿信时间");
        List<String> final_result_two = new ArrayList<String>();
        final_result_two.add("路口名称(inter_name)\t方案编号(phase_plan_no)\t相位阶段名称(phase_name)\t方向(dir_name)");
        //第一个sheel的内容
        Map<String,Object> result_sheelOne = readXlsx(filePath,sheetNum);
        //用于迭代的日期段
        List<String> listDate = (List<String>) result_sheelOne.get("listDate");
        //用于计算的sheel的具体内容
        List<String> listData = (List<String>) result_sheelOne.get("listData");
        Map<String,String> distinctMap = new HashMap<String,String>();
        List<Map<String,String>> listDis = new ArrayList<Map<String, String>>();

        //最终匹配结果
        List<String> result = new ArrayList<String>();


        if (listDate != null && !listDate.isEmpty()) {
            for (String dateinof : listDate) {
                for (String datainfo : listData) {

                    if (!"".equals(datainfo) && datainfo.indexOf(dateinof) > 0) {
                        String[] dataAry = datainfo.split(commonsp);
                                if(dataAry.length<4){
                                    System.out.println("sss");
                                }
                        distinctMap.put(dataAry[4], dateinof);
                    }

                }
                listDis.add(distinctMap);
                distinctMap = new HashMap<String, String>();
            }
        }

        for (Map<String, String> m : listDis) {
            for (String key : m.keySet()) {
                for (String datainfo : listData) {
                    if(!"".equals(datainfo)){
                        String[] dataAry = datainfo.split(commonsp);

                        if (dataAry[4].equals(key) && dataAry[7].equals(m.get(key))) {
                            result.add(datainfo);
                        }
                    }

                }
            }
        }

        Map<String,String> distinctMapOne = new HashMap<String,String>();
        Map<String,String> distinctMapTwo = new HashMap<String,String>();

        for(String datainfo:result){
            String[] dataAry = datainfo.split(commonsp);
//            路口名称(inter_name)	方案编号(phase_plan_no)	周期时长(cycle_time)	周几(day_of_week)	时间片序号(step_index)	相位阶段名称(phase_name)	绿信时间

            String zj = "0";
            if(dataAry[10].equals("工作日")){
                zj = "1";
            }else if(dataAry[10].equals("休息日")){
                zj = "2";
            }
            if(!dataAry[7].equals("null")){
                List<Integer> timeIndexList = getTimeIndexList(dataAry[7]);
                for(Integer it:timeIndexList){
                    String val = dataAry[0]+"\t"+ Double.valueOf(dataAry[2]).intValue()+"\t"+dataAry[3]+"\t"+zj+"\t"+it+"\t"+dataAry[4]+"\t"+dataAry[8];
                    if(!distinctMapOne.containsKey(val)){
                        final_result_one.add(val);
                        distinctMapOne.put(val,"");
                    }
                }
                String val =dataAry[0]+"\t"+ Double.valueOf(dataAry[2]).intValue()+"\t"+dataAry[4]+"\t"+dataAry[5]+"-"+dataAry[6];
                if(!distinctMapTwo.containsKey(val)){
                    final_result_two.add(val);
                    distinctMapTwo.put(val,"");
                }
            }
        }

        TextUtils.writeText(final_result_one,outPutFileName,outPutFilePath);
        outPutFileName = "海口路口配时方案20180119_"+sheetNum+"_"+sheelName+"_two.txt";
        TextUtils.writeText(final_result_two,outPutFileName,outPutFilePath);

        if(show){
            System.out.println("*************************************************************************");
            for(String info:final_result_one){
                System.out.println(info);
            }
            System.out.println("*************************************************************************");

            for(String info:final_result_two){
                System.out.println(info);
            }
            System.out.println("############################################################################");
        }
    }


    /**
     * 获取时间片段的值
     * @param dateStr  eg: 19:20-24:00
     * @return
     */
    public static List<Integer> getTimeIndexList(String dateStr) {

        List<Integer> result = new ArrayList<Integer>();
        try {
            String[] dateAry = dateStr.split("-");
            String yyyy_MM_dd = DateUtils.getCurrentTime_yyyy_MM_dd();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat hour = new SimpleDateFormat("HH");
            SimpleDateFormat minute = new SimpleDateFormat("mm");
            Date dateStart = sdf.parse(yyyy_MM_dd + " " + dateAry[0]);
            Date dateEnd = sdf.parse(yyyy_MM_dd + " " + dateAry[1]);
            long addTime = 10 * 60 * 1000;
            Date start = dateStart;
            while (dateEnd.after(dateStart)){
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dateStart.getTime()+addTime);
                dateStart = calendar.getTime();
//                if(dateEnd.before(dateStart)){
//                    //打印时间段
//                    System.out.println(">>"+sdf.format(start)+"————"+sdf.format(dateEnd));
//                }else{
//                    //打印时间段
//                    System.out.println(">>"+sdf.format(start)+"————"+sdf.format(dateStart));
//                }

//                floor ((hour * 60 + minute)/10) 计算时间片公式
                Double timeIndex = Math.floor((Integer.valueOf(hour.format(start))*60 + Integer.valueOf(minute.format(start)))/10);
//                System.out.println(timeIndex.intValue());
                result.add(timeIndex.intValue());
                start = dateStart;
            }


        } catch (ParseException e) {
            return null;
        }

        return result;
    }

    /**
     *  读取xlsx文件 并生成文件
     * @param filePath 读取文件的地址
     * @param sheetNum 读取文件的sheetNum
     * @throws IOException
     */
    public static Map<String,Object> readXlsx(String filePath, int sheetNum) throws IOException
    {

        Map<String,Object> result = new HashMap<String,Object>();
        Map<String,Object> distinctMap = new HashMap<String,Object>();
        List<String> listDate = new ArrayList<String>();
        XSSFWorkbook xwb = new XSSFWorkbook(filePath);
        //读取第一个sheet
        XSSFSheet sheet = xwb.getSheetAt(sheetNum);
        XSSFRow row;
        String cell;
        int rowLen = 0;
        List<String> listData = new ArrayList<String>();

        StringBuffer sf = new StringBuffer();
        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++)
        {
            row = sheet.getRow(i);
            if(i == row.getFirstCellNum()){
                rowLen = row.getPhysicalNumberOfCells();
            }

            //去除标题
            if(i==0){
                continue;
            }

            boolean  dataFlag = false;
            for (int j = row.getFirstCellNum(); j < rowLen; j++)
            {
                if(j<0){
                   break;
                }
                if(row.getCell(j)!=null){
                    cell = row.getCell(j).toString();
                    if(!"".equals(cell)){
                        if(j==7 ){
                            cell = cell.replace("，",",");
                            cell = cell.replace(";",",");
                            cell = cell.replace("；",",");
                            cell = cell.replace("；",",");
                            cell = cell.replace("：",":");
                            cell = cell.replace("次日","00:00,00:00-").trim();
//                            23:50-次日7:00
//                            23:50-00:00,00:00-7:00
                            //拆分多时间段
                            if(cell.indexOf(",")>0){
                                String[] cellAry = cell.split(",");
                                for(String cellInfo:cellAry){
                                    if(!distinctMap.containsKey(cellInfo)){
                                        listDate.add(cellInfo);
                                    }
                                    distinctMap.put(cellInfo,cellInfo);
                                }
                            }else if (cell.indexOf("不启用")>0){
                                //不做处理

                            }else{
                                if(!distinctMap.containsKey(cell)){
                                    listDate.add(cell);
                                }
                                distinctMap.put(cell,cell);
                            }


                        }
//                        System.out.print(cell + "|");
                        sf.append(cell + "|");
                        dataFlag = true;
                    }else{
//                        System.out.print("null" + "|");
                        sf.append("null" + "|");
                    }
                }else {
//                    System.out.print("null" + "|");
                    sf.append("null" + "|");
                }
            }
            if(dataFlag && sf.toString().indexOf("不启用")<0){
//                //数据换行
//                System.out.println("");
                //拆分多时间段
                if(sf.toString().indexOf(",")>0){
                    String[] infoAry = sf.toString().split(commonsp);
                    String[] dateTimeAry = infoAry[7].split(",");
                    String head =infoAry[0]+"|"+infoAry[1]+"|"+infoAry[2]+"|"+infoAry[3]+"|"+infoAry[4]+"|"+infoAry[5]+"|"+infoAry[6];
                    String end = infoAry[8]+"|"+infoAry[9]+"|"+infoAry[10]+"|"+infoAry[11];
                    for(String dt:dateTimeAry){
                        listData.add(head+"|"+dt+"|"+end);
                    }
                }else{
                    listData.add(sf.toString());
                }
            }
            sf = new StringBuffer();

        }
        result.put("listData",listData);
        result.put("listDate",listDate);
        return result;
    }




}