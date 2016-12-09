package com.codecraft.excel2html.entity;
/**
 * excel header or footer
 * @author zoro
 */
public class ExcelHeader {
	private String headerType;//类型: header footer
	
	private String leftContent = "";//居左
	
	private String centerContent = "";//居中
	
	private String rightContent = "";//居右

	public String getHeaderType() {
		return headerType;
	}

	public void setHeaderType(String headerType) {
		this.headerType = headerType;
	}

	public String getLeftContent() {
		return leftContent;
	}

	public void setLeftContent(String leftContent) {
		this.leftContent = leftContent;
	}

	public String getCenterContent() {
		return centerContent;
	}

	public void setCenterContent(String centerContent) {
		this.centerContent = centerContent;
	}

	public String getRightContent() {
		return rightContent;
	}

	public void setRightContent(String rightContent) {
		this.rightContent = rightContent;
	}
	
	public String getContent(){
		if(!"".equals(leftContent)){
			return leftContent;
		}else if(!"".equals(centerContent)){
			return centerContent;
		}else if(!"".equals(rightContent)){
			return rightContent;
		}
		return "";
	}
	

}
