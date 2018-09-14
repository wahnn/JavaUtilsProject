package com.sl.utils.text;

import com.sl.utils.date.DateUtils;

import java.io.*;
import java.util.List;

/**
 * text文档工具类
 */
public class TextUtils {

    /**
     * 写入txt文件
     *
     * @param result
     * @param fileName
     * @param filePath
     * @return
     */
    public static boolean writeText(List<String> result, String fileName, String filePath)  {
        StringBuffer content = new StringBuffer();
        boolean flag = false;
        BufferedWriter out = null;

        try {

            if (result != null && !result.isEmpty() && fileName!=null && !"".equals(fileName)) {
                fileName += "_" + DateUtils.getCurrentTime_yyyyMMddHHmmssSSS() + ".txt";
                File pathFile = new File(filePath);
                if (!pathFile.exists()) {
                    pathFile.mkdir();
                }
                String relFilePath = null;
                if(filePath.endsWith(File.separator)){
                    relFilePath = filePath + fileName;
                    System.out.println("filePath.endsWith(File.separator)");
                }else{
                    relFilePath = filePath + File.separator + fileName;
                    System.out.println("filePath.isNotendsWith(File.separator)");
                }

                File file = new File(relFilePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
    //                //标题头
    //                out.write("curr_time,link_id,travel_time,speed,reliabilitycode,link_len,adcode,time_stamp,state,public_rec_time,ds");
    //                out.newLine();
                for (String info : result) {

                    out.write(info);
                    out.newLine();
                }
                flag = true;
            }

            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            return flag;
        }
    }
}
