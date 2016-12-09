package com.codecraft.excel2html.htmlPrint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件输出
 * @author zoro
 *
 */
public class FilePrint implements IHtmlPrint {
	
	private String dirPath = "c:/temp";
	
	private String fileName = "excel2html";
	
	private static final String FILE_TYPE = ".html";
	
	public FilePrint(){
		
	}
	
	public FilePrint(String fileName){
	 	this.fileName = fileName;
	}
	
	public FilePrint(String fileName, String dirPath){
	 	this.fileName = fileName;
	 	this.dirPath = dirPath;
	}
	
	@Override
	public void print(String htmlContent) throws Exception {
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");
		String dateStr = sdf.format(new Date());
		
		File printFile = new File(dir + File.separator + fileName + "-" + dateStr + FILE_TYPE );
		
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try{
			fos = new FileOutputStream(printFile);
			osw =  new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			bw.write(htmlContent);
			bw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(bw != null){
				bw.close();
			}
			if(osw != null){
				osw.close();
			}
			if(fos != null){
				fos.close();
			}
		}
	}
}
