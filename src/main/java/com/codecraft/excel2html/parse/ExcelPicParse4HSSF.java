package com.codecraft.excel2html.parse;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.codecraft.excel2html.entity.ExcelPicture;
import com.codecraft.excel2html.entity.ExcelTable;

/**
 * 图片解析器
 * @author zoro
 *
 */
public class ExcelPicParse4HSSF implements IExcelPicParse {
	
    private static class ClientAnchorInfo {
        public HSSFSheet sheet;
        public EscherClientAnchorRecord clientAnchorRecord;
	        
        public ClientAnchorInfo(HSSFSheet sheet, EscherClientAnchorRecord clientAnchorRecord) {
            super();
            this.sheet = sheet;
            this.clientAnchorRecord = clientAnchorRecord;
        }
    }
    
	@Override
	public void parsePic(HSSFWorkbook workbook, ExcelTable table) throws Exception {
		//处理图片
		List<ExcelPicture> list = new ArrayList<ExcelPicture>();

		HSSFWorkbook hssfWorkbook = (HSSFWorkbook)workbook;
        List<HSSFPictureData> pictureList = hssfWorkbook.getAllPictures();
        List<ClientAnchorInfo> clientAnchorRecords = getClientAnchorRecords((HSSFWorkbook)workbook);
        
        if (pictureList.size() != clientAnchorRecords.size()) {
            throw new Exception("图片数量不一致!");
        }
        
        for (int i = 0; i < pictureList.size(); i++) {
            HSSFPictureData pictureData = pictureList.get(i);
           
            ClientAnchorInfo anchor = clientAnchorRecords.get(i);
            HSSFSheet she = anchor.sheet;
            //获得图片sheet号,以便图片排序
            int curSheetIndex = workbook.getSheetIndex(she);
            EscherClientAnchorRecord clientAnchorRecord = anchor.clientAnchorRecord;
            list.add(new ExcelPicture((HSSFWorkbook)workbook, she, pictureData, clientAnchorRecord,curSheetIndex));
        }
        if(list.size()!=0){
        	table.setPicList(list);
        }
	}

	private static List<ClientAnchorInfo> getClientAnchorRecords(HSSFWorkbook workbook) {
        List<ClientAnchorInfo> list = new ArrayList<ClientAnchorInfo>();
        EscherAggregate drawingAggregate = null;
        HSSFSheet sheet = null;
        List<EscherRecord> recordList = null;
        Iterator<EscherRecord> recordIter = null;
        int numSheets = workbook.getNumberOfSheets();
        for(int i = 0; i < numSheets; i++) {
            sheet = workbook.getSheetAt(i);
            drawingAggregate = sheet.getDrawingEscherAggregate();
            if(drawingAggregate != null) {
                recordList = drawingAggregate.getEscherRecords();
                recordIter = recordList.iterator();
                while(recordIter.hasNext()) {
                    getClientAnchorRecords(sheet, recordIter.next(), 1, list);
                }
            }
        }
        return list;
    }
	
    private static void getClientAnchorRecords(HSSFSheet sheet, EscherRecord escherRecord, int level, List<ClientAnchorInfo> list) {
        List<EscherRecord> recordList = null;
        Iterator<EscherRecord> recordIter = null;
        EscherRecord childRecord = null;
        recordList = escherRecord.getChildRecords();
        recordIter = recordList.iterator();
        while(recordIter.hasNext()) {
            childRecord = recordIter.next();
            if(childRecord instanceof EscherClientAnchorRecord) {
                ClientAnchorInfo e = new ClientAnchorInfo(sheet, (EscherClientAnchorRecord) childRecord);
                list.add(e);
            }
            if(childRecord.getChildRecords().size() > 0) {
                getClientAnchorRecords(sheet, childRecord, level+1, list);
            }
        }
    }
}
