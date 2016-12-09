package com.codecraft.excel2html.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.codecraft.excel2html.config.ConvertConfig;
import com.codecraft.excel2html.entity.ExcelHeader;
import com.codecraft.excel2html.entity.ExcelPicture;
import com.codecraft.excel2html.entity.ExcelTable;
import com.codecraft.excel2html.entity.ExcelTableTd;
import com.codecraft.excel2html.entity.ExcelTableTdStyle;
import com.codecraft.excel2html.entity.ExcelTableTr;
import com.codecraft.excel2html.entity.PictureStyle;
import com.codecraft.excel2html.entity.WrapperStringBuffer;
import com.codecraft.excel2html.parse.IExcelContentParse;
import com.codecraft.excel2html.picUpload.IExcelPicUpload;
import com.codecraft.excel2html.utils.StringsUtils;

/**
 * html生成器,注意每个页面都会在第一行创建一个空白行.(此空白行的目的是固定宽度)
 * @author zoro
 *
 */
public class HtmlGenerator {
	private ConvertConfig config;
	
	/**
	 * 返回html
	 * @param excelEntity
	 * @return
	 * @throws Exception
	 */
	public String toHtml(ExcelTable table) throws Exception{
		this.config = table.getConfig();
		StringBuffer sb = new StringBuffer();
		sb.append(htmlStart());//html start
		sb.append(headHtml());//head html
		sb.append(bodyHtml(table));//body html
		sb.append(htmlEnd());//html end
		return sb.toString();
	}
	
	/**
	 * html start
	 * @return
	 */
	private String htmlStart() {
		StringBuffer sb = new StringBuffer("");
		sb.append("<!DOCTYPE HTML>");
		sb.append("<html>");
		return sb.toString();
	}

