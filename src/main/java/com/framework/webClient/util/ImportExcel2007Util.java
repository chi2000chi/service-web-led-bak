package com.framework.webClient.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * 
 * 文件名  ImportExcel2007Util
 * 描述  导入Excel2007文件工具类
 * @auther 吉庆
 * 创建日期  2018年5月28日
 */
public class ImportExcel2007Util {

	/**
	 * 返回消息：错误消息Key值ֵ
	 */
	private static final String ANY_CONSTANT_ERROR_MSG = "errorMsg";

	/**
	 * 标题字符串Key值
	 */
	private static final String ANY_CONSTANT_TITLE_STR = "titleString";

	/**
	 * 导入EXCEL文件格式版本不正确的提示ʾ
	 */
	private static final String ANY_CONSTANT_VERSION_ERROR = "请确认您上传的文件是EXCEL 2007格式的文件";

	/**
	 * 没有需要导入的数据的错误提示
	 */
	private static final String ANY_CONSTANT_NODATA_ERROR = "没有需要导入的数据";

	/**
	 * Excel格式不正确的错误提示
	 */
	private static final String ANY_CONSTANT_FORMAT_ERROR = "Excel格式不正确，导入的文件与导入模板的列数不匹配";
	
	/**
	 * sheetName名称常量
	 */
	private static final String ANY_CONSTANT_SHEET_NAME = "sheetName";

	/**
	 * doImmportExcel:导入、解析EXCEL. <br/>
	 * 
	 * @author 吉庆
	 * @param dataRow 起始行
	 * @param stream 导入数据流
	 * @param countColumn 上传excel总列数
	 * @param colList 导入文件每列对应的名称集合
	 * @return List 返回导入的Excel文件中的数据
	 * @throws Exception
	 * date: 2018年5月23日 14:54:27 <br/>
	 */
	@SuppressWarnings("resource")
	public static List<Map<String, Object>> doImmportExcel(int dataRow, InputStream stream, int countColumn, List<String> colList) throws Exception {
		HSSFWorkbook wb = null;
		// 声明并初始化存储导入数据的容器
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// office2007工作区
		try {
			wb = new HSSFWorkbook(stream);
		} catch (Exception e) {
			throw new Exception(ANY_CONSTANT_VERSION_ERROR);
		}
		for (int h = 0; h < wb.getNumberOfSheets(); h++) {
			// 获取sheet名称
			String sheetName = wb.getSheetName(h);
			// 获得该工作区的第一个sheet
			HSSFSheet sheet = wb.getSheetAt(h);
			// 总共有多少行,从0开始ʼ
			int totalRows = sheet.getLastRowNum();
			if (totalRows - dataRow < 0) {
				throw new Exception(ANY_CONSTANT_NODATA_ERROR);
			}
			// 总列数
			int totalCols = sheet.getRow(dataRow - 1).getPhysicalNumberOfCells();
			if (totalCols != countColumn) {
				throw new Exception(ANY_CONSTANT_FORMAT_ERROR);
			}
			// 遍历数据行
			for (int i = dataRow; i <= totalRows; i++) {
				// 取得该行
				HSSFRow row = sheet.getRow(i);
				if (null != row) {
					Map<String, Object> map = new HashMap<String, Object>();
					// 遍历列名集合
					for (int j = 0; j < colList.size(); j++) {
						// 判断是否为合并单元格数据，是，则使用获取合并单元格数据进行保存，不是，则直接取出单元格数据进行存储
						if (isMergedRegion(sheet, i, j)) {
							String cellText = getMergedRegionValue(sheet, i, j);
							map.put(colList.get(j), cellText);
							map.put(ANY_CONSTANT_SHEET_NAME, sheetName);
						} else {
							// 设置单元格类型为字符串，取出的所有数据都为字符串
							Cell cell = row.getCell(j);
							cell.setCellType(Cell.CELL_TYPE_STRING);
							map.put(colList.get(j), NullStringConvertUtil(String.valueOf(cell)));
							map.put(ANY_CONSTANT_SHEET_NAME, sheetName);
						}
					}
					result.add(map);
				}
			}
		}
		return result;
	}

