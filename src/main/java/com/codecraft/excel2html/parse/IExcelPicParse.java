package com.codecraft.excel2html.parse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.codecraft.excel2html.entity.ExcelTable;

/**
 * excel图片解析器
 * @author zoro
 *
 */
public interface IExcelPicParse {
	public void parsePic(HSSFWorkbook workbook, ExcelTable table) throws Exception;
}
