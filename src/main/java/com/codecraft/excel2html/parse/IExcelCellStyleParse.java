package com.codecraft.excel2html.parse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.entity.ExcelTableTdStyle;
import com.codecraft.excel2html.entity.ExcelTableTr;
/**
 * excel样式解析器(字体,颜色,边框,对齐方式)
 * @author zoro
 *
 */
public interface IExcelCellStyleParse {
	public void parseStyle(Workbook workbook, Sheet sheet, Row row, Cell cell,
			int bottomeRow, int bottomeCol, CellStyle cellStyle,
			ExcelTable table, ExcelTableTr tr, ExcelTableTd td, ExcelTableTdStyle styleBo)
			throws Exception;
}
