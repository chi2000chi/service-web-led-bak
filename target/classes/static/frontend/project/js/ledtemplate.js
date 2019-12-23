var ADDTYPE = "ADD";
var UPDATETYPE = "UPDATE";
var openDialogIndex = "";
var add_fqqy_datagrid = $('#add_fqqy_datagrid');
var idAndDivMap = {"0":"textCss", "1":"imageCss", "2":"videoCss"};
var divAndTableMap = {"textCss": "文本内容", "imageCss": "上传广告", "videoCss": "上传视频"}
// 存下拉列表父编码
var zdbmList = ["5","6","7","8","9","10","11","12","13","14"];
// 存下拉列表编码对应key数据
var zdbmMap = {5:"font-size", 6:"font-style", 7:"font-weight", 8:"show-style", 9:"move-speed", 10:"sound-flag",
		11:"sound-volume", 12:"sound-speed", 13:"sound-count", 14:"replay-count"};
// 存第三步中表格数据
var qu_fileList;
// 此段用于第四层dialog中存储时，要根据不同类型存不同的key值。
var fourTextKey = ["font-size", "font-style", "font-weight", "font-color", "show-style", "move-speed",
		"show-time", "sound-flag", "sound-volume", "sound-speed", "sound-count", "replay-count", "cron"];
var fourImageKey = ["show-style","move-speed","show-time","replay-count","cron"];
var fourVideoKey = ["show-time","sound-flag","sound-volume","sound-speed","cron"];
var keyMap = {"textCssKey": fourTextKey, "imageCssKey": fourImageKey, "videoCssKey": fourVideoKey}
// 总保存时，需要根据key拆成各自的map数据, 此处为区域信息key
var qyxxKey = ["start-x", "end-x", "start-y", "end-y", "color", "background-color", "background-image"]
// 实例化编辑器
var ue;

$.extend($.fn.datagrid.methods, {
	/**
	 * 关闭所有编辑行
	 */
    endAllEditor: function (jq, param) {
        var row = $(jq).datagrid("getRows");
        for(var i=0; i< row.length; i++) {
	        if($(jq).datagrid('validateRow',i)) {
	        	var ed = $(jq).datagrid('getEditor', {index:i,field:'fqlx'});
	        	if(ed != undefined) {
	        		var fqlxmc = $(ed.target).combobox('getText');
	        		$(jq).datagrid('getRows')[i]['fqlxmc'] = fqlxmc;
	        	}
	        	$(jq).datagrid('endEdit',i);//当前行编辑事件取消
	        } else {
	        	return false;
	        }
        }
    }
})

$(function() {
	// 初始化加载所有下拉列表
	initCombobox();
	// 初始化表格
	initTable();
	// 查询页面数据
	searchFqmb();
	
	$("#add_qysz_save_btu").click(qyszSaveBtuFun);

	$("#add_save_btu").click(addSaveBtuFun);
	
	 // 查询事件
	$("#search_btn").click(searchFqmb);
	// 添加按钮点击事件
	$("#add_btu").click(function() {
		$("#add_dialog").dialog("open");
		$("#add_mbid").textbox("setValue","");
		$("#add_mblj").textbox("setValue","");
		clearAndInitFqmbpz(ADDTYPE, null);
	});
	// 修改按钮点击事件
	$("#update_btu").click(updateFun);
	// 删除按钮点击事件
	$("#del_btu").click(delFun);
	
	setHeight();
	$('.btn-searchDetail').click(function() {
		$(".panel-controller").eq(0).toggleClass("hide");
		$(this).toggleClass("on");
		if ($(this).hasClass("on")) {
			$(this).html("展开搜索<span></span>")
		} else {
			$(this).html("收起搜索<span></span>")
		}
		setHeight();
	});
	// 移除加载弹出框
	$('#loading').remove();
	// 绑定查询按钮
	$("#search_dzzpxx").click(searchLedList);
	//实例化编辑器
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	ue = UE.getEditor('editor', {
		toolbars : [ [ 'fullscreen', 'source', '|', 'undo', 'redo', '|',
				'bold', 'italic', 'underline', 'fontborder', 'strikethrough',
				'superscript', 'subscript', 'removeformat', 'formatmatch',
				'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor',
				'backcolor', 'insertorderedlist', 'insertunorderedlist',
				'selectall', 'cleardoc', '|', 'rowspacingtop',
				'rowspacingbottom', 'lineheight', '|', 'customstyle',
				'paragraph', 'fontfamily', 'fontsize', '|',
				'directionalityltr', 'directionalityrtl', 'indent', '|',
				'justifyleft', 'justifycenter', 'justifyright',
				'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
				'emotion', 'pagebreak', 'template', 'background', '|',
				'horizontal', 'date', 'time', 'spechars', 'snapscreen',
				'wordimage', '|', 'inserttable', 'deletetable',
				'insertparagraphbeforetable', 'insertrow', 'deleterow',
				'insertcol', 'deletecol', 'mergecells', 'mergeright',
				'mergedown', 'splittocells', 'splittorows', 'splittocols',
				'charts', '|', 'preview', 'searchreplace', 'help' ] ]
	});
})


/**--------------------------------------第1个dialog方法开始-----------------------------------------------*/

/**
 * 总保存事件，发请求，拼数据
 */
