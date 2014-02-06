package com.scm.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelTool {
	private static final Log log = LogFactory.getLog(ExcelTool.class);

	public static void exportExcel(HttpServletRequest request,
			HttpServletResponse response, String fileName,
			LinkedHashMap<String, String> propsMap, List<LinkedHashMap<String, Object>> dataList)
			throws UnsupportedEncodingException {

		// tell browser program going to return an application file 
        // instead of html page
		response.setHeader("Content-Disposition", "attachment;filename=" + T.toUTF8(fileName));
		response.setHeader("Connection", "close");
		response.setContentType("application/ms-excel,charset=GBK");
		
		try
		{
			HSSFWorkbook book =  new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet(fileName);	// 第一个表格
			HSSFRow row = sheet.createRow(0);	// 第一行，为表头
			HSSFCell cell = null;
			int cellNum = 0;
			
			List<String> keyList = new ArrayList<String>();
			Iterator it = propsMap.keySet().iterator();
			while (it.hasNext()){
				String key = (String) it.next();
				cell = row.createCell(cellNum++);
				cell.setCellValue(propsMap.get(key));
				keyList.add(key);
			}
			
			for (int i=0; i < dataList.size(); i++){
				Map<String, Object> map = dataList.get(i);
				cellNum = 0;
				row = sheet.createRow(i + 1);
				for (String key : keyList){
					cell = row.createCell(cellNum++);
					Object v = map.get(key);
					if (v instanceof String)
						cell.setCellValue((String)v);
					else if (v instanceof Integer || v instanceof Long || v instanceof Short || v instanceof Float){
						cell.setCellValue(Double.parseDouble(v.toString()));
					}else if (v instanceof Double){
						cell.setCellValue((Double)v);
					}else{
						cell.setCellValue(v.toString());
					}
				}
			}
			
			ServletOutputStream sos = response.getOutputStream();
			book.write(sos);
			sos.flush();
			sos.close();
			
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}
}