package com.codecraft.excel2html.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.entity.ExcelTableTr;

/**
 * excel工具类
 * @author zoro
 *
 */
public class ExcelUtils {
	/*
	 * 用于将excel表格中列索引转成列号字母，从A对应1开始
	 */
	public static String indexToColumn(int index) {
        if(index <= 0){                
        	try{                     
        		throw new Exception("Invalid parameter");                 
        	}catch (Exception e) {                         
        		e.printStackTrace();                
        	}         
        }         
        index--;         
        String column = "";         
        do{                
        	if(column.length() > 0) {
                        index--;
            }
        	column = ((char) (index % 26 + (int) 'A')) + column;
            index = (int) ((index - index % 26) / 26);
        }while(index>0);
        return column;
	}
	
	/*
	 * 计算合并列的宽度度(单位为px)
	 */
	public static double getTdSpanWidth(Sheet sheet, int startCol, int endCol) {
		double tdwidth = 0;
		for (int i = startCol; i <= endCol; i++) {
			double tempwidth = sheet.getColumnWidthInPixels(i);//获得px像素
			tdwidth = tdwidth + tempwidth;

		}
		return tdwidth;
	}
	
	/*
	 * 计算合并列的高度
	 */
	public static int getTdSpanHeight(Sheet sheet, int startRow, int endRow) {
		int tdHeight = 0;
		for (int i = startRow; i <= endRow; i++) {
			int tempHeight = sheet.getRow(i).getHeight() / 32;
			tdHeight = tdHeight + tempHeight;
		}
		return tdHeight;
	}
	
	/*
	 * 计算合并列的高度
	 */
	public static String getTdSpanHeight(ExcelTable table, ExcelTableTr tr,
			ExcelTableTd td) {
		int rowspan = 0;
		if(!"".equals(td.getRowspan())){
			rowspan = Integer.parseInt(td.getRowspan());
		}
		//计算合并单元格的高度
		int height = Integer.parseInt(tr.getHeight().substring(0, tr.getHeight().indexOf("px")));
		if(rowspan > 1){
			int thisIndex = tr.getRowNum();
			for(int i=1; i < rowspan; i++){
				ExcelTableTr trBo = table.getTrMap().get(thisIndex + i);
				if(trBo != null){
					height+=Integer.parseInt(trBo.getHeight().substring(0, trBo.getHeight().indexOf("px")));
				}
			}
		}
		return height + "px";
	}
	
	/**
	 * 根据文件的路径创建Workbook对象
	 * 
	 * @param filePath
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws Exception
	 */
	public static Workbook getExcelWorkBook(InputStream ins) throws Exception {
		Workbook book = null;
		try {
			book = WorkbookFactory.create(ins);
		}catch(IllegalArgumentException e){
			throw new Exception("Excel打开出错!");
		}
		return book;
	}
	