function addSaveBtuFun() {
	add_fqqy_datagrid.datagrid("endAllEditor");
	// 表格数据，根据addType,需拆分为新建和更新数据
	var rows =add_fqqy_datagrid.datagrid("getRows");
	var mbmc = $("#add_mbmc").textbox("getValue");
	var mbbh = $("#add_mbbh").textbox("getValue");
	var mbid = $("#add_mbid").textbox("getValue");
	var mblj = $("#add_mblj").textbox("getValue");
	// 校验mbmc和mbbh是否填写。
	if (mbmc == null || mbmc == "" || mbbh == null || mbbh == "") {
		$.messager.alert("提示", "分区模板名称和分区模板编号都不能为空!");
		return false;
	}
	// 验证数据填写是否符合规则
	if(checkSendTableData(rows)) {
		var resMap = {};
		resMap.mbmc = mbmc;
		resMap.mbbh = mbbh;
		resMap.mbid = mbid;
		resMap['template-id'] = mbid;
		resMap.mblj = mblj;
		resMap.mblj = mblj;
		// 拼数据
		$.each(rows, function(i, obj) {
			obj.mbmc = mbmc;
			obj.mbbh = mbbh;
			splitKey(obj);
		})
		resMap['area-count'] = rows.length;
		resMap.dataList = JSON.stringify(rows);
		// 向后台发送数据
		$.post("/saveMbinfo", resMap, function(data){
			
			if (data.status == "1") {
				// 关闭dialog
				$("#add_dialog").dialog("close");
				// 刷新页面
				searchFqmb();
			} else {
				$.messager.alert("提示", data.message); 
				return false;
			}
		})
	}
}

/**
 * 验证数据填写是否符合规则
 */
function checkSendTableData(data) {
	var status = true;
//	$.each(data, function(i, obj) {
//		if(obj['start-x'] == undefined || obj['start-x'] == null || obj['start-x'] == "") {
//			status = false;
//			return false;
//		}
//	})
	if (data == undefined || data == null || data.length == 0) {
		$.messager.alert("提示", "至少设置一种分区类型!");
		status = false;
		return false;
	}
	$.each(data, function(i, obj){
		if (obj.fileList == undefined || obj.fileList.length == 0) {
			status = false;
			return false;
		}
	})
	if(!status) {
		$.messager.alert("提示", "每个分区类型数据不能为空,请重新填写!");
	}
	return status;
}

/**
 * 拆分key
 */
function splitKey(data) {
	var type = idAndDivMap[data.fqlx];
	var keyList = keyMap[type+"Key"];
	var ledAttr = {};
	// 第一个区域key
	$.each(qyxxKey, function(index, keyStr) {
		ledAttr[keyStr] = data[keyStr];
	})
	data.ledAttr = ledAttr;
	var ledCommonset = {};
	// 第二个区域key
	$.each(keyList, function(index, keyStr) {
		ledCommonset[keyStr] = data[keyStr];
	})
	data.ledCommonset = ledCommonset;
	$.each(data.fileList, function(index, obj) {
		if(!obj.sjc) {
			obj.sjc = obj.fileid;
		}
	})
	data.ledFile = data.fileList;
}

/**
 * 分区模板配置表格操作栏加载方法
 */
function formatterOperation(value,row,index) {
	var add = '<a href="javascript:;" class="easyui-linkbutton zd-btn" style="width:65px;height:24px;" onclick="addRowFun(' + index + ')">设置</a>';
	var show = '<a href="javascript:;" class="easyui-linkbutton zd-btn" style="width:65px;height:24px;" onclick="showRowFun(' + index + ')">查看</a>'
	var del = '<a href="javascript:;" class="easyui-linkbutton zd-btn" style="width:65px;height:24px;" onclick="delRowFun(' + index + ')">删除</a>'
	return add + show + del;
}

/**
 * dialog中表格双击开启编辑事件
 */
function dblClickFun(rowIndex, rowData) {
	if(rowData.fqid == undefined) {
		var ed = add_fqqy_datagrid.datagrid('getEditor', {index:rowIndex,field:'fqlx'});
		// 开启编辑
		if(ed == undefined) {
			// 开启编辑
			add_fqqy_datagrid.datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
		} else {
			// 结束编辑
			var fqlxmc = $(ed.target).combobox('getText');
			add_fqqy_datagrid.datagrid('getRows')[rowIndex]['fqlxmc'] = fqlxmc;
			add_fqqy_datagrid.datagrid('endEdit', rowIndex);
			validateFqlx();
		}
	}
}

/**
 * 校验选择的是否重复，获取选择则模版类型，统计文本、视频和广告的个数，大于2，那么就return，并提示消息
 * @returns
 */
function validateFqlx(){
	// 取消所有行内编辑
	add_fqqy_datagrid.datagrid("endAllEditor");
	// 获取当前行数据，开启弹窗
	var rows = add_fqqy_datagrid.datagrid('getRows');//获得所有行
	var textnum = 0;
	var guanggaonum = 0;
	var shipinnum = 0;
	$.each(rows,function(i, val) {
		if (val.fqlx == 0) {
			textnum = textnum + 1;
		}
		if (val.fqlx == 1) {
			guanggaonum = guanggaonum + 1;
		}
		if (val.fqlx == 2) {
			shipinnum = shipinnum + 1;
		}
	});
	if (textnum > 1) {
		$.messager.alert("提示", "只允许设置一条文本类型!");
		return false;
	} else if (guanggaonum > 1) {
		$.messager.alert("提示", "只允许设置一条广告类型!");
		return false;
	} else if (shipinnum > 1) {
		$.messager.alert("提示", "只允许设置一条视频类型!");
		return false;
	} else {
		return true;
	}
}

/**
 * dialog中新增按钮，表格新增一行
 */
$("#add_fqqysz_btu").click(function() {
	var rowData = {};
	rowData.addType = ADDTYPE;
	add_fqqy_datagrid.datagrid('appendRow',rowData);
	editIndex = add_fqqy_datagrid.datagrid('getRows').length-1;
	add_fqqy_datagrid.datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
})

/**
 * 行内设置按钮
 */
