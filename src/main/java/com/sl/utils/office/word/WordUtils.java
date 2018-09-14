package com.sl.utils.office.word;

import com.sl.utils.date.DateUtils;
import com.sl.utils.freemark.FreeMarkUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * docx、doc文档生成工具类  (改变后缀名即可)
 * 在使用制作模板的过程中如果模板中有图片那就保留图片，注意[Content_Types].xml和document.xml.rels文档
 * 如果模板中没有图片 则不需要设置[Content_Types].xml和document.xml.rels
 * 由于word模板的个性化 所以 每次做模板都要重新覆盖原来的模板
 *
 *
 *
 * gaoxueyong
 */
public class WordUtils {

    private final static String separator = File.separator;
    private final static String suffix_docx = "docx";
    private final static String suffix_doc = "doc";


    /*
     * @param dataMap               参数数据
     * @param docxTemplateFile      docx模主板名称
     * @param xmlDocument           docx中document.xml模板文件  用来存在word文档的主要数据信息
     * @param xmlDocumentXmlRels    docx中document.xml.rels 模板文件  用来存在word文档的主要数据配置 包括图片的指向
     * @param xmlContentTypes       docx中 [Content_Types].xml 模板文件 用来配置 docx文档中所插入图片的类型 如 png、jpeg、jpg等
     * @param xmlHeader             docx中 header1.xml 模板文件 用来配置docx文档的页眉文件
     * @param templatePath          模板存放路径 如 /templates/
     * @param outputFileTempPath    所生成的docx文件的临时路径文件夹 如果 temp/20180914051811/
     * @param outputFileName        所生成的docx文件名称  如  xxx.docx  或  xxx.doc
     * */
    public static void createDocx(Map dataMap, String docxTemplateFile, String xmlDocument, String xmlDocumentXmlRels,
                                  String xmlContentTypes, String xmlHeader, String templatePath,
                                  String outputFileTempPath, String outputFileName) throws Exception {

        URL basePath = WordUtils.class.getClassLoader().getResource("");
//        System.out.println("basePath.getPath() ==> " + basePath.getPath());
        String realTemplatePath = basePath.getPath() + templatePath;
        //临时文件产出的路径
        String outputPath = basePath.getPath() + outputFileTempPath;
        List<String> delFileList = new ArrayList<>();
        try {


            //================================获取 document.xml.rels 输入流================================
            String xmlDocumentXmlRelsComment = FreeMarkUtils.getFreemarkerContent(dataMap, xmlDocumentXmlRels, templatePath);
            ByteArrayInputStream documentXmlRelsInput =
                    new ByteArrayInputStream(xmlDocumentXmlRelsComment.getBytes());
            //================================获取 document.xml.rels 输入流================================

            //================================获取 header1.xml 输入流================================
            ByteArrayInputStream headerInput = FreeMarkUtils.getFreemarkerContentInputStream(dataMap, xmlHeader, templatePath);

            //================================获取 header1.xml 输入流================================

            //================================获取 [Content_Types].xml 输入流================================
            ByteArrayInputStream contentTypesInput = FreeMarkUtils.getFreemarkerContentInputStream(dataMap, xmlContentTypes, templatePath);
            //================================获取 [Content_Types].xml 输入流================================


            //读取 document.xml.rels  文件 并获取rId 与 图片的关系 (如果没有图片 此文件不用编辑直接读取就行了)
            Document document = DocumentHelper.parseText(xmlDocumentXmlRelsComment);
            Element rootElt = document.getRootElement(); // 获取根节点
            Iterator iter = rootElt.elementIterator();// 获取根节点下的子节点head
            List<Map<String, String>> picList = (List<Map<String, String>>) dataMap.get("picList");

            // 遍历Relationships节点
            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next();
                String id = recordEle.attribute("Id").getData().toString();
                String target = recordEle.attribute("Target").getData().toString();
                if (target.indexOf("media") == 0) {
//                        System.out.println("id>>>"+id+"   >>>"+target);
//                        id>>>rId18   >>>media/pic1
//
                    for (Map<String, String> picMap : picList) {
                        if (target.endsWith(picMap.get("name"))) {
                            picMap.put("rId", id);
                        }
                    }
                }
            }
            dataMap.put("picList", picList);//覆盖原来的picList;

            //================================获取 document.xml 输入流================================
            ByteArrayInputStream documentInput = FreeMarkUtils.getFreemarkerContentInputStream(dataMap, xmlDocument, templatePath);
            //================================获取 document.xml 输入流================================


//            System.out.println("base_path_template+separator+docxTemplate===="+base_path_template+separator+docxTemplate);
            File docxFile = new File(realTemplatePath + separator + docxTemplateFile);
            if (!docxFile.exists()) {
                docxFile.createNewFile();
            }

            ZipFile zipFile = new ZipFile(docxFile);
            Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
            File tempPath = new File(outputPath);
            //如果输出目标文件夹不存在，则创建
            if (!tempPath.exists()) {
                tempPath.mkdirs();
            }
            ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(outputPath + outputFileName));


