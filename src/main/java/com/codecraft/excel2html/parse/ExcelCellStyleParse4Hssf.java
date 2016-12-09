package com.codecraft.excel2html.parse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.entity.ExcelTableTdStyle;
import com.codecraft.excel2html.entity.ExcelTableTr;
import com.codecraft.excel2html.utils.ExcelUtils;
import com.codecraft.excel2html.utils.StringsUtils;

/**
 * 解析字体样式 表格样式 
 * @author zoro
 *
 */
public class ExcelCellStyleParse4Hssf implements IExcelCellStyleParse {
	
	/**
	 * 解析样式,外部调用此方法
	 */
	@Override
	public void parseStyle(Workbook wb, Sheet s, Row r,
			Cell c, int bottomRow, int bottomCell, CellStyle cs, ExcelTable table,
			ExcelTableTr tr, ExcelTableTd td,
			ExcelTableTdStyle styleBo) throws Exception {
		HSSFWorkbook workbook = (HSSFWorkbook) wb;
		HSSFSheet sheet = (HSSFSheet) s;
		HSSFRow row = (HSSFRow) r;
		HSSFCell cell = (HSSFCell) c;
		HSSFRow bottomeRow = bottomRow == -1 ? null : (HSSFRow) sheet.getRow(bottomRow);
		HSSFCell bottomeCol = bottomeRow == null ? null : bottomeRow
				.getCell(bottomCell);
		HSSFCellStyle cellStyle = (HSSFCellStyle) cs;
		HSSFPalette palette = workbook.getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式


	
		short bgColor = cellStyle.getFillForegroundColor();
		HSSFColor hc = palette.getColor(bgColor);
		String bgColorStr = ExcelUtils.convertToStardColor(hc);

		if (!StringsUtils.isEmpty(bgColorStr)) {
			styleBo.setColor("background-color:" + bgColorStr + ";"); // 背景颜色
		}
		parseBorder(row, cell, bottomeRow, bottomeCol,
				cellStyle, palette, table, tr, td, styleBo);
	}

	/**
	 * 解析边框样式
	 * @param bo
	 * @param s
	 * @param row
	 * @param cell
	 * @param bottomeRow
	 * @param bottome
	 * @param cellStyle
	 * @param palette
	 * @param table
	 * @param tr
	 * @param td
	 * @param styleBo
	 * @throws Exception
	 */
	private void parseBorder( HSSFRow row, HSSFCell cell, HSSFRow bottomeRow,
			HSSFCell bottome, HSSFCellStyle cellStyle, HSSFPalette palette,
			ExcelTable table, ExcelTableTr tr, ExcelTableTd td,
			ExcelTableTdStyle styleBo) throws Exception {
		parseBorderTop(cellStyle,styleBo);
		parseBorderLeft(cellStyle,styleBo);
		parseBorderBottom(cell, row, bottomeRow, bottome, cellStyle, styleBo);  
		parseBorderRight(cell, row, bottomeRow, bottome, cellStyle, styleBo);
	}

	private void parseBorderTop(HSSFCellStyle cellStyle,
			ExcelTableTdStyle styleBo) throws Exception {
		short bordertop = cellStyle.getBorderTop();
		if (bordertop > 0) {
			short colortop = cellStyle.getTopBorderColor();
			String boderstyler = ExcelUtils.getBorderStyle(bordertop,
					colortop);
			if (!StringsUtils.isEmpty(boderstyler)) {
				styleBo.setBorderTop("border-top:" + boderstyler + ";");
			}
		}
	}

	private void parseBorderLeft(HSSFCellStyle cellStyle,
			ExcelTableTdStyle styleBo) throws Exception {
		short borderleft = cellStyle.getBorderLeft();
		if (borderleft > 0) {
			short colorleft = cellStyle.getLeftBorderColor();
			String boderstyler = ExcelUtils.getBorderStyle(borderleft,
					colorleft);
			if (!StringsUtils.isEmpty(boderstyler)) {
				styleBo.setBorderLeft("border-left:" + boderstyler + ";");
			}
		}
	}

	private void parseBorderRight(HSSFCell cell, HSSFRow row, HSSFRow bottomeRow, Cell bottomCell,
			HSSFCellStyle cellStyle, ExcelTableTdStyle styleBo) throws Exception {
		if (bottomCell != null) {
			//跨行或跨列td
			CellStyle cs = bottomCell.getCellStyle();
			if (cs != null) {
				short borderRight = cs.getBorderRight();
				short colorRight = cs.getRightBorderColor();
				String boderStyler = ExcelUtils.getBorderStyle(borderRight,
						colorRight);
				if (!StringsUtils.isEmpty(boderStyler)) {
					styleBo.setBorderRight("border-right:" + boderStyler + ";");
				}
			}
		} else {
			//非跨行跨列td
			short borderRight = cellStyle.getBorderRight();//边框
			if (borderRight > 0) {
				short colorRight = cellStyle.getRightBorderColor();//边框颜色
				String boderStyler = ExcelUtils.getBorderStyle(borderRight,
						colorRight);
				if (!StringsUtils.isEmpty(boderStyler)) {
					styleBo.setBorderRight("border-right:" + boderStyler + ";");
				}
			}
		}
	}

	private void parseBorderBottom(HSSFCell c, HSSFRow row, HSSFRow bottomeRow, HSSFCell bottomCell,
			HSSFCellStyle cellStyle, ExcelTableTdStyle styleBo) throws Exception {
		if (bottomCell != null) {
			//跨行或跨列td
			CellStyle cs = bottomCell.getCellStyle();
			if (cs != null) {
				short borderbottom = cs.getBorderBottom();
				short colorbottom = cs.getBottomBorderColor();
				String boderstyler = ExcelUtils.getBorderStyle(borderbottom,
						colorbottom);
				if (!StringsUtils.isEmpty(boderstyler)) {
					styleBo.setBorderBottom("border-bottom:" + boderstyler
							+ ";");
				}
			}
		} else {
			//非跨行跨列td
			short borderbottom = cellStyle.getBorderBottom();
			if (borderbottom > 0) {
				short colorbottom = cellStyle.getBottomBorderColor();
				String boderstyler = ExcelUtils.getBorderStyle(borderbottom,
						colorbottom);
				if (!StringsUtils.isEmpty(boderstyler)) {
					styleBo.setBorderBottom("border-bottom:" + boderstyler
							+ ";");
				}
			}
		}
	}
}