function addRowFun(index) {
	var row = getRowdataForIndex(index);
	var valStatus = validateFqlx();
	if (valStatus) {
		// 获取div前缀
		var divStr = idAndDivMap[row.fqlx];
		// 禁用的文本框开启
		getInput(divStr, false);
		// 取消所有编辑行，根据区域类型开启不同窗口,并返回当前行数据
		getRowAndOpenDialog(row, index, divStr, true);
		$("#add_qysz_save_btu").show();
	}
	setEnabled();
}

/**
 * 行内查看按钮
 */
function showRowFun(index) {
	// 取消所有编辑行，根据区域类型开启不同窗口,并返回当前行数据
	var row = getRowdataForIndex(index);
	// 获取div前缀
	var divStr = idAndDivMap[row.fqlx];
	// 先填充数据，在禁用
	getRowAndOpenDialog(row, index, divStr, false);
	// 禁用文本框
	getInput(divStr, true);
    // 保存按钮隐藏
    $("#add_qysz_save_btu").hide();
    // 禁用编辑器
    setDisabled();
}

/**
 * 
 * @param divStr id前缀
 * @param isShow true 禁用  false 可编辑
 * @returns
 */
function getInput(divStr, isShow) {
	var inputList = $("#"+divStr+"_form input");
	var iList = [];
	$.each(inputList, function(i, obj) {
		var id = $(obj).attr("id");
		if(id != undefined) {
			if(id.indexOf(divStr) != -1 ) {
				iList.push(obj);
			}
		}
	})
	$.each(iList, function(i, obj) {
		var domId = "#" + $(obj).attr("id");
		var className= $(obj).attr('class');
		if(isShow) {
			if(className != undefined) {
				if(className.indexOf('easyui-numberbox') != -1){
					$(domId).numberbox("disable");
				} else if(className.indexOf('easyui-combobox') != -1){
					$(domId).combobox("disable");
				} else if(className.indexOf('easyui-textbox') != -1){
					$(domId).textbox("disable");
				} else {
					$(domId)[0].disabled=true;
				}
			}
		} else {
			if(className != undefined) {
				if(className.indexOf('easyui-numberbox') != -1){
					$(domId).numberbox("enable");
				} else if(className.indexOf('easyui-combobox') != -1){
					$(domId).combobox("enable");
				} else if(className.indexOf('easyui-textbox') != -1){
					$(domId).textbox("enable");
				} else {
					$(domId)[0].disabled=false;
				}
			}
		}
	})
}

/**
 * 行内删除按钮
 */
function delRowFun(index) {
	// 取消所有行内编辑
	add_fqqy_datagrid.datagrid("endAllEditor");
	// 删除之前存一遍fqid
	// 获取当前行数据，开启弹窗
	var rows = add_fqqy_datagrid.datagrid('getRows');//获得所有行
	add_fqqy_datagrid.datagrid('deleteRow',index);
    var rows = add_fqqy_datagrid.datagrid("getRows");    //重新获取数据生成行号
    add_fqqy_datagrid.datagrid("loadData", rows);
}

/**
 * 获取当前行数据
 */
function getRowdataForIndex(index) {
	// 取消所有行内编辑
	add_fqqy_datagrid.datagrid("endAllEditor");
	// 获取当前行数据，开启弹窗
	var rows = add_fqqy_datagrid.datagrid('getRows');//获得所有行
    var row = rows[index];
    return row;
}

/**
 * 取消所有编辑行，根据区域类型开启不同窗口
 * @param row 当前行数据 
 * @param index 行下标
 * @param divStr 用于判断显示哪个div
 * @param isShow 判断是否可以做第三步的添加操作
 * @returns
 */
function getRowAndOpenDialog(row, index, divStr, isShow) {
	openDialogIndex = index;
	$("#add_qysz_dialog").attr("type", divStr);
    if(0 == row.fqlx) {
    	$("#textCss_div").show();
    	$("#imageCss_div").hide();
    	$("#videoCss_div").hide();
    } else if(1 == row.fqlx) {
    	$("#imageCss_div").show();
    	$("#textCss_div").hide();
    	$("#videoCss_div").hide();
    } else if(2 == row.fqlx) {
    	$("#videoCss_div").show();
    	$("#imageCss_div").hide();
    	$("#textCss_div").hide();
    }
	// 清空表单
	$("#"+divStr + "_form").form("reset");
	// 数据填充
	fillForm(divStr, row);
	if(row.color) {
		$("#" + divStr + "_color").val(row.color);
	} else {
		$("#" + divStr + "_color").attr("#FFFFFF");
	}
	if(row['background-color']) {
		$("#" + divStr + "_background-color").val(row['background-color']);
	} else {
		$("#" + divStr + "_background-color").attr("#FFFFFF");
	}
	if(row['font-color']) {
		$("#" + divStr + "_font-color").val(row['font-color']);
	} else {
		$("#" + divStr + "_font-color").attr("#FFFFFF");
	}
	// 画最底下表格
	if(row.fileList) {
		qu_fileList = copyData(row.fileList);
		createSzTable(row, qu_fileList, divStr, isShow);
	} else {
		qu_fileList = [];
		createSzTable(row, qu_fileList, divStr, isShow);
	}
	// 获取当前行数据，开启弹窗
	$("#add_qysz_dialog").dialog("open");
}

/**--------------------------------------第1个dialog方法结束-----------------------------------------------*/

/**--------------------------------------第2个dialog方法开始-----------------------------------------------*/

/**
 * 获得编辑器的纯文本内容
 */
function hasContentTxt() {
	return UE.getEditor('editor').hasContents();
}

/**
 * 获得编辑器的内容
 */
function getContent() {
	return UE.getEditor('editor').getContent();
}