            //------------------覆盖文档------------------
            int len = -1;
            byte[] buffer = new byte[1024];
            while (zipEntrys.hasMoreElements()) {
                ZipEntry next = zipEntrys.nextElement();
                InputStream is = zipFile.getInputStream(next);
                if (next.toString().indexOf("media") < 0) {
                    // 把输入流的文件传到输出流中 如果是word/document.xml由我们输入
                    zipout.putNextEntry(new ZipEntry(next.getName()));
//                    System.out.println("next.getName()>>>" + next.getName() + "  next.isDirectory()>>>" + next.isDirectory());
                    //写入图片配置类型
                    if (next.getName().equals("[Content_Types].xml")) {
                        if (contentTypesInput != null) {
                            while ((len = contentTypesInput.read(buffer)) != -1) {
                                zipout.write(buffer, 0, len);
                            }
                            contentTypesInput.close();
                        }

                    } else if (next.getName().indexOf("document.xml.rels") > 0) {
                        //写入填充数据后的主数据配置信息
                        if (documentXmlRelsInput != null) {
                            while ((len = documentXmlRelsInput.read(buffer)) != -1) {
                                zipout.write(buffer, 0, len);
                            }
                            documentXmlRelsInput.close();
                        }
                    } else if ("word/document.xml".equals(next.getName())) {
                        //写入填充数据后的主数据信息
                        if (documentInput != null) {
                            while ((len = documentInput.read(buffer)) != -1) {
                                zipout.write(buffer, 0, len);
                            }
                            documentInput.close();
                        }

                    } else if ("word/header1.xml".equals(next.getName())) {
                        //写入填充数据后的页眉信息
                        if (headerInput != null) {
                            while ((len = headerInput.read(buffer)) != -1) {
                                zipout.write(buffer, 0, len);
                            }
                            headerInput.close();
                        }

                    } else {
                        while ((len = is.read(buffer)) != -1) {
                            zipout.write(buffer, 0, len);
                        }
                        is.close();
                    }

                }

            }
            //------------------覆盖文档------------------

            //------------------写入新图片------------------
            len = -1;
            if (picList != null && !picList.isEmpty()) {
                for (Map<String, String> pic : picList) {
                    ZipEntry next = new ZipEntry("word" + separator + "media" + separator + pic.get("name"));
                    zipout.putNextEntry(new ZipEntry(next.toString()));
                    InputStream in = new FileInputStream(pic.get("path"));
                    while ((len = in.read(buffer)) != -1) {
                        zipout.write(buffer, 0, len);
                    }
                    in.close();
                }
            }