	/**
	 * header html
	 * @return
	 * @throws Exception
	 */
	private StringBuffer headHtml() throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE8\"/>");
		sb.append("<meta name=\"keywords\" content=\"关键字\" /> ");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
		sb.append("<meta http-equiv=\"pragma\" CONTENT=\"no-cache\">");
		sb.append("<meta http-equiv=\"Cache-Control\" CONTENT=\"no-cache, must-revalidate\">");
		sb.append("<meta http-equiv=\"expires\" CONTENT=\"0\">");
		
		String jsVersion = config.getHtmlJsVsersion();
		String prefixPath = config.getPrefixPath();
		sb.append(String.format("<link rel=\"stylesheet\" type=\"text/css\" href=\"%s/preformCss.css?version=%s\">", prefixPath, jsVersion));
		sb.append(String.format("<link rel=\"stylesheet\" type=\"text/css\" href=\"%s/js/My97DatePicker/skin/WdatePicker.css?version=%s\">", prefixPath, jsVersion));
		sb.append("<title>www.excel2html.com</title>");

		//scriptHtml(sb);
		//scriptBodyHtml(sb);
		sb.append("</head>");
		return sb;
	}
	
	private void scriptHtml(StringBuffer sb) {
		String domain = config.getHtmlDomain();
		String jsVersion = config.getHtmlJsVsersion();
		String prefixPath = config.getPrefixPath();
		if(!StringsUtils.isEmpty(domain)){
			sb.append(String.format("<SCRIPT type='text/javascript'>document.domain='%s';</SCRIPT>",domain));
		}
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/jquery/jquery.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/easyui/jquery.easyui.min.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/easyui/locale/easyui-lang-zh_CN.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/frame/js/zh_CN/frame.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/jquery/json2.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/layer/layer.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/JSExpressionEval/Evaluator.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/JSExpressionEval/Tokanizer.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/JSExpressionEval/Stack.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/JSExpressionEval/Date.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/My97DatePicker/WdatePicker.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));		
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/preform/excel2MdfHtml.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/underscore/underscore.js?version=%s\"></SCRIPT>", prefixPath, jsVersion));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/highcharts/js/highcharts.js\"></SCRIPT>", prefixPath));	
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/highcharts/js/highcharts-3d.js\"></SCRIPT>", prefixPath));	
	}

	/**
	 * body html
	 * @param table
	 * @return
	 */
	private String bodyHtml(ExcelTable table) throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("<body>");
		sb.append("<form id='preformForm'>");
		//style='border-collapse:collapse;'  使用这个边框合并样式，会导致某些表单的边框变长很多,以后处理。
		sb.append("<table id=\"preformTable\" border=\"0\" cellspacing=\"0\" align='center' cellpadding=\"0\" style='border-collapse:collapse;margin-top:70px'>");
		sb.append(hiddenHtml(table));//隐藏域信息
		theadHtml(table,sb);//table页眉
		tfootHtml(table,sb);//table页脚
		tbodyHtml(table, sb);//table正文
		sb.append("</table>");
		sb.append("</form>");
		sb.append("</body>");
		//多页模板时,第二页不会重新解析
		if(table.getPicList().size() ==0){
			List<PictureStyle> parsePic = parsePic(table.getPicList());
			table.setPicStyleList(parsePic);
		}
		return sb.toString();
	}
	
	/**
	 * script htlml
	 * @param sb
	 * @return
	 * @throws Exception
	 */
	private String scriptBodyHtml(StringBuffer sb) throws Exception {
		WrapperStringBuffer wsb = new WrapperStringBuffer(sb);
		wsb.append(" <SCRIPT type='text/javascript'> ");
		wsb.append("     var param = {}; ");
		wsb.append("     var map = {}; ");
		wsb.append("		var editorList = {}; ");
		wsb.append("     var iframeId = ''; ");
		
		wsb.append(" $(function(){ ");
		wsb.append("	layer.config({extend: 'extend/layer.ext.js'}); ");	//加载layer.js
		wsb.append("	resizeParantFrame();	");							//设置父页面大小
		//sb.append(" 	resizeToolBar();	");								//设置工具栏
		wsb.append(" 	parseRequestParam(); ");							//解析请求的参数对象
		wsb.append(" 	parseTagId(); ");									//解析控件id与Excel的标示对应关系
		//sb.append(" 	loadToolbar(); ");									//工具栏js
		wsb.append(" 	loadCopyData(); ");									//加载复制数据
		wsb.append(" 	initToolbarStat(); ");								//初始化工具栏的状态
		wsb.append(" 	loadSpeChars(); ");									//加载特殊字符
		wsb.append("     iframeId = getFormContentId(); ");
		
		//编辑器加载完毕后，重新设置父页面大小
		wsb.append("		if('undefined' != typeof CKEDITOR ){ ");
		wsb.append("			CKEDITOR.on('instanceReady', function (e) { ");
		wsb.append("				resizeParantFrame();	");
		wsb.append("			});");
		wsb.append("		}	");

		wsb.append("		var _id = param._id; ");
		wsb.append("     if(_id && _id!='null'){ ");
		String prefixPath = config.getPrefixPath();
		wsb.append(String.format(" 		var url='%s/preform_getPreformData?_id=' + _id; ", prefixPath));
		wsb.append("			var json=''; ");
		wsb.append(" 		var index = layer.load(2); ");//遮罩开始
		wsb.append(" 		$.ajax({ ");
		wsb.append(" 			type: 'POST', ");
		wsb.append(" 			contentType: 'application/json;charset=utf-8', ");
		wsb.append(" 			url:  url, ");
		wsb.append(" 			data: json, ");
		wsb.append(" 			async: false, ");//这里必须为false，解决编辑器加载数据有几率失败的问题
		wsb.append(" 			dataType : 'json', ");
		wsb.append(" 			success : function(data) { ");
		wsb.append(" 				layer.close(index); ");//遮罩关闭
		wsb.append("         		if(data && data.body){ ");
		wsb.append("						setDataBody(data.body); ");//设置数据 
		wsb.append("					} "); 
		wsb.append("     			var height = calcPageHeight(); ");//在数据加载完后，重新设置iframe高度
		wsb.append("     			if(parent.document != null && parent.document.getElementById(iframeId)!=null){");
		wsb.append("     				parent.document.getElementById(iframeId).style.height = height + 100 + 'px'; ");
		wsb.append("     				parent.document.getElementById(iframeId).style.width = '1198px';");
		wsb.append(" 				}	");
		wsb.append(" 			}, ");
		wsb.append(" 			error : function(){  ");
		wsb.append(" 				layer.close(index);");//遮罩关闭
		wsb.append(" 			}");
		wsb.append(" 		});");
		wsb.append(" 	}");
		wsb.append(" });");
		
		wsb.append(" function setDataBody(body){ ");
		wsb.append("		loadStyleByJson(body); ");//加载样式
		wsb.append("		loadDataByJson(body); ");//加载数据
		wsb.append(" };");

		//加载样式(fontSize size align)
		wsb.append("		function loadStyleByJson(data){ ");
		wsb.append(" 	  	var styData = data.intSty;");
		wsb.append(" 	  	var tempMap = preformConfig.styAttr;");
		wsb.append(" 		if(styData != null){");
		wsb.append(" 	  		for(var i in styData){");
		wsb.append(" 	 			if($('#'+i).size()==1 ){");
		wsb.append(" 	 				if(styData[i].fontSize!=null){ ");
		wsb.append(" 	 					$('#'+i).parent().css('font-size',styData[i].fontSize);");
		wsb.append(" 	 					$('#'+i).css('font-size',styData[i].fontSize);");//view时因为是span所以不需要
		wsb.append(" 	  				}");
		wsb.append(" 	 				if(styData[i].align!=null){ ");
		wsb.append(" 	 					$('#'+i)[0].style.textAlign=styData[i].align;");
		wsb.append(" 	  				}");
		wsb.append(" 	 				if(styData[i].size!=null){ ");
		wsb.append("							$('#'+i).attr('size',styData[i].size);");
		wsb.append(" 	  				}");
		wsb.append(" 	 				if(styData[i].valign!=null){ ");
		wsb.append(" 	 					$('#'+i).css('vertical-align',styData[i].valign);");
		wsb.append(" 	 					$('#'+i).parent().parent().attr('valign',styData[i].valign);");
		wsb.append(" 	  				}");
		wsb.append(" 	  				for(var attrName in styData[i]){	");
		wsb.append(" 						if(tempMap[attrName]==1){	");
		wsb.append(" 							$('#'+i).css(attrName,styData[i][attrName]);");
		wsb.append(" 						}");
		wsb.append(" 					}");
		wsb.append(" 	  			}");
		wsb.append(" 	 		}");
		wsb.append(" 		}");
		wsb.append(" 	}");
		 
		//加载数据
		wsb.append("		function loadDataByJson(data){ ");
		wsb.append("			for(var name in data){ ");
		wsb.append("				var jqObj = $('#' + name); ");
		wsb.append("		 		if(jqObj.size()==1){ "); 
		wsb.append("		 			var len = jqObj.attr('size'); ");
		wsb.append("					var iseditor = jqObj.attr('iseditor'); ");
		wsb.append("					var tagName = jqObj[0].tagName; ");
		wsb.append("					var inpType = jqObj.attr('type'); ");
		wsb.append("					if(tagName.toUpperCase() == 'SPAN' || (tagName.toUpperCase() == 'DIV' && iseditor == undefined)){ ");// 处理span
		wsb.append("						$('#'+ name).html(data[name]); ");
		wsb.append("					}else if(iseditor == 'true'){ ");//ckeditor
		wsb.append("    					 for(var e in editorList){ ");
		wsb.append("     					if(editorList[e]['name'] == name){ ");
		wsb.append("     						editorList[e].setData(data[name]); ");
		wsb.append("     					} ");
		wsb.append("    					 } ");
		wsb.append("					}else if(tagName.toUpperCase() == 'INPUT' && inpType.toUpperCase() == 'CHECKBOX'){ ");//处理checkbox
		wsb.append("						jqObj.attr('checked','checked'); ");
		wsb.append("					}else{ ");
		wsb.append("		 				jqObj.val(data[i]); ");//是input 或  textarea
		wsb.append("					} ");
		wsb.append("		 		 } ");
		wsb.append("		 	} ");
		wsb.append("		} ");
		
		//获得天气
		wsb.append("		function getWeaData(){ 	");
		wsb.append("			var ip = param.ip; ");
		wsb.append(String.format(" 		var url='%s/preform_getWeaDataByIp?ip=' + ip; ", prefixPath));
		wsb.append(" 		var result = ''; ");
		wsb.append(" 		$.ajax({ ");
		wsb.append(" 			type: 'POST', ");
		wsb.append(" 			contentType: 'application/json;charset=utf-8', ");
		wsb.append(" 			url:  url, ");
		wsb.append(" 			data: '', ");
		wsb.append(" 			async: false, ");
		wsb.append(" 			dataType : 'json', ");
		wsb.append(" 			success : function(data) { ");
		wsb.append("         		if(data != null){ ");
		wsb.append("						result = data.body.result; ");
		wsb.append("					} "); 
		wsb.append(" 			}, ");
		wsb.append(" 			error : function(){  ");
		wsb.append(" 			}	 ");
		wsb.append(" 		}); ");
		wsb.append("		 	return result;	");
		wsb.append("		 }	");
		
		//预览时需要调用此方法
		wsb.append("		function getAllData(){ 	 ");
		wsb.append("			var obj = {}; 	 ");
		wsb.append("			var pform = $('#preformForm').serializeArray();");
		wsb.append("			$.each(pform, function(){  	 ");
		wsb.append("				if(this.value == '' || this.name == 'paramValue') return true;	 ");
		wsb.append("				if(obj[this.name]){  	 ");
		wsb.append("					if (obj[this.name].push) { ");
		wsb.append(" 			 		obj[this.name].push(this.value); ");
		wsb.append(" 				} ");
		wsb.append(" 			}else {   ");
		wsb.append(" 				obj[this.name] = this.value; ");
		wsb.append(" 	  		}");
		wsb.append(" 		 });  ");
		wsb.append("			$.each($('span[id][name]'), function(){  	 ");
		wsb.append("		 		if($(this).text()=='' ) return true; ");
		wsb.append("		 		obj[$(this).attr('id')] = $(this).text(); ");
		wsb.append("			});  ");
		wsb.append(" 		obj.id=param.id; ");
		wsb.append(" 		obj._id=param._id; ");
		
		//删除无用属性
		wsb.append("      	removeNullAttr(obj);	");
		wsb.append(" 		json = JSON.stringify(obj); ");
		wsb.append(" 		json = encodeURIComponent(encodeURIComponent(json)); ");
		wsb.append("			return json; 	 ");
		wsb.append("		} 	 ");
		
		//获得数据(保存时调用此方法)
		wsb.append("		function getData(){ 	 ");
		wsb.append("			var obj = {}; 	 ");
		wsb.append("			var a = $('#preformForm').serializeArray();");
		wsb.append("			$.each(a, function(){  	 ");
		wsb.append("				if(this.value == '') return true;	 ");
		wsb.append("				if(obj[this.name]){  	 ");
		wsb.append("					if (obj[this.name].push){ ");
		wsb.append(" 			 		obj[this.name].push(this.value); ");
		wsb.append(" 				} ");
		wsb.append(" 			}else {   ");
		wsb.append(" 				obj[this.name] = this.value; ");
		wsb.append(" 	  		}");
		wsb.append(" 		});  ");
		wsb.append(" 		obj.id=param.id; ");
		wsb.append(" 		obj._id=param._id; ");
		wsb.append("      	removeNullAttr(obj);	");//删除无用属性
		wsb.append(" 		json = JSON.stringify(obj); ");
		wsb.append(" 		json = encodeURIComponent(encodeURIComponent(json)); ");
		wsb.append("			return json; 	 ");
		wsb.append("		} 	 ");
		
		//保存数据
		wsb.append(" function saveData(){ ");   
		wsb.append("     var result = {}; ");
		wsb.append("     var json = getData(); ");
		wsb.append(String.format(" 	var  url='%s/preform_savePreformData'; ", prefixPath));
		wsb.append(" 	$.ajax({ ");
		wsb.append(" 		type: 'POST', ");
		wsb.append(" 		contentType: 'application/json;charset=utf-8', ");
		wsb.append(" 		url:  url, ");
		wsb.append(" 		data: json, ");
		wsb.append(" 		async: false, ");
		wsb.append(" 		dataType : 'json', ");
		wsb.append(" 		success : function(data) { ");
		wsb.append("         	if(data != null){ ");
		wsb.append("         		result=data; ");
		wsb.append("				} "); 
		wsb.append(" 		}, ");
		wsb.append(" 		error : function(){  ");
		wsb.append(" 		  layer.alert('保存失败!'); ");
		wsb.append(" 		}	 ");
		wsb.append(" 	}); ");
		wsb.append(" 	return result; ");
		wsb.append(" } ");
		
		//获得被选中的单选框
		wsb.append(" function getChecked(rndSrcMap){ ");
		wsb.append("		for(var tagId in rndSrcMap){	");
		wsb.append("			if($('#'+tagId).is(':checked')){");
		wsb.append("				return tagId;");
		wsb.append("			}");
		wsb.append("		}");
		wsb.append(" } ");
		
		wsb.append(" 	function loadCopyData(){	");
		wsb.append("  		var pattern = /A[0-9]+/;	");
		wsb.append(" 		for(var name in param){	");
		wsb.append(" 			if(pattern.test(name)){	");
		wsb.append(" 				if($('#'+name).is('input')){	");
		wsb.append(" 					$('#'+name).val(param[name]);");
		wsb.append(" 				}else if($('#'+name).is('span'))");
		wsb.append(" 					$('#'+name).html(param[name]);");
		wsb.append(" 				}	");
		wsb.append(" 		}	");
		wsb.append(" 	}	");
		
		wsb.append(" 	function resizeToolBar(obj){	");
		wsb.append(" 		$('#toolbarDiv1').show();	");
		wsb.append(" 	}	");
		
		//获得指定对象与左屏幕距离
		wsb.append(" 	function getAbsLeft(obj){	");
		wsb.append(" 		var l=obj.offsetLeft;");
		wsb.append(" 		while(obj.offsetParent != null){");
		wsb.append(" 			obj = obj.offsetParent;");
		wsb.append(" 			l += obj.offsetLeft;");
		wsb.append(" 		}");
		wsb.append(" 		return l; ");
		wsb.append(" 	}	");
		
		wsb.append(" 	function radioOnlyEvent(obj){	");
		wsb.append(" 		var alias = $(obj).attr('alias');	");
		wsb.append(" 		$('input[type=checkbox][alias=' + alias +']').each(function(){	");
		wsb.append(" 			if(this!=obj){");
		wsb.append(" 				$(this).prop('checked',false);	");
		wsb.append(" 				cancelRndChkEvent(this);	");
		wsb.append(" 			}	");
		wsb.append(" 		});");
		wsb.append(" 		cancelRndChkEvent(obj);	");
		wsb.append(" 	}	");
		wsb.append(" </SCRIPT> ");
		return wsb.toString();
	}

	/**
	 * 隐藏域html
	 * @param table
	 * @return
	 */
	private String hiddenHtml(ExcelTable table) {
		StringBuffer sb = new StringBuffer("");
		sb.append("<input id='id' name='id' type='hidden'>");
		sb.append("<input id='_id' name='_id' type='hidden'>");
		sb.append("<input id='paramValue' name='paramValue' type='hidden'>");//前台传递的参数信息
		sb.append(String.format("<input id='excelIdMap' name='excelIdMap' type='hidden' value='%s'>", table.getExcelIdMap().toString()));//excelId与tagId的对应关系(计算时会使用)
		sb.append(String.format("<input id='tagIdMap'   name='tagIdMap'   type='hidden' value='%s'>", table.getTagIdMap().toString()));//tagId与excelId的对应关系
		return sb.toString();
	}
	
	/**
	 * 页眉 html
	 */
	private void theadHtml(ExcelTable table, StringBuffer sb) {
		ExcelHeader header =  table.getHeader();
		if(header != null){
			String align = "";
			String content = "";
			if(!"".equals(header.getLeftContent())){
				align = "left";
				content = header.getLeftContent();
			}else if(!"".equals(header.getCenterContent())){
				align = "center";
				content = header.getCenterContent();
			}else if(!"".equals(header.getRightContent())){
				align = "right";
				content = header.getRightContent();
			}
			if(!"".equals(content)){
				int cellNum = table.getCellSize();
				sb.append("<thead>");
				sb.append("<tr>");
				sb.append(String.format("<td align='%s' colspan='%s' style='font-family:宋体;font-size: 10pt;'>%s", align, cellNum, content));
				sb.append("</td>");
				sb.append("</tr>");
				sb.append("</thead>");
			}
		}
	}
	
	/**
	 * 页脚转化为theader
	 */
	private void tfootHtml(ExcelTable table, StringBuffer sb) {
		ExcelHeader footer =  table.getFooter();
		if(footer != null){
			String align = "";
			String content = "";
			if(!"".equals(footer.getLeftContent())){
				align = "left";
				content = footer.getLeftContent();
			}else if(!"".equals(footer.getCenterContent())){
				align = "center";
				content = footer.getCenterContent();
			}else if(!"".equals(footer.getRightContent())){
				align = "right";
				content = footer.getRightContent();
			}
			if(!"".equals(content)){
				int cellNum = table.getCellSize();
				sb.append("<tfoot>");
				sb.append("<tr>");
				sb.append(String.format("<td align='%s' colspan='%s' style='font-family:宋体;font-size: 10pt;'>%s", align, cellNum, content));
				
				sb.append("</td>");
				sb.append("</tr>");
				sb.append("</tfoot>");
			}
		}
	}
	
	/**
	 * 表格内容 转化为tbody
	 */
	private void tbodyHtml(ExcelTable table, StringBuffer sb)
			throws Exception {
		sb.append("<tbody>");
		
		//设置宽度td
		String blankTrHtml = createBlankRow(table);
		sb.append(blankTrHtml);
		for (int rowNum = table.getFirstRowNum(); rowNum <= table.getLastRowNum(); rowNum++) {
			ExcelTableTr tr = table.getTrMap().get(rowNum);
			if (tr != null) {
				tr.setRowNum(rowNum);//设置rownum 计算合并单元格高度时用到
				

				String trHtml = tr2Html(table, tr);
				sb.append(trHtml);
			}
		}
		sb.append("</tbody>");
	}

	/**
	 * html end
	 * @return
	 */
	private String htmlEnd() {
		return "</html>";
	}
	
	/**
	 * tr to html
	 * @param table
	 * @param tr
	 * @return
	 * @throws Exception
	 */
	private String tr2Html(ExcelTable table, ExcelTableTr tr)
			throws Exception {
		Set<String> filter = table.getRowColumnSpan().getRowColunmSpanFilter();
		StringBuffer sb = new StringBuffer();
		sb.append("<tr ");
		sb.append(String.format("height='%s'>", tr.getHeight()));
		int lastColNum = tr.getLastColNum();
		int rowNum = tr.getRowNum();
		for (int colNum = 0; colNum < lastColNum; colNum++) {
			ExcelTableTd td = tr.getTdMap().get(colNum);
			if(td != null && !filter.contains(rowNum + "," + colNum)){
				String tdHtml = td2Html(table, tr, td);
				sb.append(tdHtml);
			}
		}
		sb.append("</tr>");
		return sb.toString();
	}
	
	/**
	 * 创建一个空白行(此行的目的是固定宽度)***
	 * @param table
	 * @param tr
	 * @return
	 */
	private String createBlankRow(ExcelTable table) {
		boolean testWidth = table.getConfig().getTestWidth();
		int rowNum = table.getWidthRowNum();
		ExcelTableTr tr =  table.getTrMap().get(rowNum);
		if(tr == null){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<tr ");
		sb.append(String.format("height='%s'>", tr.getHeight()));
		int lastColNum = tr.getLastColNum();

		for (int colNum = 0; colNum < lastColNum; colNum++) {
			ExcelTableTd td = tr.getTdMap().get(colNum);
			if(td != null){
				sb.append("<td ");
				sb.append(String.format("colspan='1' rowspan='1' "));
				if (!StringsUtils.isEmpty(td.getWidth()) ) {
						sb.append("style='");
						sb.append(String.format("width:%s;min-width:%s;max-width:%s;", td.getWidth(),td.getWidth(),td.getWidth()));
						sb.append("'");
						if(testWidth){
							System.out.println("rowNum:" + rowNum + ",width:" + td.getWidth());
						}
				}
				sb.append("> ");
				sb.append("</td>");
			}
		}
		sb.append("</tr>");
		return sb.toString();
	}
	
	/**
	 * td to html
	 * @param table
	 * @param tr
	 * @param td
	 * @return
	 * @throws Exception
	 */
	private String td2Html(ExcelTable table, ExcelTableTr tr,
			ExcelTableTd td) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<td ");
		if (!StringsUtils.isEmpty(td.getColspan())) {
			sb.append(String.format(" colspan= '%s'", td.getColspan()));
		}
		if (!StringsUtils.isEmpty(td.getRowspan())) {
			sb.append(String.format(" rowspan= '%s'", td.getRowspan()));
		}
		if (td.getColNum() != -1) {
			sb.append(String.format(" colNum= '%s'", td.getColNum()));
		}
		if (td.getTdStyle() != null) {
			ExcelTableTdStyle tdStyle = td.getTdStyle();
			if (tdStyle != null) {
				sb.append(tdStyle2Html(table, tr, td, tdStyle));
			}
		}
		
		//如果td包含编辑器，则不需要align
		if (!StringsUtils.isEmpty(td.getTdAlign()) && !td.isEditor()) {
			sb.append(td.getTdAlign());
		}
		sb.append(">");
		
		String content = "";
		if (!StringsUtils.isEmpty(td.getContent())) {
			IExcelContentParse contentParse = table.getConfig().getExcelContentParse();
			content = contentParse.parseConten(table, tr, td);
		}
		sb.append(content);
		sb.append("</td>");
		return sb.toString();
	}
	
	//获得td样式
	private String tdStyle2Html(ExcelTable table, ExcelTableTr tr,
			ExcelTableTd td, ExcelTableTdStyle tdStyle) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format(" style='"));
		sb.append(tdStyle.getFont());
		sb.append(tdStyle.getColor());
		sb.append(tdStyle.getBorderBottom());
		sb.append(tdStyle.getBorderLeft());
		sb.append(tdStyle.getBorderRight());
		sb.append(tdStyle.getBorderTop());
		sb.append(String.format("'"));
		return sb.toString();
	}
	
	/*
	 ** 解析图片样式
	 */
	private List<PictureStyle> parsePic(List<ExcelPicture> listPic) throws Exception {
		List<PictureStyle> pbList = new ArrayList<PictureStyle>();
		for(ExcelPicture pic : listPic ){
			int top = pic.getTop(pic.getRow1());
			int left = pic.getLeft(pic.getCol1());
			double width = pic.getWidth(pic.getCol1(), pic.getCol2());
			double height = pic.getHeight(pic.getRow1(), pic.getRow2());
			PictureStyle pb = new PictureStyle();
			pb.setTop(top);
			pb.setLeft(left);
			pb.setWidth(width);
			pb.setHeight(height);
			if(pic.getSheet() != null){
				pb.setSheetIndex(pic.getSheetIndex());
			}
			String url = uploadPic(pic);
			pic.setUrl(url);
			pb.setUrl(url);
			pbList.add(pb);
		}
		Collections.sort(pbList);
		return pbList;
	}
	
	//上传Excel图片
	private String uploadPic(ExcelPicture picBo) throws Exception {
		byte[] data = picBo.getData();
		IExcelPicUpload picLoad = config.getPicUpload();
		if(picLoad != null){
			return picLoad.loadPic(data);
		}
		return "";
	}
}