/**
 * 字符串去空
 * @param str
 * @returns
 */
function Trim(str){ 
    return str.replace(/(^\s*)|(\s*$)/g, ""); 
}

/**
 * 禁用编辑器
 * @returns
 */
function setDisabled() {
    UE.getEditor('editor').setDisabled('fullscreen');
}

/**
 * 启用编辑器
 * @returns
 */
function setEnabled() {
    UE.getEditor('editor').setEnabled();
}

/**
 * 第三层保存按钮点击事件
 */
function qyszSaveBtuFun() {
	
	var divStr = $("#add_qysz_dialog").attr("type");
	var contentTxt;
	var content = "";
	/****** 新增 *****/
	// 获取UEditor录入的值和html格式，用值判断是否录入了，存在值，就保存到后台html 
	if (divStr == "textCss") {
		// 获取文本字符串长度
		contentTxt = hasContentTxt();
		// 获取页面中文本的网页，然后写入到html中
		content = getContent();
	}
	/****** 新增 *****/
	
	
	var color = $("#"+divStr+"_color").val();
	var backgroundColor = $("#"+divStr+"_background-color").val();
	var fontColor = null;
	if(divStr == "textCss") {
		fontColor = $("#"+divStr+"_font-color").val();
	}
	// 验证数据，数据都通过保存提交  校验数据是否必填，要去掉
//	if (!$("#"+divStr+"_form").form("validate")){
//        return false;
//    }
	// 由于颜色块是隐藏的，默认都是黑色，所以不为空，下面的操作是为了获取填写的横纵坐标值，并判断操作类型是添加还是更新。
	if(color != "" && backgroundColor != "" && ((fontColor != null && fontColor != "") || (fontColor == null))) {
		// 表单验证通过，开始获取数据
		var getFormData = $("#"+divStr+"_form").serializeJson();
//		getFormData["color"] = color;
//		getFormData["background-color"] = backgroundColor;
//		if(fontColor != null && fontColor != "") {
//			getFormData["font-color"] = fontColor;
//		}
		// 全局openDialogIndex ，  获取表格数据，合并掉下标为index的数据， 重新加载表格
		var rows = add_fqqy_datagrid.datagrid('getRows');//获得所有行数据
		var row = rows[openDialogIndex];
		// 判断是修改
		if(getFormData.addType == "") {
			getFormData.addType = UPDATETYPE;
			// 合并当前数据和表格数据
			$.extend(row, getFormData);
		} else {
			getFormData.addType = row.addType;
			getFormData.fqlx = row.fqlx;
			getFormData.fqlxmc = row.fqlxmc;
			// 判断是添加，替换
			row = getFormData;
		}
		// 获取表格数据，并替换掉row.fileList数据， 全局表格数据中替换掉序号和内容
		var divStr = $("#add_qysz_dialog").attr("type");
		// 验证数据
		if(checkTable(divStr)) {
			var table = $("#"+ divStr +"_sz_table tbody tr:gt(0)");
			// 下列循环为获取录入的文本、广告、视频详情，由于文本格式调整为UEditor，所以不会创建table，则无法执行下列代码，获取文本qu_filelist为空。现将其调整为在循环外获取，写入到qu_filelist。
			$.each(table, function(tabIndex, tabObj) {
				var sjc = $(tabObj).attr("sjc");
				var xhValue = $(tabObj).find("[class='inputClass']").val();
				var fileValue;
				var fileSize;
				if(divStr == "textCss") {
					// 如果文本有内容，那么就拼接网页所需格式。
					if (contentTxt) {
						fileValue = "<!DOCTYPE html><html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body>" + content + "</body></html>";
					}
//					fileValue = $(tabObj).find("[class='fileClass']").val();
				} else {
					fileValue = $(tabObj).find("[typeClass='"+ divStr +"_sz_inputclass']").textbox("getValue");
					fileSize = $(tabObj).find("[typeClass='"+ divStr +"_sz_inputclass']").attr("fileSize");
				}
				$.each(qu_fileList, function(quIndex, qjObj) {
					if(sjc == qjObj.sjc) {
						// 获取图片和视频文件的数据，添加到qu_fileList的每一行数据中
						
						qjObj.xh = xhValue;
						qjObj.text = fileValue;
						if(divStr != "textCss") {
							qjObj["file-path"] = fileValue;
							qjObj["fileSize"] = fileSize;
						}
						return;
					}
				})
			})
			// 将UEditor中的数据写入到qu_fileList中，用来向后台发送，进行存储
			if(divStr == "textCss" && qu_fileList.length == 0) {
				if (contentTxt) {
					var fileValue = "<!DOCTYPE html><html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body>" + content + "</body></html>";
					var qjObj = {};
					// 获取图片和视频文件的数据，添加到qu_fileList的每一行数据中
					qjObj.sjc = getSjc();
					qjObj.text = fileValue;
					qjObj.xh = "1";
					qu_fileList.push(qjObj);
				}
			} 
			
			// 替换表格数据
			row.fileList = qu_fileList;
			rows[openDialogIndex] = row;
			loadAddFqqyDatagrid(rows);
			$("#add_qysz_dialog").dialog("close");
		}
	}
}

/**
 * 创建第三个dialog中下面表格
 */
