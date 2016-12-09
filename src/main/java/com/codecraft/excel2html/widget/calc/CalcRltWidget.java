package com.codecraft.excel2html.widget.calc;

import java.util.Map;

import com.codecraft.excel2html.cons.ExcelConstant;
import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.utils.WidgeUtils;
import com.codecraft.excel2html.widget.AbsWidget;

/**
 * 最终计算结果显示(也可以当做计算来源)
 * @description 
 * 	exps  表达式  A*B
 * 	format 格式化
 *  prec 精度(必须为正整数)
 *  rigor 严格计算.表达式A*B 其中有一个变量值为''时,计算结果为Null  (不设置此属性时,变量值为''当做0来计算) 
 * @author zoro
 */
public class CalcRltWidget extends AbsWidget {

	public static final String WIDGE_NAME = "calc".toLowerCase();

	@Override
	public String parseHtml4Edit(String widgeType, StringBuilder sb, ExcelTable table ,ExcelTableTd td) throws Exception {
		double width = td.getWidthNum();
		double height = td.getHeightNum();
		String tagId = getTagId(table, WIDGE_NAME);
		String textAlign = td.getTextAlign();
		String fontStyle = getFontStyle(td);
		
		if (isCur(widgeType)) {
			validate(widgeType);
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			int length = 0;
			
			width = width-2-2;//表格长度-padding-边框
			
			String exps = attrs.get("exps").toLowerCase();//公式
			String format = attrs.get("format")!=null? attrs.get("format").toLowerCase():"";//格式化
			String prec = attrs.get("prec")!=null? attrs.get("prec"):"";//精度precision
			String rigor = attrs.get("rigor")!=null? attrs.get("rigor"):"";//严格计算 
			//overflow:hidden;display:inline-block;white-space:nowrap;
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' showmenu='true' exps='%s' format='%s' prec='%s' rigor='%s' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all; background-color:%s; %s'size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, exps, format, prec, rigor, width, height, textAlign, ExcelConstant.WIDGET_BACKGROUND_COLOR, fontStyle, length/2));
			return WIDGE_NAME;
		} 
		return "";
	}

	@Override
	public String parseHtml4View(String widgeType, StringBuilder sb, ExcelTable table ,ExcelTableTd td) throws Exception {
		double width = td.getWidthNum();
		double height = td.getHeightNum();
		String tagId = getTagId(table, WIDGE_NAME);
		String textAlign = td.getTextAlign();
		String fontStyle = getFontStyle(td);
		
		if (isCur(widgeType)) {
			validate(widgeType);
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			int length = 0;
			width = width-2-2;//表格长度-padding-边框
			
			String exps = attrs.get("exps");//公式
			String format = attrs.get("format");//格式化
			String prec = attrs.get("prec")!=null? attrs.get("prec"):"";//精度
			String rigor = attrs.get("rigor")!=null? attrs.get("rigor"):"";//严格计算 
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' showmenu='true' exps='%s' format='%s' prec='%s' rigor='%s' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all; %s'size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, exps, format, prec, rigor, width, height, textAlign, fontStyle, length/2));
			return WIDGE_NAME;
		} 
		return "";
	}

	@Override
	public void validate(String widgeType) throws Exception{

	}

	@Override
	public boolean isCur(String widgeType){
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
