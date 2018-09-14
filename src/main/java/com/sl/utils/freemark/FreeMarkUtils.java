package com.sl.utils.freemark;


import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;

/**
 * 获取freemarker模板字符串
 */
public class FreeMarkUtils {

    /**
     * 获取模板字符串
     * @param dataMap   参数
     * @param templateName  模板名称
     * @param temp_path  模板路径 classes下的路径 如果  classes/templates  传入 /templates即可
     * @return
     */
    public static String getFreemarkerContent(Map dataMap, String templateName, String temp_path) {
        String result = "";
        try {
            //创建配置实例
            Configuration configuration = new Configuration();

            //设置编码
            configuration.setDefaultEncoding("UTF-8");

            //ftl模板文件统一放至 com.lun.template 包下面
//            configuration.setDirectoryForTemplateLoading(new File("D:/idea_workspace/alarm/alarm/src/main/resources/template/"));
            configuration.setClassForTemplateLoading(FreeMarkUtils.class, temp_path);
            //获取模板
            Template template = configuration.getTemplate(templateName);

            StringWriter swriter = new StringWriter();
            //生成文件
            template.process(dataMap, swriter);
            result = swriter.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生成主数据模板xml
     *
     * @param dataMap      数据参数
     * @param templateName 模板名称 eg: xxx.xml
     * @param pathPrefix   模板路径 eg: /templates/
     * @param filePath     生成路径 eg: d:/ex/ee/xxx.xml
     */
    public static void createTemplateXml(Map dataMap, String templateName, String pathPrefix, String filePath) {
        try {
            //创建配置实例
            Configuration configuration = new Configuration();

            //设置编码
            configuration.setDefaultEncoding("UTF-8");

            //ftl模板文件统一放至 com.lun.template 包下面
//            configuration.setDirectoryForTemplateLoading(new File("D:/idea_workspace/alarm/alarm/src/main/resources/template/"));
//            configuration.setClassForTemplateLoading(FreemarkerWordUtils.class,"/template/doc");
            configuration.setClassForTemplateLoading(FreeMarkUtils.class, pathPrefix);
            //获取模板
            Template template = configuration.getTemplate(templateName);
//            System.out.println("filePath ==> " + filePath);
            //输出文件
            File outFile = new File(filePath);
//            System.out.println("outFile.getParentFile() ==> " + outFile.getParentFile());
            //如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }

            //将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));


            //生成文件
            template.process(dataMap, out);

            //关闭流
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取模板字符串输入流
     * @param dataMap   参数
     * @param templateName  模板名称
     * @param tempPath  模板路径 classes下的路径 如果  classes/templates  传入 /templates即可
     * @return
     */
    public static ByteArrayInputStream getFreemarkerContentInputStream(Map dataMap, String templateName, String tempPath) {
        ByteArrayInputStream in = null;

        try {
            //创建配置实例
            Configuration configuration = new Configuration();

            //设置编码
            configuration.setDefaultEncoding("UTF-8");

            //ftl模板文件统一放至 com.lun.template 包下面
//            configuration.setDirectoryForTemplateLoading(new File("D:/idea_workspace/alarm/alarm/src/main/resources/template/"));
            configuration.setClassForTemplateLoading(FreeMarkUtils.class, tempPath);
            //获取模板
            Template template = configuration.getTemplate(templateName);

            StringWriter swriter = new StringWriter();
            //生成文件
            template.process(dataMap, swriter);
            String result = swriter.toString();
            in = new ByteArrayInputStream(swriter.toString().getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }
}