            //------------------写入新图片------------------
            zipout.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("生成docx文件失败！");
        }

    }


    /**
     * 删除文件
     *
     * @param listFiles
     */
    public static void delFiles(List<String> listFiles) {
        try {
            if (listFiles != null && !listFiles.isEmpty()) {
                for (String file_temp_path : listFiles) {
                    File file_temp = new File(file_temp_path);
                    if (file_temp.exists()) {
                        file_temp.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        URL basePath = WordUtils.class.getClassLoader().getResource("");
//        System.out.println("basePath.getPath() ==> " + basePath.getPath());
        String picPath = basePath.getPath() + separator + "templates" + separator;
        ;
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("ymdhis", DateUtils.getCurrentTime_yyyyMMddHHmmss());
        List<String> listTile = new ArrayList<>();
        listTile.add("这是第一个标题");
        listTile.add("这是第二个标题");
        listTile.add("这是第三个标题");
        dataMap.put("listTitle", listTile);

        List<String> picTypes = new ArrayList<>();
        picTypes.add("jpg");
        dataMap.put("picTypes", picTypes);

        List<Map<String, String>> picList = new ArrayList<>();

        Map<String, String> picMap = new HashMap<>();
        // 要按顺序
        picMap.put("path", picPath + "pic1.jpg");
        picMap.put("name", "pic1.jpg");
        picList.add(picMap);

        picMap = new HashMap<>();
        picMap.put("path", picPath + "pic2.jpg");
        picMap.put("name", "pic2.jpg");
        picList.add(picMap);

        picMap = new HashMap<>();
        picMap.put("path", picPath + "pic3.jpg");
        picMap.put("name", "pic3.jpg");
        picList.add(picMap);
        dataMap.put("picList", picList);
        List<Map<String, Object>> listTaleData = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "小明");
        map.put("age", "11");
        map.put("sex", "男");
        map.put("grade", "五年级");
        listTaleData.add(map);

        map = new HashMap<>();
        map.put("name", "小红");
        map.put("age", "12");
        map.put("sex", "女");
        map.put("grade", "六年级");
        listTaleData.add(map);

        map = new HashMap<>();
        map.put("name", "小花");
        map.put("age", "13");
        map.put("sex", "女");
        map.put("grade", "七年级");
        listTaleData.add(map);
        dataMap.put("listTaleData", listTaleData);

        dataMap.put("summary", "这次word文档产出的非常棒！继续努力！O(∩_∩)O哈哈~");



        String timeStr = DateUtils.getCurrentTime_yyyyMMddHHmmssSSS();
        String docxTemplateFile = "docTemplates.docx";
        String xmlDocument = "document.xml";
        String xmlDocumentXmlRels = "document.xml.rels";
        String xmlContentTypes = "[Content_Types].xml";
        String xmlHeader = "header1.xml";//可以用来修改页眉的一些信息
        String templatePath = separator + "templates" + separator;
        String outputFileTempPath = "temp" + separator + timeStr + separator;
        String outputFileName = timeStr + "."+suffix_docx;
//        String outputFileName = timeStr + "."+suffix_doc;


        /*
        * @param dataMap               参数数据
        * @param docxTemplateFile      docx模主板名称
        * @param xmlDocument           docx中document.xml模板文件  用来存在word文档的主要数据信息
        * @param xmlDocumentXmlRels    docx中document.xml.rels 模板文件  用来存在word文档的主要数据配置 包括图片的指向
        * @param xmlContentTypes       docx中 [Content_Types].xml 模板文件 用来配置 docx文档中所插入图片的类型 如 png、jpeg、jpg等
        * @param xmlHeader             docx中 header1.xml 模板文件 用来配置docx文档的页眉文件
        * @param templatePath          模板存放路径 如 /templates/
        * @param outputFileTempPath    所生成的docx文件的临时路径文件夹 如果 temp/20180914051811/
        * @param outputFileName        所生成的docx文件名称  如  xxx.docx 或  xxx.doc
        * */
        try {
            createDocx(dataMap, docxTemplateFile, xmlDocument, xmlDocumentXmlRels, xmlContentTypes,
                    xmlHeader, templatePath, outputFileTempPath, outputFileName);


//            String xmlDocumentXmlRelsComment = FreeMarkUtils.getFreemarkerContent(dataMap,xmlDocumentXmlRels,separator + "templates" );
//            System.out.println(xmlDocumentXmlRelsComment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