function createSzTable(rowData, paramTableData, divStr, isShow) {
	// 清表格数据
	$("#" + divStr + "_sz_table tbody tr").remove();
	// 添加头
	var headTr = $("<tr>").attr("style", "height: 30px").append($("<td>").attr("style", "width: 15%").text("序号"))
			.append($("<td>").attr("style", "width: 55%").text(divAndTableMap[divStr]))
			.append($("<td>").attr("style", "width: 30%").text("操作"));
	$("#" + divStr + "_sz_table").append(headTr);
	// 数据按order排序再遍历
	// 遍历数据，添加sjc
	if(paramTableData.length > 0) {
		paramTableData.sort(sortXh);
		$.each(paramTableData, function(i, obj) {
			// 将写入到数据库里面的文本写入到UEditor中展示
			if (obj.fqlx == 0) {
				ue.setContent(obj.text.replace("<!DOCTYPE html><html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body>","").replace("</body></html>",""));
			}
			if(!obj.sjc) {
				obj.sjc = obj.fileid;
			}
			createTable(rowData, obj, divStr, isShow, paramTableData, i);
		})
	}
	// 判断添加按钮是否可用并添加点击事件
	if(isShow) {
		$("#" + divStr + "_sz_add_btu").attr("style", "width:80px;height:24px; margin-bottom: 10px; margin-left: 55px;display: block");
		// 添加按钮点击事件
		$("#" + divStr + "_sz_add_btu").unbind("click");
		$("#" + divStr + "_sz_add_btu").bind("click", function() {
			// 验证表格数据是否都不为空，才可以添加
			if(checkTable(divStr)) {
				createTable(rowData, null, divStr, isShow, paramTableData);
			}
		})
		// 背景图片上传按钮事件
		$("#" + divStr + "_upload").attr("style", "width:80px;height:24px; margin-bottom: 10px; margin-left: 55px;display: block");
		$("#" + divStr + "_upload").unbind("click");
		$("#" + divStr + "_upload").bind("click", function() {
			// 点击背景图片上传按钮时，标记背景图片文本框的id
			$("#reportDialog").attr("inputid", "#" + divStr + "_background-image");
			// 重新渲染filebox
			$("input[name=upfile]").val();
			$("#upfile").filebox({
				buttonText: "选择文件"
			});
			// 选择文件按钮右边显示
			$("#reportDialog span a:first").css("marginLeft",'80%');
			$("#reportDialog").dialog("open");
		})
	} else {
		$("#" + divStr + "_sz_add_btu").attr("style", "display: none");
		$("#" + divStr + "_upload").attr("style", "display: none");
		$("#add_qysz_dialog").dialog({});
	}
}

/**
 * 导入背景图片事件
 */
$("#reportUploadButton").bind("click", function() {
	var inputid = $(this).parent().attr("inputid");
	
	var uploadVideo = false;
	// 判断如果inputid == videoCss_sz_inputid, 证明是传入视频，
	if(inputid == "#videoCss_sz_inputid") {
		uploadVideo = true;
	}
	// 判断选择的文件是否是.xls文件
	var file = $("#upfile").filebox("getValue");
	if(checkFile(file, uploadVideo)) {
		var formData = new FormData();
		formData.append("file", $("input[name=upfile]")[0].files[0]);
		// inputid为在打开上传弹窗时，就已经添加这个属性了 475行
		sendUpload(formData, $(inputid));
	}
})

/**
 * 上传文件，并将返回路径赋值到文本框中
 * @returns
 */
function sendUpload(formData, inputObj) {
	$.ajax({
		url : '/uploadToFtp',
		type : 'POST',
		async : false,
		data : formData,
		// 告诉jQuery不要去处理发送的数据
		processData : false,
		// 告诉jQuery不要去设置Content-Type请求头
		contentType : false,
		success : function(data) {
			if(data.status == 1 || data.status == "1") {
				inputObj.textbox("setValue", data.path);
				var fileSize = $("input[name=upfile]")[0].files[0].size;
				inputObj.attr("fileSize",fileSize);
			}
			$("#reportDialog").dialog("close");
		}
	})
}

/**
 * 验证文件格式
 * 添加一个参数验证是传入视频还是图片
 */
function checkFile(filename, uploadVideo) {
	var suffix = filename.substring(filename.lastIndexOf('.') + 1);
	// 校验上传格式
//	if (filename == '' || filename == null) {
//		$.messager.alert("提示", "请选择上传文件", "warning");
//		return false;
//	} else if (suffix != "jpg" && suffix != "png" && suffix != "jpeg") {
//		$.messager.alert("提示", "请选择图片格式文件", "warning");
//		return false;
//	} else {
//		return true;
//	}
	if (filename == '' || filename == null) {
		$.messager.alert("提示", "请选择上传文件", "warning");
		return false;
	} else  {
		// 传视频
		if (uploadVideo) {
			if(suffix != "mp4") {
				$.messager.alert("提示", "请选择MP4格式文件", "warning");
				return false;
			} else {
				return true;
			}
		} else {
			// 图片
			if(suffix != "jpg" && suffix != "png" && suffix != "jpeg" && suffix != "mp4") {
				$.messager.alert("提示", "请选择图片或MP4格式文件", "warning");
				return false;
			} else {
				return true;
			}
		}
	}
}


/**
 * 创建第三步每一行表格
 * @param rowData 第二步当前行数据
 * @param obj  行数据，没有就传null
 * @param divStr  div前缀
 * @param isShow  是否显示删除按钮
 * @param paramTableData 表格数据
 * @returns
 */
