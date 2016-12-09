package com.codecraft.excel2html.htmlPrint;

/**
 * 控制台输出
 * @author zoro
 *
 */
public class ConsolePrint implements IHtmlPrint {

	@Override
	public void print(String htmlContent) throws Exception {
		System.out.println(htmlContent);
	}
}
