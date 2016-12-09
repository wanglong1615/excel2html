package com.codecraft.excel2html.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.codecraft.excel2html.config.ConvertConfig;
import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.entity.ExcelTableTdStyle;
import com.codecraft.excel2html.entity.ExcelTableTr;
import com.codecraft.excel2html.entity.RowColumnSpan;
import com.codecraft.excel2html.utils.ExcelUtils;
import com.codecraft.excel2html.utils.StringsUtils;

/**
 * hssf excel的解析器
 * @author zoro
 *
 */
public class ExcelParse4HSSF implements IExcelParse {
	
	private ConvertConfig config = null;

	@Override
	public ExcelTable parse(InputStream input, ConvertConfig config) throws Exception{
		this.config = config;
		return parse(input, 0, config);
	}

	@Override
	public ExcelTable parse(InputStream input, int sheetIndex, ConvertConfig config) throws Exception{
		this.config = config;
		ExcelTable table = parseTable(input, sheetIndex);
		table.setConfig(config);
		return table;
	}
	
	/**
	 * 解析table
	 * @param input
	 * @param sheetIndex
	 * @return
	 * @throws Exception
	 */
	public ExcelTable parseTable(InputStream input, int sheetIndex) throws Exception{
		Workbook wk = ExcelUtils.getExcelWorkBook(input);
		HSSFWorkbook workbook = null;
		if(wk instanceof HSSFWorkbook){
			workbook = (HSSFWorkbook)wk;
		}else{
			throw new Exception("目前只支持HSSF格式!");
		}
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum-firstRowNum > config.getMaxRowNum()) {
			// 行数不允许多于300
			Exception e = new Exception("行数不能超过" + config.getMaxRowNum() + "行!");
			throw e;
		}
		ExcelTable table = new ExcelTable();
		table.setFirstRowNum(firstRowNum);//设置起始行
		table.setLastRowNum(lastRowNum);//设置结束行
		