function createTable(rowData, obj, divStr, isShow, paramTableData, index) {
	var haveTable = $("#" + divStr + "_sz_table tbody tr");
	// 判断分区类型设置中表格的记录数，如果分区类型是文本和广告(这里只判断广告(imageCss)，因为文本换成了UEditor，没有表格了)，那么只允许创建一个tr，视频没限制，否则大于1就给拦住。
	if (haveTable.length > 1 && divStr == "imageCss") {
		$.messager.alert("提示", "最多允许添加一条记录!");
		return false;
	}
	var tr = $("<tr>")
	if(obj != null) {
		tr.attr("sjc", obj.sjc);
	} else {
		// 添加进入，tr中添加时间戳，且表格数据添加事件戳，且第二层表格数据添加此表格数据
		var map = {};
		map.sjc = getSjc();
		tr.attr("sjc", map.sjc);
		if(paramTableData == null) {
			paramTableData = [];
		}
		paramTableData.push(map);
		obj = map;
	}
	var tdxh = $("<td>");
	var xuinput = $("<input>").addClass("inputClass");
	if(obj != null) {
		xuinput.val(obj.xh);
	}
	tr.append(tdxh.append(xuinput));
	// 操作列
	var tda = $("<td>");
	var setupButton = $('<a href="javascript:;" class="easyui-linkbutton zd-btn">').attr("style", "width:80px;height:24px;").text("样式设置");
	var delButton = $('<a href="javascript:;" class="easyui-linkbutton zd-btn">').attr("style", "width:80px;height:24px;").text("删除");
	// 绑定删除事件
	delButton.bind("click", function(){
		var sjc = $(this).parent().parent().attr("sjc");
		var delIndex;
		$.each(paramTableData, function(i2, obj2) {
			if(obj2.sjc == sjc) {
				delIndex = i2;
				return;
			}
		})
		paramTableData.splice(delIndex, 1);
		$(this).parent().parent().remove();
	});
	// 绑定设置列事件
	setupButton.bind("click", function(){
		var sjc = $(this).parent().parent().attr("sjc");
		// 根据divStr开启dialog,dialog中添加sjc属性
		$("#add_yssz_dialog").attr("sjc", sjc);
		$("#add_yssz_dialog").attr("type", divStr);
		// 关闭dialog中所有div，根据传入类型判断打开哪个div
		$.each(idAndDivMap, function(mapKey, mapValue) {
			if(divStr == mapValue) {
				$("#"+mapValue+"_yssz_div").attr("style", "display: block");
			} else {
				$("#"+mapValue+"_yssz_div").attr("style", "display: none");
			}
		})
		$("#"+divStr+"_yssz_form").form("reset");
		// 填充第四层数据
		fillForm(divStr+"_yssz", obj);
		// 如果是textCss需单独赋值font-color值
		if(divStr == "textCss") {
			if(obj['font-color'] != undefined) {
				var fontColor = obj['font-color'];
				$("#textCss_yssz_font-color").val(fontColor.substring(1)).attr("style", "height:27px;background-color:" + fontColor) 
			} else {
				$("#textCss_yssz_font-color").attr("style", "height:27px;background-color:#FFFFFF"); 
			}
		} 
		$("#add_yssz_dialog").dialog("open");
	});
	if(isShow) {
//		tda.append(setupButton);
		tda.append(delButton);
	}
	var tdArea = $("<td>");
	if(divStr == "textCss") {
		var area = $("<textarea>").addClass("fileClass");;
		if(obj != null) {
			area.val(obj.text);
		}
		tdArea.append(area);
	} else {
		// 图片/视频的表格填充和事件
		// 上传按钮绑定事件
		// 创建时，一个文本框和一个上传按钮相对应，给前缀为divStr + "_sz_input"+ index
		// 上传按钮同理。这样上传按钮点击时，会在选择文件dialog中添加inputid属性，上传dialog中上传按钮就会自动取这个值赋值到input中了
		var input = $("<input>").addClass(divStr+"_sz_inputclass").addClass("easyui-textbox").attr("typeClass", divStr+"_sz_inputclass")
			.attr("data-options", "required:true,editable:false").attr("style", "height: 30px; width: 70%");
		if(obj != null) {
			input.val(obj.text);
			input.attr("fileSize",obj.fileSize);
		}
		var abtu = $("<a>").addClass(divStr + "_sz_aclass").addClass("easyui-linkbutton zd-btn")
			.attr("style", "width:65px;height:24px; margin-top: 3px; margin-left: 5px").text("上传");
		// 上传按钮绑事件
		abtu.bind("click", function() {
			// 1. 表格内所有带这个class de _sz_inputclass id移除掉，
			var inputTalle = $("#" + divStr + "_sz_table").find("[typeClass='" + divStr + "_sz_inputclass']");
			$.each(inputTalle, function(inputTallei, inputTalleObj) {
				$(inputTalleObj).removeAttr("id");
			})
			// 2. 给当前按钮的input 加一个id
			var thisProInput = $(this).parent().find("[typeClass='" + divStr + "_sz_inputclass']");
			$(thisProInput).attr("id", divStr+"_sz_inputid");
			// 此id打开导入dialog时传入给保存使用
			$("#reportDialog").attr("inputid", "#" + divStr + "_sz_inputid");
			// 重新渲染filebox
			$("input[name=upfile]").val();
			$("#upfile").filebox({
				buttonText: "选择文件"
			});
			// 选择文件按钮右边显示
			$("#reportDialog span a:first").css("marginLeft",'80%');
			$("#reportDialog").dialog("open");
		})
		tdArea.append(input).append(abtu);
	}
	tr.append(tdArea);
	tr.append(tda);
	$("#" + divStr + "_sz_table tbody").append(tr);
	$.parser.parse($("#" + divStr + "_sz_div"));
}

/**
 * 验证第三层中表格数据
 */
