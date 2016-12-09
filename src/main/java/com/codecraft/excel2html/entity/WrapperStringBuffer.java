package com.codecraft.excel2html.entity;

/**
 * 增加\r\n 增加script的阅读性
 * 
 * @author zoro
 *
 */
public class WrapperStringBuffer{
	private StringBuffer sb = null;
	private static final String NEW_LINE = "\r\n";
	
	public WrapperStringBuffer(StringBuffer sb){
		this.sb = sb;
	}
	
	public void append(String str){
		sb.append(str);
		//##########################提交代码需要注释掉
		sb.append(NEW_LINE);
	}
	
	public StringBuffer getStringBuffer(){
		return sb;
	}
	
	public String toString(){
		return sb.toString();
	}
}
