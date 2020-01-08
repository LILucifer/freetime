package com.weixiao.smart.poi;

import com.weixiao.smart.utils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtil {

	/**
	 * 读取Excel数据内容
	 * 
	 * @param is
	 *            文件流
	 * @param sheetNo
	 *            sheetNo:(1,2,3,4,5)
	 * @param startLineNo
	 *            开始行号:(1,2,3,4,5)
	 * @param tableMark
	 *            分表标志: {"info:1,2,3,4","user:3,5"}
	 * @param StringMark
	 *            字符标识
	 * @return
	 * @throws Exception
	 */
	public static Map<String, List<String>> readExcelContent(InputStream is, Integer sheetNo, Integer startLineNo, Integer endLineNo, Integer maxColNum, List<String> tableMarks, String StringMark)
			throws Exception {
		Map<String, List<String>> content = new HashMap<String, List<String>>();
		POIFSFileSystem fs;
		HSSFWorkbook wb;
		HSSFSheet sheet;
		HSSFRow row;
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(sheetNo - 1);
			row = sheet.getRow(0);
			CheckMaximum(maxColNum, row);// 校验最大列数是否正确
			// 得到总行数
			int rowNum = sheet.getLastRowNum();
			System.out.println("数据总行数为:" + rowNum);
			// 获取需要几个表
			if (rowNum != 1 && startLineNo > rowNum) {
				return content;
			}
			if (endLineNo != null && endLineNo < rowNum) {
				rowNum = endLineNo - 1;
			}
			for (String tableMark : tableMarks) {
				System.out.println("tableMark:" + tableMark);
				String[] marks = tableMark.split(":");
				String key = marks[0];
				String[] colNos = marks[1].split(",");
				List<String> list = new ArrayList<String>();
				// 正文内容应该从startLineNo行开始,第一行为表头的标题
				StringBuffer sb;

				for (int i = (startLineNo - 1); i <= rowNum; i++) {
					sb = new StringBuffer();
					try {
						System.out.println("row:" + row.getFirstCellNum() + "-" + row.getLastCellNum());
						row = sheet.getRow(i);
						System.out.println("获取行" + (i + 1));
						String str = "";
						if (StringMark != null) {
							str = sbAppend(sb, colNos, StringMark, row);
							Pattern pattern = Pattern.compile("[\\d]+");
							String indata = marks[1];
							Matcher matcher = pattern.matcher(indata);
							indata = matcher.replaceAll("");
							if (StringMark.trim().equals("','")) {
								if (str != null && (str.replaceAll("'", "").replaceAll("\\(", "").replaceAll("\\)", "").length() - 1) == indata.length()) {
									str = null;
								}
							}
						}
						else {
							str = sbAppend(sb, marks[1], row);
						}
						if (BeanUtils.isNotEmpty(str)) {
							list.add(str);
						}
					}
					catch (Exception e) {
						// TODO: handle exceptionY
						System.out.println(i + ":" + e);
						throw e;
					}
				}
				System.out.println(key + "数据为:" + list.size() + "条");
				content.put(key, list);
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			is.close();
		}
		return content;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Map<String, Object>> readExcelContent(InputStream is, Integer sheetNo, Integer startLineNo, Integer maxColNum, Map<Class, Map<String, String>> tableMap) throws Exception {
		Map<String, Map<String, Object>> content = new LinkedHashMap<String, Map<String, Object>>();
		POIFSFileSystem fs;
		HSSFWorkbook wb;
		HSSFSheet sheet;
		HSSFRow row=null;
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
			row = sheet.getRow(0);
			CheckMaximum(maxColNum, row);// 校验最大列数是否正确
			// 得到总行数
			int rowNum = sheet.getLastRowNum();
			// System.out.println("数据总行数为:" + (rowNum-1));
			// 正文内容应该从startLineNo行开始,第一行为表头的标题
			for (int i = 1; i <= rowNum; i++) {
				row = sheet.getRow(i);
				// System.out.println("获取行"+i);
				// System.out.println("row:"+row.getFirstCellNum()+"-"+row.getLastCellNum());
				Map<String, Object> data = new HashMap<String, Object>();
				for (Map.Entry<Class, Map<String, String>> tableEntry : tableMap.entrySet()) {
					Map<String, String> colMap = new HashMap<String, String>();
					Class clazz = tableEntry.getKey();
					Class parentClazz = clazz.getSuperclass();
					Map<String, Field> parentField = new HashMap<String, Field>();
					if (BeanUtils.isNotEmpty(parentClazz)) {
						Field[] Fields = parentClazz.getDeclaredFields();
						for (Field field : Fields) {
							parentField.put(field.getName(), field);
						}
					}
					List<Object> listContent = new ArrayList<Object>();
					colMap = tableEntry.getValue();
					Object object = clazz.newInstance();
					for (Map.Entry<String, String> colEntry : colMap.entrySet()) {
						// Field colName = clazz.getDeclaredField(colEntry.getKey());
						Field colName = null;
						if (parentField.containsKey(colEntry.getKey())) {
							colName = parentField.get(colEntry.getKey());
						}
						else {
							colName = clazz.getDeclaredField(colEntry.getKey());
						}
						colName.setAccessible(true);
						String colValue = null;
						String colMark = colEntry.getValue();
						if (BeanUtils.isNotEmpty(colMark)) {
							if (colMark.contains("+")) {
								HSSFCell cell = row.getCell(Integer.valueOf(colMark.split("\\+")[0]) - 1);
								HSSFCell cell1 = row.getCell(Integer.valueOf(colMark.split("\\+")[1]) - 1);
								if (cell != null && cell1 != null) {
									colValue = cell.toString().trim() + "-" + cell1.toString().trim();
								}
								else {
									throw new RuntimeException("CellOfNull:第" + row.getRowNum() + "行,第" + (cell != null ? colMark.split("\\+")[1] : colMark.split("\\+")[0]) + "列没有数据");
								}
							}
							else {
								HSSFCell cell = row.getCell(Integer.valueOf(colMark) - 1);
								if (BeanUtils.isNotEmpty(cell)) {
									colValue = cell.toString().trim();
								}
							}
						}

						String type = colName.getType().toString();
						if (type.endsWith("String")) {
							colName.set(object, colValue);
						}
						if (type.endsWith("int") || type.endsWith("Integer")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, Integer.valueOf(colValue));
							}
						}
						if (type.endsWith("float") || type.endsWith("Float")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, Float.valueOf(colValue));
							}
						}
						if (type.endsWith("Short")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, Short.valueOf(colValue));
							}
						}
						if (type.endsWith("Double")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, Double.valueOf(colValue));
							}
						}
						if (type.endsWith("BigDecimal")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, new BigDecimal(colValue));
							}
						}
						if (type.endsWith("boolean") || type.endsWith("Boolean")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, Boolean.valueOf(colValue));
							}
						}
						if (type.endsWith("Date")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								if (!"00000000".equals(colValue)) {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
									Date date = sdf.parse(colValue);
									colName.set(object, date);
								}
							}
						}
					}
					data.put(clazz.toString(), object);
				}
				content.put(String.valueOf(i), data);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().contains("java.lang.NullPointerException")) {
				if (row != null) {
					e = new Exception("导入数据异常，文件操作失败！第" + row.getRowNum()+ "行有数据为空！请检查");
				}
			}
			throw e;
		}
		finally {
			is.close();
		}
		return content;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Map<String, Object>> readExcelContentXSS(InputStream is, Integer sheetNo, Integer startLineNo, Integer maxColNum, Map<Class, Map<String, String>> tableMap) throws Exception {
		Map<String, Map<String, Object>> content = new LinkedHashMap<String, Map<String, Object>>();
		XSSFWorkbook wb;
		XSSFSheet sheet;
		XSSFRow row=null;
		try {
			wb = new XSSFWorkbook(is);
			sheet = wb.getSheetAt(0);
			row = sheet.getRow(0);
			CheckMaximumXSS(maxColNum, row);// 校验最大列数是否正确
			// 得到总行数
			int rowNum = sheet.getLastRowNum();
			// System.out.println("数据总行数为:" + (rowNum-1));
			// 正文内容应该从startLineNo行开始,第一行为表头的标题
			for (int i = 1; i <= rowNum; i++) {
				row = sheet.getRow(i);
				// System.out.println("获取行"+i);
				// System.out.println("row:"+row.getFirstCellNum()+"-"+row.getLastCellNum());
				Map<String, Object> data = new HashMap<String, Object>();
				for (Map.Entry<Class, Map<String, String>> tableEntry : tableMap.entrySet()) {
					Map<String, String> colMap = new HashMap<String, String>();
					Class clazz = tableEntry.getKey();
					Class parentClazz = clazz.getSuperclass();
					Map<String, Field> parentField = new HashMap<String, Field>();
					if (BeanUtils.isNotEmpty(parentClazz)) {
						Field[] Fields = parentClazz.getDeclaredFields();
						for (Field field : Fields) {
							parentField.put(field.getName(), field);
						}
					}
					List<Object> listContent = new ArrayList<Object>();
					colMap = tableEntry.getValue();
					Object object = clazz.newInstance();
					for (Map.Entry<String, String> colEntry : colMap.entrySet()) {
						// Field colName = clazz.getDeclaredField(colEntry.getKey());
						Field colName = null;
						if (parentField.containsKey(colEntry.getKey())) {
							colName = parentField.get(colEntry.getKey());
						}
						else {
							colName = clazz.getDeclaredField(colEntry.getKey());
						}
						colName.setAccessible(true);
						String colValue = null;
						String colMark = colEntry.getValue();
						if (BeanUtils.isNotEmpty(colMark)) {
							if (colMark.contains("+")) {
								XSSFCell cell = row.getCell(Integer.valueOf(colMark.split("\\+")[0]) - 1);
								XSSFCell cell1 = row.getCell(Integer.valueOf(colMark.split("\\+")[1]) - 1);
								if (cell != null && cell1 != null) {
									colValue = cell.toString().trim() + "-" + cell1.toString().trim();
								}
								else {
									throw new RuntimeException("CellOfNull:第" + row.getRowNum() + "行,第" + (cell != null ? colMark.split("\\+")[1] : colMark.split("\\+")[0]) + "列没有数据");
								}
							}
							else {
								XSSFCell cell = row.getCell(Integer.valueOf(colMark) - 1);
								if (BeanUtils.isNotEmpty(cell)) {
									colValue = cell.toString().trim();
								}
							}
						}

						String type = colName.getType().toString();
						if (type.endsWith("String")) {
							colName.set(object, colValue);
						}
						if (type.endsWith("int") || type.endsWith("Integer")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, Integer.valueOf(colValue));
							}
						}
						if (type.endsWith("float") || type.endsWith("Float")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, Float.valueOf(colValue));
							}
						}
						if (type.endsWith("Short")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, Short.valueOf(colValue));
							}
						}
						if (type.endsWith("Double")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, Double.valueOf(colValue));
							}
						}
						if (type.endsWith("BigDecimal")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, new BigDecimal(colValue));
							}
						}
						if (type.endsWith("boolean") || type.endsWith("Boolean")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								colName.set(object, Boolean.valueOf(colValue));
							}
						}
						if (type.endsWith("Date")) {
							if (BeanUtils.isNotEmpty(colValue)) {
								if (!"00000000".equals(colValue)) {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
									Date date = sdf.parse(colValue);
									colName.set(object, date);
								}
							}
						}
					}
					data.put(clazz.toString(), object);
				}
				content.put(String.valueOf(i), data);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().contains("java.lang.NullPointerException")) {
				if (row != null) {
					e = new Exception("导入数据异常，文件操作失败！第" + row.getRowNum()+ "行有数据为空！请检查");
				}
			}
			throw e;
		}
		finally {
			is.close();
		}
		return content;
	}


	public static String toFirstLetterUpperCase(String str) {
		if (str == null) {
			return str;
		}
		String firstLetter = str.substring(0, 1).toUpperCase();
		return firstLetter + str.substring(1, str.length());
	}

	/**
	 * 将设置的数据字段分别拿出来
	 * 
	 * @param sb
	 *            字符流
	 * @param colNos
	 *            设置的列号
	 */
	protected static String sbAppend(StringBuffer sb, String colNums, HSSFRow row) throws Exception {
		Pattern pattern = Pattern.compile("[\\d]+");
		Matcher matcher = pattern.matcher(colNums);
		while (matcher.find()) {
			HSSFCell cell = row.getCell(Integer.valueOf(matcher.group()) - 1);
			if (cell != null) {
				matcher.appendReplacement(sb, cell.toString().trim());
			}
			else {
				matcher.appendReplacement(sb, "");
			}
		}
		matcher.appendTail(sb);
		colNums = matcher.replaceAll("");
		System.out.println("行数据为: " + sb);
		if (colNums.equals(sb.toString())) {// 如果是空则不记录
			return null;
		}
		else {
			return sb.toString();
		}
	}

	/**
	 * 将设置的数据字段分别拿出来
	 * 
	 * @param sb
	 *            字符流
	 * @param colNos
	 *            设置的列号
	 */
	protected static String sbAppend(StringBuffer sb, String[] colNos, String mark, HSSFRow row) throws Exception {
		for (String no : colNos) {// 根据传入的列数取值
			if (no.contains("+")) {
				HSSFCell cell = row.getCell(Integer.valueOf(no.split("\\+")[0]) - 1);
				HSSFCell cell1 = row.getCell(Integer.valueOf(no.split("\\+")[1]) - 1);
				if (cell != null && cell1 != null) {
					sb.append(cell.toString().trim() + "-" + cell1.toString().trim() + mark);
				}
				else {
					sb.append(mark);
					System.out.println("ERROR-CellOfNull:第" + (row.getRowNum() + 1) + "行,第" + (cell != null ? no.split("\\+")[1] : no.split("\\+")[0]) + "列没有数据");
				}
			}
			else if (no.toLowerCase().contains("select")) {
				Pattern pattern = Pattern.compile("[\\d]+");
				Matcher matcher = pattern.matcher(no);
				if (sb.length() > 0 && mark.equals("','")) {
					sb = new StringBuffer(sb.toString().substring(0, sb.length() - 1));
				}
				sb = sb.append("(");
				while (matcher.find()) {
					HSSFCell cell = row.getCell(Integer.valueOf(matcher.group()) - 1);
					if (cell != null) {
						matcher.appendReplacement(sb, "'" + cell.toString().trim() + "'");
					}
					else {
						matcher.appendReplacement(sb, "''");
					}
				}
				matcher.appendTail(sb);
				if (mark.equals("','")) {
					sb.append("),'");
				}
				else {
					sb.append(")" + mark);
				}

			}
			else {
				HSSFCell cell = row.getCell(Integer.valueOf(no) - 1);
				if (cell != null) {
					sb.append(cell.toString().trim() + mark);
				}
				else {
					sb.append(mark);
				}
			}
		}
		System.out.println("行数据为" + sb);
		if (BeanUtils.isEmpty(sb.toString().replace(mark, ""))) {// 如果是空则不记录
			return null;
		}
		else {
			return (sb.substring(0, sb.length() - 1));// 去除最后的标记
		}
	}

	/**
	 * 校验最大列数
	 * 
	 * @param maxColNum
	 * @throws Exception
	 */
	protected static void CheckMaximum(Integer maxColNum, HSSFRow row) throws Exception {
		// 总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		List<String> title = new ArrayList<String>();
		for (int i = 0; i < colNum; i++) {
			if (BeanUtils.isNotEmpty(row.getCell(i).toString())) {
				title.add(row.getCell(i).toString());
			}
		}
		colNum = title.size();
		if (maxColNum != colNum) {// 列数包括最大列并且不能超过设置列
			throw new RuntimeException("colNum error:文档列数不符合规则,文档最大列数为:" + colNum);
		}
	}
	/**
	 * 校验最大列数
	 *
	 * @param maxColNum
	 * @throws Exception
	 */
	protected static void CheckMaximumXSS(Integer maxColNum, XSSFRow row) throws Exception {
		// 总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		List<String> title = new ArrayList<String>();
		for (int i = 0; i < colNum; i++) {
			if (BeanUtils.isNotEmpty(row.getCell(i).toString())) {
				title.add(row.getCell(i).toString());
			}
		}
		colNum = title.size();
		if (maxColNum != colNum) {// 列数包括最大列并且不能超过设置列
			throw new RuntimeException("colNum error:文档列数不符合规则,文档最大列数为:" + colNum);
		}
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			InputStream is = new FileInputStream("E:\\importExcel\\weihugongdan.xls");
			ExcelUtil util = new ExcelUtil();
			List<String> list = new ArrayList<String>();
			String pm_id = "SELECT pm_id FROM pm_project_info WHERE proj_id=1";
			list.add("workOrder:3,1,3,4,5,7,8,9,10,11,12,13,14,15,16,17,18,19,20,select * form pm where id=21,22," + "" + pm_id + "," + pm_id + "");
			util.readExcelContent(is, 1, 2, null, 22, list, "','");
		}
		catch (Exception e) {
			if (e.toString().contains("java.io.FileNotFoundException")) {
				System.out.println("系统找不到指定的文件!");
			}
			else if (e.toString().contains("org.apache.poi.poifs.filesystem.OfficeXmlFileException: The supplied data appears to be in the Office 2007+ XML")) {
				System.out.println("请改成xls格式文件!");
			}
			else if (e.toString().contains("java.lang.RuntimeException: colNum error")) {
				System.out.println("文档列数不符合规则!");
			}
			else if (e.toString().contains("java.lang.RuntimeException: CellOfNull")) {
				System.out.println("文档列数为null!");
			}
			e.printStackTrace();
		}
	}
}