function checkTable(divStr) {
	var table = $("#" + divStr + "_sz_table tbody tr");
	if(table.length >1) {
		var xhList = [];
		var isNull = false;
		$.each(table, function(i, obj) {
			if(i > 0) {
				var inputValue = $(obj).find("[class='inputClass']").val();
				var fileValue;
				if(divStr == "textCss") {
					fileValue = $(obj).find("[class='fileClass']").val();
				} else {
					fileValue = $(obj).find("[typeClass='"+ divStr +"_sz_inputclass']").textbox("getValue");
				}
				if(inputValue == "" || fileValue == "" ) {
					isNull = true;
				} else {
					xhList.push(inputValue);
				}
			}
		})
		if(isNull) {
			$.messager.alert("提示", "序号和内容不能为空!");
			return false;
		} else {
			if(!valiList(xhList)) {
				$.messager.alert("提示", "序号必须从1开始且连续!");
				return false;
			}
		}
	} 
	return true;
}
/**--------------------------------------第2个dialog方法结束-----------------------------------------------*/

/**--------------------------------------第3个dialog方法开始-----------------------------------------------*/

/**
 * 第四层保存按钮
 */
$("#add_yssz_save_btu").click(function() {
	var type = $("#add_yssz_dialog").attr("type");
	var sjc = $("#add_yssz_dialog").attr("sjc");
	// 如果表单获取为空就没有这个key
	var fourFormData = $("#"+type+"_yssz_form").serializeJson2();
	// 获取需要删除的key集合
	var keyList = keyMap[type+'Key'];
	// 如果是textCss，需要单独存一个颜色
	if(type == "textCss") {
		var fontColor = $("#textCss_yssz_font-color").val();
		if(fontColor != "") {
			fourFormData["font-color"] = "#"+fontColor;
		}
	}
	// 将数据存到第三层全局中
	$.each(qu_fileList, function(i, obj) {
		if(sjc == obj.sjc) {
			deleteKey(obj, keyList);
			$.extend(obj, fourFormData);
			return;
		}
	})
	$("#add_yssz_dialog").dialog("close");
})

/**
 * 根据key集合删除map中的此key数据
 * @param obj 要删除key的数据
 * @param keyList 删除的key集合
 */
function deleteKey(obj, keyList) {
	$.each(keyList, function(i, keyStr) {
		delete obj[keyStr];
	})
}

/**--------------------------------------第3个dialog方法结束-----------------------------------------------*/

/**--------------------------------------主页方法-----------------------------------------------*/

/**
 * 点击添加或修改跳转方法
 * @param type 判断添加或修改
 * @param data 后台查回数据
 * @returns
 */
function clearAndInitFqmbpz(type, paramdData) {
	$("#add_mbmc").textbox("setValue", "");
	$("#add_mbbh").textbox("setValue", "");
	$("#add_dialog").attr("type", type);
	if(ADDTYPE == type) {
		// 新建
		// 表格加载为空，数据空
		add_fqqy_datagrid.datagrid("loadData", {
			total : 0,
			rows : []
		});
	} else if(UPDATETYPE == type) {
		// 更新
		// 赋值文本框和表格加载
		var rowData = $("#datagrid").datagrid("getSelected");
		
		$("#add_mbid").textbox("setValue", rowData.mbid);
		$("#add_mblj").textbox("setValue", rowData.mblj);
		$("#add_mbmc").textbox("setValue", rowData.mbmc);
		$("#add_mbbh").textbox("setValue", rowData.mbbh);
		loadAddFqqyDatagrid(paramdData)
	}
	$("#add_dialog").dialog("open");
}

/**
 * 修改
 */
function updateFun() {
	var rowData = $("#datagrid").datagrid("getSelected");
	if(rowData != null && rowData != "") {
		var mbid = {};
		mbid.mbid = rowData.mbid;
		$.ajax({
	        url : "/selectQyszByMbidAndFqlx",  
	        type : 'POST',
	        data : mbid,	
	        success : function (data) {
	        	if(data.status == 1) {
	        		clearAndInitFqmbpz(UPDATETYPE, data.dataList);
	        	} else {
	        		$.messager.alert("提示", "查询失败");
	        	}
	        }
		});
	} else {
		$.messager.alert("提示", "请选择一条数据");
	}
}

/**
 * 删除
 */
function delFun() {
	var rowData = $("#datagrid").datagrid("getSelected");
	if(rowData != null && rowData != "") {
		// 打开删除提示消息对话框
		$.messager.confirm("操作提示", "您确定要执行删除操作吗？", function(data) {
			if (data) {
				var mbid = {};
				mbid.mbid = rowData.mbid;
				// 发送删除请求
				$.ajax({
			        url : "/deleteLedTemplateById",
			        type : 'POST',
			        data : mbid,	
			        success : function (data) {
			        	if(data = "1") {
			        		searchFqmb();
			        	} else {
			        		$.messager.alert("操作提示", data.message);
			        	}
			        }
			    });
			}
		});
	} else {
		$.messager.alert("提示", "请选择一条数据");
	}
}

/**
 * 查询按钮和初始化查询事件
 */
function searchFqmb() {
	var map = {};
	map.mbmc = $("#search_mbmc").textbox("getValue");
	map.mbbh = $("#search_mbbh").textbox("getValue");
	$('#datagrid').datagrid({
		url: "/selectLedTemplateByInfo",
		queryParams: map,
		loadFilter: pagerFilter
	});
}

function formatterFqlx(value, rowData, index) {
	return rowData.fqlxmc;
}

/**
 * 初始化加载所有下拉列表
 */
