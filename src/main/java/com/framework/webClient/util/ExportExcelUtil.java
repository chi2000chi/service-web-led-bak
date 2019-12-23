package com.framework.webClient.util;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 文件名  ExportExcelUtil
 * 描述  通用excel导出工具类
 * @auther 吉庆
 * 创建日期  2018年6月6日
 */
public class ExportExcelUtil {
	 /**
     * 验证整数
     */
    public static final String regNum = "^[1-9]*[1-9][0-9]*$";

    /**
     * 设置excel表格样式
     * @param wb
     * @return
     */
    public static CellStyle setUpCellStyle(SXSSFWorkbook wb){
        // 设置除标题行以外的样式
        CellStyle styleBold = wb.createCellStyle();
        // 设置边框下边框样式
        styleBold.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        styleBold.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置边框左边框样式
        styleBold.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        styleBold.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置边框上边框样式
        styleBold.setBorderTop(XSSFCellStyle.BORDER_THIN);
        styleBold.setTopBorderColor(HSSFColor.BLACK.index);
        // 设置边框右边框样式
        styleBold.setBorderRight(XSSFCellStyle.BORDER_THIN);
        styleBold.setRightBorderColor(HSSFColor.BLACK.index);
        // 垂直居中
        styleBold.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        Font font = wb.createFont();
        // 字体高度
        font.setFontHeightInPoints((short) 11);
        styleBold.setFont(font);
        // 水平布局：居中
        styleBold.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //设置文字自动换行
        styleBold.setWrapText(true);
        return styleBold;
    }

    /**
     * 设置excel表头样式
     * @param wb
     * @return
     */
    public static CellStyle setUpTitleCellStyle(SXSSFWorkbook wb){
        // 设置标题行样式
        CellStyle headerStyle = wb.createCellStyle();
        // 设置背景色
        headerStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE
                .getIndex());
        // 设置边框下边框
        headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        // 设置边框左边框
        headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        // 设置边框右边框
        headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        // 设置边框上边框
        headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        // 水平居中
        headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 垂直居中
        headerStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        Font headerFont = wb.createFont();
        // 字体高度
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setColor(HSSFColor.WHITE.index);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    /**
     *
     * @param rege 正则表达式
     * @param param 需要验证的参数
     * @return
     */
    public static boolean matchs(String rege, String param) {
        Pattern pattern = Pattern.compile(rege);
        Matcher matcher = pattern.matcher(param);
        return matcher.matches();
    }

    /**
     *
     * @param wb excel工作区
     * @param dataList 导出的数据
     * @param sheet 对应的sheet页
     * @param lastRowNum excel模板中的数据行数，如果指定了excel模板，需要传入该参数，否则不需要
     * @param titleList  导出的列的顺序
     */
    public static void writeDateToCell(SXSSFWorkbook wb, List<Map<String, Object>> dataList, SXSSFSheet sheet, int lastRowNum, List<String> titleList) {
        // 临时文件进行压缩，建议不要true，否则会影响导出时间
        wb.setCompressTempFiles(false);
        //设置cell样式
        CellStyle styleBold = setUpCellStyle(wb);
        //设置表头样式
        CellStyle headerStyle = setUpTitleCellStyle(wb);
        //存储每一列的最大列宽的键值对，列索引为key，最大列宽为值
        Map<Integer, Integer> colMap = new HashMap<>();
        // 遍历数据，创建表格
        for (short i = 0; i < dataList.size(); i++) {
            // 创建行
            Row row = sheet.createRow(lastRowNum + i);
            // 设置行高亮
            row.setHeightInPoints(20);
            // 取数据
            Map<String, Object> fileMap = dataList.get(i);
            // 遍历map，创建单元格
            if (fileMap != null && !fileMap.isEmpty()) {
                // 用于记录每行创建的单元格个数
                int colNum = 0;
                for (String key : titleList) {
                    // 如果是第一行数据，创建单元格，使用标题行样式
                    if (i == 0) {
                        row.createCell(colNum).setCellStyle(headerStyle);
                    } else {
                        // 其他行数据，创建单元格，使用正常的行样式
                        row.createCell(colNum).setCellStyle(styleBold);
                    }
                    //计算长度
                    String colValue = fileMap.get(key) == null ? "" : String.valueOf(fileMap.get(key));
                    //如果是整数，列宽度*2
                    int strLength = matchs(regNum, colValue) ? colValue.getBytes().length * 2 * 256 :  colValue.getBytes().length * 256;
                    //判断该列是否已经存在，若果存在，比较当前值与colMap中值大小，不存在，将当前字符串长度和列值保存
                    if(colMap.containsKey(colNum)){
                        if(strLength > colMap.get(colNum)){
                            colMap.put(colNum, strLength);
                        }
                    }else{
                        colMap.put(colNum, strLength);
                    }
                    // 将数据写进单元格中
                    row.getCell(colNum).setCellValue(colValue);
                    // 单元格记录数+1
                    colNum++;
                }
            }

        }
        //遍历列值数组，设置列宽
        for (Map.Entry<Integer, Integer> entry : colMap.entrySet()) {
            Integer colWidth = entry.getValue();
            if(colWidth > 255*256) {
                colWidth = 255 * 256;
            }
            sheet.setColumnWidth(entry.getKey(), colWidth);
        }
    }