	/**
	 * 空字符串处理
	 * @author 吉庆
	 * @param str 需要格式化的字符串
	 * @return 去掉空格后的串
	 */
	public static String NullStringConvertUtil(String str) {
		if (str == null || str.trim().equals("null")) {
			str = "";
		}
		return str.trim();
	}
	
	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */	
	private static String getMergedRegionValue(HSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return getCellValue(fCell);
				}
			}
		}
		return null;
	}
	
	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 * 
	 */
	public static boolean isMergedRegion(HSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return cell.getCellFormula();
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		}
		return "";
	}
	

	/**
	 * 
	 * getImportExcelTitle:(获取导入Excel文件的标题行). <br/>
	 * 
	 * @author 吉庆
	 * @param dataRow 起始行
	 * @param stream  导入数据流
	 * @param countColumn 上传excel总列数
	 * @param colList 导入文件每列对应的名称集合
	 * @return List 返回导入的Excel文件中的标题行
	 * @throws Exception
	 * date: 2018年5月23日 上午10:08:23 <br/>
	 */
	public static Map<String, Object> getImportExcelTitle(int dataRow, InputStream stream, int countColumn, List<String> colList) {
		// result存错误消息
		Map<String, Object> result = new HashMap<String, Object>();
		// title存表头
		StringBuilder title = new StringBuilder();
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(stream);
		} catch (Exception e) {
			result.put(ANY_CONSTANT_ERROR_MSG, ANY_CONSTANT_VERSION_ERROR);
		}
		// 获得该工作区的第一个sheet
		HSSFSheet sheet = wb.getSheetAt(0);
		// 总共有多少行,从0开始ʼ
		int totalRows = sheet.getLastRowNum();
		if (totalRows - dataRow < 0) {
			result.put(ANY_CONSTANT_ERROR_MSG, ANY_CONSTANT_NODATA_ERROR);
		}
		// 获得表头行索引,从0开始
		int titleRow = sheet.getFirstRowNum() + 1;
		// 总列数
		int totalCols = sheet.getRow(titleRow).getPhysicalNumberOfCells();
		if (totalCols != countColumn) {
			result.put(ANY_CONSTANT_ERROR_MSG, ANY_CONSTANT_FORMAT_ERROR);
		}
		// 获得表头内容
		if (null == result || result.isEmpty()) {
			if (titleRow >= 0 && titleRow == dataRow - 1) {
				// 获得表头行
				HSSFRow titleRowData = sheet.getRow(titleRow);
				if (null != titleRowData) {
					if (null == colList || colList.size() == 0) {
						// 取得表头行
						HSSFRow row = sheet.getRow(titleRow);
						// 循环每行的单元格
						for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
							// 判断当前单元格是否为空
							Cell cell = row.getCell(j, XSSFRow.RETURN_BLANK_AS_NULL);
							// 为空，获取合并单元格数据，拼接表头，不为空，直接拼接表头
							if (null == cell) {
								// 获取不为空的拼接
								String hbCellText = getMergedRegionValue(sheet, titleRow, j);
								title.append(NullStringConvertUtil(hbCellText));
								if (j < row.getLastCellNum() - 1) {
									title.append(",");
								}
							} else {
								// 直接拼接表头
								title.append(NullStringConvertUtil(cell.getStringCellValue()));
								if (j < row.getLastCellNum() - 1) {
									title.append(",");
								}
							}
						}
					} else {
						// 遍历列名集合
						for (int i = 0; i < colList.size(); i++) {
							// 将定义的列名代号与导入文件中的列名匹配并存储到map容器中
							result.put(colList.get(i), NullStringConvertUtil(String.valueOf(titleRowData.getCell(i))));
						}
					}
				}
			}
		}
		if (title.length() > 0) {
			result.put(ANY_CONSTANT_TITLE_STR, title.toString());
		}
		return result;
	}
}