		parseTableTr(workbook, table, sheet);//解析table tr
		return table;
	}
	
	/**
	 * 解析tr
	 * @param workbook
	 * @param table
	 * @param sheet
	 * @throws Exception
	 */
	private void parseTableTr(HSSFWorkbook workbook, ExcelTable table,
			Sheet sheet) throws Exception{
		//解析跨行 跨列信息
		RowColumnSpan rowColumnSpan = parseRowColumnSpan(sheet);
		table.setRowColumnSpan(rowColumnSpan);
		int firstRow = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		for (int rowNum = firstRow ; rowNum <= lastRowNum; rowNum++) {
			Row row = sheet.getRow(rowNum);
			ExcelTableTr tr = new ExcelTableTr();
			if (row == null) {
				tr.setRowNum(rowNum);
				tr.setHeight("0px");
				tr.setLastColNum(-1);
				tr.getTdMap().put(0, new ExcelTableTd());
				table.getTrMap().put(rowNum, tr);
			}else{
				//空白行会出现lastCellNum为-1
				if(row.getLastCellNum() == -1){
					continue;
				}
				//设置实际的有效行
				if(row != null && table.getWidthRowNum() == -1){
					table.setWidthRowNum(rowNum);
				}
				tr.setHeight((int)(row.getHeightInPoints() * 1.35) + "px");
				parseTableTd(workbook, sheet, row, table, tr, rowColumnSpan);
				table.getTrMap().put(rowNum, tr);
			}
		}
	}

	/**
	 * 解析td
	 * @param workbook
	 * @param sheet
	 * @param row
	 * @param table
	 * @param tr
	 * @param rowColumnSpanBo
	 * @throws Exception
	 */
	private void parseTableTd(HSSFWorkbook workbook, Sheet sheet, Row row,
			ExcelTable table, ExcelTableTr tr,
			RowColumnSpan rowColumnSpanBo) throws Exception{
		int rowNum = row.getRowNum();
		int firstColNum = row.getFirstCellNum();
		int lastColNum = row.getLastCellNum();
		if (lastColNum - firstColNum > config.getMaxCellNum()) {
			// 列数不允许多于300
			Exception e = new Exception("excel列数不能超过" + config.getMaxCellNum() + "行!");
			throw e;
		}
		Cell cell = null;
		tr.setLastColNum(lastColNum);
		for (int colNum = 0; colNum <= lastColNum; colNum++) {
			ExcelTableTd td = new ExcelTableTd();
			td.setColNum(colNum);
			cell = row.getCell(colNum);
			if (cell == null) {
				td.setContent("&nbsp;");
				tr.getTdMap().put(colNum, td);
				continue;
			}
			int bottomeCol = -1;
			int bottomeRow = -1;
			//当前td是否跨行跨列
			if (rowColumnSpanBo.getRowColunmSpanMap().containsKey(rowNum + "," + colNum)) {
				String pointString = rowColumnSpanBo.getRowColunmSpanMap().get(rowNum + "," + colNum);
				rowColumnSpanBo.getRowColunmSpanMap().remove(rowNum + "," + colNum);
				bottomeRow = Integer.valueOf(pointString.split(",")[0]);
				bottomeCol = Integer.valueOf(pointString.split(",")[1]);

				int colSpan = bottomeCol - colNum + 1;
				//Please note, that this method works correctly only for workbooks with the default font size (Arial 10pt for .xls and Calibri 11pt for .xlsx). 
				//If the default font is changed the column width can be streched
				double tdWidth = sheet.getColumnWidthInPixels(colNum);
				
				td.setWidth(tdWidth + "px");
				td.setWidthNum(tdWidth);
				int rowSpan = bottomeRow - rowNum + 1;
				if (rowSpan != 1) {
					td.setRowspan(rowSpan + "");
				}
				if (colSpan != 1) {
					td.setColspan(colSpan + "");
				}
			}else {
				//如果输出的宽度有问题,那么肯定是设置的默认字体大小的问题
				double tdwidth = sheet.getColumnWidthInPixels(colNum);
				td.setWidth(tdwidth + "px");
			}
			parseTdStyle(workbook, sheet, row, cell, bottomeRow, bottomeCol,
					table, tr, td);
			//解析内容
			String content =getCellContent(cell);
			td.setContent(content);
			tr.getTdMap().put(colNum, td);
		}
	}
	
	/**
	 * 获得cell内容
	 * @param cell
	 * @return
	 * @throws Exception
	 */
	private String getCellContent(Cell cell) throws Exception {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING://string
				String str = cell.getStringCellValue();
				if (str==null || "".equals(str)) {
					return "&nbsp;";
				} else {
					str = str.replaceAll(String.valueOf(' '),"&nbsp;");
					str = str.replaceAll(String.valueOf('<'), "&lt;");
					str = str.replaceAll(String.valueOf('>'), "&gt;");
					str = str.replaceAll(String.valueOf('\n'), "</br>");
					return str;
				}
			case Cell.CELL_TYPE_NUMERIC://numeric
				//去掉整数后的'.0'
				DecimalFormat format = new DecimalFormat("#0.##");
				String result = format.format(cell.getNumericCellValue());
				return result;
			case Cell.CELL_TYPE_BOOLEAN://boolean
				return cell.getBooleanCellValue() +"";
			case Cell.CELL_TYPE_FORMULA: // 公式  
                return "FORMULA";
			case Cell.CELL_TYPE_BLANK://blank
				return "";
			case Cell.CELL_TYPE_ERROR: //error
                return "ERROR";	
			default:
				return "";
		}
	}

	/**
	 * 解析td样式
	 * @param workbook
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param bottomeRow
	 * @param bottomeCol
	 * @param table
	 * @param tr
	 * @param td
	 * @throws Exception
	 */
	private void parseTdStyle(HSSFWorkbook workbook, Sheet sheet, Row row, Cell cell, int bottomeRow,
			int bottomeCol, ExcelTable table, ExcelTableTr tr,
			ExcelTableTd td) throws Exception{
		ExcelTableTdStyle styleBo = new ExcelTableTdStyle();
		CellStyle cellStyle = cell.getCellStyle();
		cellStyle.setWrapText(true);
		//解析字体
		parseFontStyle(workbook, td, styleBo, cellStyle);
		//解析对齐方式
		parseTdAlign(workbook, sheet, row, cell, td, cellStyle);
		//解析边框样式
		config.getExcelStyleParse().parseStyle(workbook, sheet, row, cell, bottomeRow, bottomeCol, cellStyle, table, tr, td, styleBo);
		td.setTdStyle(styleBo);
	}

	/**
	 * 解析字体样式(fontColor,fontHeight,fontName,fontItalic)
	 * @param workbook
	 * @param td
	 * @param styleBo
	 * @param cellStyle
	 */
	private void parseFontStyle(HSSFWorkbook workbook, ExcelTableTd td,
			ExcelTableTdStyle styleBo, CellStyle cellStyle) {
		short fontIndex = cellStyle.getFontIndex();
		Font font = workbook.getFontAt(fontIndex);
		HSSFPalette palette = ((HSSFWorkbook)workbook).getCustomPalette();
		HSSFColor hc = palette.getColor(font.getColor());
		String fontColorStr = ExcelUtils.convertToStardColor(hc);
		styleBo.setFontColor(fontColorStr) ;//字体颜色
		styleBo.setFontHeight(String.valueOf(font.getFontHeightInPoints()+"pt;"));//字体大小
		styleBo.setFontName(font.getFontName());//字体 （宋体,黑体）
		styleBo.setFontItalic(String.valueOf(font.getItalic()));//是否斜体
		
		String fontStr = parseFont(workbook,(HSSFCellStyle)cellStyle,palette,td);
		styleBo.setFont(fontStr);
	}
	
	/**
	 * 解析字体样式(font-size,font-family,font-weight,font-style,color)
	 * @param workbook
	 * @param row
	 * @param c
	 * @param cellStyle
	 * @param palette
	 * @param td
	 * @return
	 * @throws Exception
	 */
	private String parseFont(HSSFWorkbook workbook, HSSFCellStyle cellStyle,
			HSSFPalette palette, ExcelTableTd td){
		StringBuilder sb = new StringBuilder();
		HSSFFont hf = cellStyle.getFont(workbook);
		short fontColor = hf.getColor();
		HSSFColor hc = palette.getColor(fontColor);
		if (hf.getFontHeightInPoints() > 0) {
			sb.append("font-size: " + hf.getFontHeightInPoints() + "pt;");
			td.setFontSize(hf.getFontHeightInPoints());
		}
		if (!StringsUtils.isEmpty(hf.getFontName())) {
			sb.append("font-family:" + hf.getFontName() + ";");
		}
		if (hf.getBoldweight() > 0) {
			sb.append("font-weight:" + hf.getBoldweight() + ";");
		}
		if (hf.getItalic()) {
			sb.append("font-style:italic;");
		}
		String fontColorStr = ExcelUtils.convertToStardColor(hc);
		if (fontColorStr != null && !"".equals(fontColorStr.trim())) {
			sb.append("color:" + fontColorStr + ";"); // 字体颜色
		}
		return sb.toString();
	}

	/**
	 * 解析对齐方式
	 * @param workbook
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param td
	 * @param cellStyle
	 * @throws Exception
	 */
	private void parseTdAlign(HSSFWorkbook workbook, Sheet sheet, Row row,
			Cell cell, ExcelTableTd td, CellStyle cellStyle) throws Exception {
		if (cellStyle != null) {
			//td align
			String tdAlign = alignment2Html( workbook, sheet, row, cell, cellStyle);
			td.setTdAlign(tdAlign);
			//text align
			String textAlign = getTextAlign2Html(workbook, sheet, row, cell, cellStyle);
			td.setTextAlign(textAlign);
			//ver Align
			String vAlign = getVerAlign2Html(workbook, sheet, row, cell, cellStyle);
			td.setVerAlign(vAlign);
		}
	}
	/**
	 * 获取垂直对齐方式
	 * @param workbook
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param cellStyle
	 * @return
	 */
	private String getVerAlign2Html(Workbook workbook, Sheet sheet, Row row, Cell cell,
			CellStyle cellStyle) {
		String vAlign = "right";
		short verticalAlignment = cellStyle.getVerticalAlignment();
		if (verticalAlignment > 0) {
			vAlign = ExcelUtils.convertVerticalAlignToHtml(verticalAlignment);
		}
		return vAlign;
	}

	/**
	 * 获取文本对齐方式
	 * @param workbook
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param cellStyle
	 * @return
	 */
	private String getTextAlign2Html(Workbook workbook, Sheet sheet, Row row, Cell cell,
			CellStyle cellStyle) {
		String textAlign = "right";
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			short alignment = cellStyle.getAlignment();
			if (cellStyle.getAlignment() > 0) {
				textAlign = ExcelUtils.convertAlignToHtml(alignment);
			} 
		} else {
			short alignment = cellStyle.getAlignment();
			if (cellStyle.getAlignment() > 0) {
				textAlign = ExcelUtils.convertAlignToHtml(alignment);
			}
		}
		return textAlign;
	}
	
	/**
	 * 获取水平对齐方式
	 * @param workbook
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param cellStyle
	 * @return
	 * @throws Exception
	 */
	private String alignment2Html(Workbook workbook,
			Sheet sheet, Row row, Cell cell, CellStyle cellStyle)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			short alignment = cellStyle.getAlignment();
			if (cellStyle.getAlignment() > 0) {
				sb.append(" align='" + ExcelUtils.convertAlignToHtml(alignment) + "'");
			} else {
				sb.append(" align='right'");
			}
		} else {
			short alignment = cellStyle.getAlignment();
			if (cellStyle.getAlignment() > 0) {
				sb.append(" align='" + ExcelUtils.convertAlignToHtml(alignment) + "'");
			}
		}
		short verticalAlignment = cellStyle.getVerticalAlignment();
		//垂直居中居上时,verticalAlignment为0
		if (verticalAlignment > -1) {
			sb.append(" valign='"
					+ ExcelUtils.convertVerticalAlignToHtml(verticalAlignment) + "' ");
		}
		return sb.toString();
	}

	/**
	 * 解析跨行 跨列信息
	 * @param sheet
	 * @return
	 */
	private RowColumnSpan parseRowColumnSpan(Sheet sheet) {
		RowColumnSpan bo = new RowColumnSpan();
		int mergedRegions = sheet.getNumMergedRegions();
		for (int i = 0; i < mergedRegions; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int topRow = range.getFirstRow();
			int topCol = range.getFirstColumn(); 
			int bottomRow = range.getLastRow();
			int bottomCol = range.getLastColumn();
			bo.getRowColunmSpanMap().put(topRow + "," + topCol, bottomRow + "," + bottomCol);
			
			Set<String> filter = bo.getRowColunmSpanFilter();
			for(int m = topRow; m <= bottomRow; m++){
				for(int n = topCol; n <= bottomCol; n++){
					if(m == topRow && n == topCol){
						continue;
					}else{
						filter.add(m + "," + n);
					}
				}
			}
		}
		return bo;
	}
	
	//test excel width
	public static void main(String[] args) throws Exception {
		FileInputStream fis = new FileInputStream(new File("C:/Temp/test.xls"));
		HSSFWorkbook workbook = (HSSFWorkbook)ExcelUtils.getExcelWorkBook(fis);
		Sheet sheet = workbook.getSheetAt(0);
		
		int firstNum = sheet.getFirstRowNum();
		int lastNum = sheet.getLastRowNum();
		
		for(int i = firstNum; i <= lastNum; i++ ){
			Row row = sheet.getRow(i);
			
			int firstCellNum = row.getFirstCellNum();
			int lastCellNum = row.getLastCellNum();
			
			for(int j = firstCellNum; j <= lastCellNum; j++){
				 float width = sheet.getColumnWidthInPixels(j);
				 System.out.println("第" + i + "行,第" + j + "列宽度:" + width);
			}
		}
		workbook.close();
	}
}