	/**
	 * 判断Excel版本
	 * @param filePath
	 * @return
	 */
	public static boolean isExcelHSSF(InputStream input) {
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(input);
		} catch (Exception e) {
			return false;
		} finally{
			if(workbook != null){
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	/**
	 * 转换为标准颜色
	 * @param hc
	 * @return
	 */
	public static String convertToStardColor(HSSFColor hc) {
		StringBuffer sb = new StringBuffer("");
		if (hc != null) {
			if (HSSFColor.AUTOMATIC.index == hc.getIndex()) {
				return null;
			}
			sb.append("#");
			for (int i = 0; i < hc.getTriplet().length; i++) {
				sb.append(fillWithZero(Integer.toHexString(hc
					.getTriplet()[i])));
			}
		}
		return sb.toString();
	}
	
	/**
	 * 十六进制补0
	 * @param str
	 * @return
	 */
	public static String fillWithZero(String str) {
		if (str != null && str.length() < 2) {
			return "0" + str;
		}
		return str;
	}
	
	/**
	 * 单元格水平对齐
	 * @param alignment
	 * @return
	 */
	public static String convertAlignToHtml(short alignment) {
		String align = "";
		switch (alignment) {
			case CellStyle.ALIGN_LEFT:
				align = "left";
				break;
			case CellStyle.ALIGN_CENTER:
				align = "center";
				break;
			case CellStyle.ALIGN_RIGHT:
				align = "right";
				break;
			default:
				break;
		}
		return align;
	}
	
	/**
	 * 单元格垂直对齐
	 * @param verticalAlignment
	 * @return
	 */
	public static String convertVerticalAlignToHtml(short verticalAlignment) {
		String valign = "";
		switch (verticalAlignment) {
			case CellStyle.VERTICAL_BOTTOM:
				valign = "bottom";
				break;
			case CellStyle.VERTICAL_CENTER:
				valign = "middle";
				break;
			case CellStyle.VERTICAL_TOP:
				valign = "top";
				break;
			case CellStyle.VERTICAL_JUSTIFY:
				valign = "baseline";
				break;
			default:
				break;
		}
		return valign;
	}
	
	/**
	 * 获取border样式
	 * @param borderType
	 * @param colorType
	 * @return
	 */
	public static String getBorderStyle(short borderType, short colorType) {
		String html = "none";
		switch (borderType) {
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_NONE:
			html = "none";
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN:
			html = "1px solid " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM:
			html = "2px solid " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DASHED:
			html = "1px dashed " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_HAIR:
			html = "1px solid " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_THICK:
			html = "5px solid " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DOUBLE:
			html = "double solid " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DOTTED:
			html = "1px dotted " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM_DASHED:
			html = "3px dashed " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DASH_DOT:
			html = "1px solid " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM_DASH_DOT:
			html = "3px solid " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DASH_DOT_DOT:
			html = "1px solid " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM_DASH_DOT_DOT:
			html = "3px solid " + getBorderColor(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_SLANTED_DASH_DOT:
			html = "1px solid " + getBorderColor(colorType);
			break;
		default:
			break;
		}
		return html;
	}
	
	/**
	 * 获取border颜色
	 * @param bordercolor
	 * @return
	 */
	public static String getBorderColor(short bordercolor) {
		String type = "black";
		switch (bordercolor) {
		case org.apache.poi.hssf.util.HSSFColor.AUTOMATIC.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_CORNFLOWER_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.ROYAL_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.CORAL.index:
			type = "coral";
			break;
		case org.apache.poi.hssf.util.HSSFColor.ORCHID.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.MAROON.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LEMON_CHIFFON.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.CORNFLOWER_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.WHITE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LAVENDER.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.PALE_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_TURQUOISE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_GREEN.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_YELLOW.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.TAN.index:
			type = "tan";
			break;
		case org.apache.poi.hssf.util.HSSFColor.ROSE.index:
			type = "rose";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GREY_25_PERCENT.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.PLUM.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.SKY_BLUE.index:
			type = "blue";
			break;
		case org.apache.poi.hssf.util.HSSFColor.TURQUOISE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.BRIGHT_GREEN.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.YELLOW.index:
			type = "yellow";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GOLD.index:
			type = "gold";
			break;
		case org.apache.poi.hssf.util.HSSFColor.PINK.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GREY_40_PERCENT.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.VIOLET.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.AQUA.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.SEA_GREEN.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIME.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_ORANGE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.RED.index:
			type = "red";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GREY_50_PERCENT.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.BLUE_GREY.index:
			type = "grey";
			break;
		case org.apache.poi.hssf.util.HSSFColor.BLUE.index:
			type = "blue";
			break;
		case org.apache.poi.hssf.util.HSSFColor.TEAL.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GREEN.index:
			type = "green";
			break;
		case org.apache.poi.hssf.util.HSSFColor.DARK_YELLOW.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.ORANGE.index:
			type = "orange";
			break;
		case org.apache.poi.hssf.util.HSSFColor.DARK_RED.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GREY_80_PERCENT.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.INDIGO.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.DARK_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.DARK_TEAL.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.DARK_GREEN.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.OLIVE_GREEN.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.BROWN.index:
			type = "brown";
			break;
		case org.apache.poi.hssf.util.HSSFColor.BLACK.index:
			type = "black";
			break;
		default:
			break;
		}
		return type;
	}
}
