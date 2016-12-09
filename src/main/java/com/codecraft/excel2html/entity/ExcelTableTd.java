package com.codecraft.excel2html.entity;


/**
 * 注意:width是sheet.getColumnWidthInPixels方法取得的,而此方法只能在xls格式默认字体为宋体10pt时才会正确使用.(重要)
 * 
 * getColumnWidthInPixels:
 * Please note, that this method works correctly only for workbooks with the default font size (Arial 10pt for .xls and Calibri 11pt for .xlsx).
 * 
 * 
 * excel table td
 * @author zoro
 *
 */
public class ExcelTableTd {
	private int colNum = -1;//所属第几列
	
	private String width = "";//宽度(带单位)
	
	private double widthNum = -1;//width - 单位 = widthNum
	
	private String height="";//控件的高度(带单位),不是td的高度
	
	private double heightNum = -1;//height - 单位 = heightNum
	
	private String colspan = "";//横跨的列数
	
	private String rowspan = "1";//横跨的行数
	
	private String tdAlign = "";    //td的align
	
	private String textAlign = "";//text的水平align(left center right)
	
	private String verAlign = "";//text的垂直align(bottom center top)
	
	private int fontSize = 0;//字体大小
	
	private boolean isEditor = false;//是否是编辑器，如果包含就把 align 去掉
	
	private boolean isComponet = false;//是否包含控件 （包括inut span div editor）
	
	private String content = "";//td内容
	
	private ExcelTableTdStyle tdStyle = new ExcelTableTdStyle();//td样式

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getColspan() {
		return colspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public String getRowspan() {
		return rowspan;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public String getTdAlign() {
		return tdAlign;
	}

	public void setTdAlign(String tdAlign) {
		this.tdAlign = tdAlign;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}
	
	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public boolean isEditor() {
		return isEditor;
	}

	public void setEditor(boolean isEditor) {
		this.isEditor = isEditor;
	}

	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	public boolean isComponet() {
		return isComponet;
	}

	public void setComponet(boolean isComponet) {
		this.isComponet = isComponet;
	}

	public String getVerAlign() {
		return verAlign;
	}

	public void setVerAlign(String verAlign) {
		this.verAlign = verAlign;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public double getWidthNum() {
		return widthNum;
	}

	public void setWidthNum(double widthNum) {
		this.widthNum = widthNum;
	}

	public double getHeightNum() {
		return heightNum;
	}

	public void setHeightNum(double heightNum) {
		this.heightNum = heightNum;
	}

	public ExcelTableTdStyle getTdStyle() {
		return tdStyle;
	}

	public void setTdStyle(ExcelTableTdStyle tdStyle) {
		this.tdStyle = tdStyle;
	}
}