    /**
     *
     * @param dataList 需要导出的数据（请将标题行放在第一行）
     * @param excelPath excel模板存放路径，可以不传
     * @param fileName 导出excel文件名，可以不传，默认使用当前时间为文件名
     * @param response
     */
	@SuppressWarnings("unchecked")
	public static void excel(List<Map<String, Object>> dataList,
                             String excelPath, String fileName, HttpServletResponse response) {
        try {
            // 声明excel文件对象
            SXSSFWorkbook wb = null;
            // 声明sheet页
            SXSSFSheet sheet = null;
            // 设置excel文件名后缀
            // 判断是否存在excel文件路径，如果存在获取excel文件，如果不存在创建excel对象
            if (excelPath != null && !excelPath.isEmpty()) {
                // 获取excel文件对象
                File excelFile = new File(excelPath);
                // 获取文件输入流
                FileInputStream io = new FileInputStream(excelFile);
                // 创建Excel文件对象
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(io);
                // 创建SXSSF文件对象，POI3.8 beta3开始支持，基于XSSF，低内存占用
                wb = new SXSSFWorkbook(xssfWorkbook, 1000);
                for(int i = 0;i < wb.getNumberOfSheets();i++){
                    // 获取第一个sheet页
                    sheet = wb.getSheetAt(i);
                    if(i >= dataList.size()){
                        break;
                    }
                    Map<String, Object> dataMap = dataList.get(i);
					List<Map<String, Object>> excelData = (List<Map<String, Object>>) dataMap.get("dataList");
                    int lastRowNum = dataMap.get("lastRowNum") == null ? 0 : Integer.parseInt(String.valueOf(dataMap.get("lastRowNum")));
                    List<String> titleList = (List<String>) dataMap.get("titleList");
                    writeDateToCell(wb, excelData, sheet, lastRowNum, titleList);
                }
            } else {
                wb = new SXSSFWorkbook(1000);
                for(int i = 0;i < dataList.size();i++){
                    Map<String, Object> dataMap = dataList.get(i);
                    // 创建一个sheet页
                    sheet = dataMap.get("sheetName") == null ? wb.createSheet() : wb.createSheet(String.valueOf(dataMap.get("sheetName")));
                    List<Map<String, Object>> excelData = (List<Map<String, Object>>) dataMap.get("dataList");
                    int lastRowNum = dataMap.get("lastRowNum") == null ? 0 : Integer.parseInt(String.valueOf(dataMap.get("lastRowNum")));
                    List<String> titleList = (List<String>) dataMap.get("titleList");
                    writeDateToCell(wb, excelData, sheet, lastRowNum, titleList);
                }
            }
            // 判断是否存在文件名，如果没有文件名则使用获取当前时间
            if (fileName == null || fileName.isEmpty()) {
                // 设置日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                // 设置文件名
                fileName = df.format(new Date()) + ".xlsx";
                // excel 导出部分
            }
            // 转换编码格式，防止乱码
            fileName = new String(fileName.getBytes("utf-8"), "iso8859-1");
            // 去除首部空行
            response.reset();
            // 设置response header
            response.setHeader("Content-Disposition", "attachment;filename="
                    + fileName);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            // 获取输出流
            OutputStream output = response.getOutputStream();
            BufferedOutputStream bufferedOutPut = new BufferedOutputStream(
                    output);
            bufferedOutPut.flush();
            // 输出文件
            wb.write(bufferedOutPut);
            // 关闭流
            bufferedOutPut.close();
            wb.close();
        } catch (IOException | EncryptedDocumentException e) {
            e.printStackTrace();
        }
    }
}