function initCombobox() {
//获取所有下拉列表数据
	var param = {};
	param.pzdbmList = JSON.stringify(zdbmList);
	$.post('/selectLedSjzdByPzdbmList' , param, function(data) {
		$.each(zdbmMap, function(key, value) {
			$.each(idAndDivMap, function(key2, divStr) {
				if ($('#' + divStr + "_" + value).length > 0) {
					$('#' + divStr + "_" + value).combobox({
						valueField: 'zdbm',
						textField: 'zdmc',
						data: data[key]
					})
				}
				if ($('#' + divStr + "_yssz_" + value).length > 0) {
					$('#' + divStr + "_yssz_" + value).combobox({
						valueField: 'zdbm',
						textField: 'zdmc',
						data: data[key]
					})
				}
			})
		})
	});
	
	// 线路需要加站点关联
	// 线路下拉框数据加载
	$('#fpzp_xlmc').combobox({
		url : '/selectXlxx?tag=1',
		valueField : 'xlmc',
		textField : 'lpmc',
		onLoadSuccess: function () { 
			//加载完成后,设置选中第一项
            var val = $(this).combobox("getData");
            for (var item in val[0]) {
                if (item == "xlmc") {
                    $(this).combobox("select", val[0][item]);
                }
            }
        },
		onChange : function(value) {
			// 站点下拉框数据加载
			$('#fpzp_zdmc').combobox({
				url : '/selectZdxx?tag=1&xlmc=' + value,
				valueField : 'zdid',
				textField : 'zdmc'
			});
		}
	});
	// 站点下拉列表
	$('#fpzp_zdmc').combobox({
		url : '/selectZdxx?tag=1',
		valueField : 'zdid',
		textField : 'zdmc',
		onLoadSuccess: function () { 
			//加载完成后,设置选中第一项
            var val = $(this).combobox("getData");
            for (var item in val[0]) {
                if (item == "zdid") {
                    $(this).combobox("select", val[0][item]);
                }
            }
        }
	})
}

function loadAddFqqyDatagrid(data) {
	add_fqqy_datagrid.datagrid({
		data: data
	});
}

/**
 * 初始化表格样式
 */
function initTable() {
	// 加载数据操作
	$('#datagrid').datagrid({
		striped : true,
		pagination : true, //分页是否显示
	    pageList : [10, 20, 30, 40, 50,100,200], //可以设置每页记录条数的列表
		singleSelect : true,
		selectOnCheck : true,
		rownumbers : true,
		fitColumns : true,
		pagination : true,
		pageSize : 10,
		loadMsg : '数据加载中请稍后……'
	});
}

function setHeight() {
	var h = $(window).height() - ($(".zd-nav").height() + 10 + 10)
			- ($(".zd-controller").height() + $(".panel-controller").height())
			- 70;
	$('#datagrid').datagrid('resize', {
		height : h
	});
}


//移到右边
$('#add').bind('click',function(){
//获取选中的选项，删除并追加给对方
	$('#select1 option:selected').appendTo('#select2');
});

//移到左边
$('#remove').bind('click', function(){
	$('#select2 option:selected').appendTo('#select1');
});

//全部移到右边
$('#add_all').bind('click',function(){
	//获取全部的选项,删除并追加给对方
	$('#select1 option').appendTo('#select2');
});

//全部移到左边
$('#remove_all').bind('click', function(){
	$('#select2 option').appendTo('#select1');
});

//双击选项
$('#select1').bind('dblclick', function(){
	//获取全部的选项,删除并追加给对方
	$("option:selected",this).appendTo('#select2'); //追加给对方
});

//双击选项
$('#select2').bind('dblclick', function(){
	$("option:selected",this).appendTo('#select1');
});

/**
 * 分配站牌按钮
 * @returns
 */
$("#fpzp_btu").bind('click', function() {
	// 重置分配站牌筛选
	$("#fpzp_ledmc").textbox("setValue", "");
	$("#fpzp_ledbh").textbox("setValue", "");
	$("#fpzp_xlmc").combobox("setValue", "");
	$("#fpzp_zdmc").combobox("setValue", "");
	// 判断是否选择
	var rowData = $("#datagrid").datagrid('getSelections');
	if (rowData.length < 1){
		$.messager.alert("提示", "请选择一条数据");
		return false;
	} else if (rowData.length == 1){
		// 查询数据
		searchLedList();
		// 打开dialog
		$("#fpzp_dialog").dialog('open');
	}
	
});

/**
 * 查询按钮和初始化查询事件
 */
function searchLedList() {
	// 获取选中的电子站牌
	var rowData = $("#datagrid").datagrid("getSelected");
	var map = {};
	map.mbid = rowData.mbid;
	map.ledmc = $("#fpzp_ledmc").textbox("getValue");
	map.ledbh = $("#fpzp_ledbh").textbox("getValue");
	map.xlbh = $("#fpzp_xlmc").combobox("getValue");
	map.zdbh = $("#fpzp_zdmc").combobox("getValue");
	$.post('/selectFpzpList', map, function(data){
		$("#select1").empty();
		$("#select2").empty();
		// 装配select的option
		$.each(data.usedLedList, function(i, val){
			var option = '<option value="' +data.usedLedList[i].ledid +'">'+ data.usedLedList[i].ledmc + '</option>'
			$("#select2").append(option);
		});
		$.each(data.ledList, function(i, val){
			var option = '<option value="' +data.ledList[i].ledid +'">'+ data.ledList[i].ledmc + '</option>'
			$("#select1").append(option);
		});
	})
}

/**
 * 保存站牌分配
 * @returns
 */
$("#add_zdsz_save_btu").bind('click', function() {
	// 获取已分配站牌列表
	var roomIds = $("#select2 option").map(function(){
		return $(this).val();
		}).get().join(',');
	// 获取选中模版数据
	var rowData = $("#datagrid").datagrid("getSelected");
	var param = {};
	param.mbid = rowData.mbid;
	param.ledList = roomIds;
	$.post('/saveFpzpToLedTempRel', param, function(data){
		if (data.status == "0") {
			$.messager.alert("提示", data.message);
			return false;
		} else {
			$("#fpzp_dialog").dialog("close");
			$.messager.alert("提示", data.message);
		}
	});
});

