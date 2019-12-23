var ADDTYPE = "ADD";
var UPDATETYPE = "UPDATE";
var openDialogIndex = "";
var add_fqqy_datagrid = $('#add_fqqy_datagrid');
var zdsmstatus = "close";
var sbztstatus = "close";
// 需要保存的电子站牌集合，只在参数下发时使用
var saveLedPara = [];
//存下拉列表父编码
var zdbmList = ["15","16","17","18","19","20","21"];
//存下拉列表编码对应key数据
var zdbmMap = {15:"add_ledfx", 16:"fx", 17:"search_zxzt", 18:"search_sbzt", 19:"change_zdtk", 20:"add_qyzt", 21:"change_qyzd"};
// 默认站点方向
var zdfx = 0;
$(function(){
	
	// 初始化表格
	initTable();
	// 初始化下拉列表
	comboboxInit(null);
	// 下拉框初始化加载
	$("#add_kzms").combobox({
		url:"/selectLedSjzdByPzdbm?pzdbm=1",
		valueField: "zdbm",
		textField: "zdmc"
	})
	$("#add_sbsjbh").combobox({
		url:"/selectLedSjzdByPzdbm?pzdbm=3",
		valueField: "zdbm",
		textField: "zdmc"
	})
	$("#add_sbfwcq").combobox({
		url:"/selectLedSjzdByPzdbm?pzdbm=2",
		valueField: "zdbm",
		textField: "zdmc"
	})
	$("#edit_kzms").combobox({
		url:"/selectLedSjzdByPzdbm?pzdbm=1",
		valueField: "zdbm",
		textField: "zdmc"
	})
	$("#edit_sbsjbh").combobox({
		url:"/selectLedSjzdByPzdbm?pzdbm=3",
		valueField: "zdbm",
		textField: "zdmc"
	})
	$("#edit_sbfwcq").combobox({
		url:"/selectLedSjzdByPzdbm?pzdbm=2",
		valueField: "zdbm",
		textField: "zdmc"
	})
	$('#edit_csmb').combobox({
		url : '/selectCsmbCombox',
		valueField : 'mbid',
		textField : 'mbmc'
	});
	// 添加按钮点击事件
	$("#add_btu").click(function() { //添加站牌
		//$('#add_fqqy_datagrid').datagrid('showColumn', 'aaa');  新增站牌的新增站点设置
		showInput(false);
		$("#add_dialog").dialog("open");
		$("#csmbdiv").show();
		$("#csmbiddiv").show();
		clearAndInitFqmbpz(ADDTYPE, null);
		
	})
	 // 查询事件
	$("#search_btn").click(searchLedList);
	
	searchLedList();
	
	$("#add_save_ledpara").click(function(){
		getLedParaSaveAndSend(false);
	});
	
	$("#add_save_send_ledpara").click(function(){
		getLedParaSaveAndSend(true);
	});
	
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
	
	$('#guali').combobox({
		onChange:function(){
		 		if($('#add_ledbh').textbox('getValue') != null && $('#add_ledbh').textbox('getValue') != ''){
		 			$('#add_ledbh').textbox('setValue',$('#add_ledbh').textbox('getValue').substring(0,6)+$('#guali').combobox('getValue')+$('#add_ledbh').textbox('getValue').substring(8,12));
		 		}
	 	}
	})
})

/**
 * 修改，新建，保存按钮
 * @returns
 */
$('#add_save_btu').bind('click', function () {
	// 获取站牌，参数，分区信息
	var getFormData = $("#add_form").serializeJson();
	
	//校验输入站牌编号是不是四位数字
	var ledbh=getFormData.ledbh;//输入的站牌编号
	var reg=/^\d{4}$/; 
	var reg1=/^\d{12}$/;  
	if(!reg.test(ledbh)&&!reg1.test(ledbh)){//不是四位数字
		$.messager.alert("提示", "输入站牌编号必须为四位数字");
		return;
	}
	if(reg.test(ledbh)){
	var guali=getFormData.guali;
	ledbh="000000"+guali+ledbh;//12位站牌编号 六个零+挂式立式+四位编号
	getFormData.ledbh=ledbh;
//	getFormData.ledmc=ledbh;
	}
//	getFormData.ledmc=
	//给json对象赋值,led编号,led名称跟编号一样
	
	//alert("getFormData:"+getFormData);
	
	// 获取表格变动行
	// 1. 获取添加行数据
	//var addList = $('#add_fqqy_datagrid').datagrid('getChanges', 'inserted');
	// 2. 获取删除行数据
	//var delList = $('#add_fqqy_datagrid').datagrid('getChanges', 'deleted');
	// 3. 获取修改行数据
	//var updateList =  $('#add_fqqy_datagrid').datagrid('getChanges', 'updated');
	//getFormData.addList = JSON.stringify(addList);
	//getFormData.delList = JSON.stringify(delList);
	//getFormData.updateList = JSON.stringify(updateList);
	$.post('/saveLedList', getFormData, function (data) {
		if (data.status == 1) {
			searchLedList();
			$("#add_dialog").dialog("close");
			$.messager.alert("提示", data.message);
		} else if (data.status == 0){
			$.messager.alert("提示", data.message);
			return false;
		}
	});
});

/**
 * 导入模版下载
 * 
 * @returns 计划导入模版.xls
 */
$("#mobanxiazai").bind("click", function() {
	var $form = $('<form method="GET"></form>');
	$form.attr('action', '/frontend/project/template/电子站牌导入模板.xls');
	$form.appendTo($('body'));
	$form.submit();
});

$('#del_btu').bind('click', function () {
	// 获取选中行
	var rowData = $("#datagrid").datagrid("getSelected");
	var checkedRows = $("#datagrid").datagrid('getSelections');
	if (rowData == null || rowData == '' || rowData == undefined) {
		$.messager.alert("操作提示", "请选择一条记录", "warning");
		return false;
	} if (checkedRows.length > 1) {
		$.messager.alert("操作提示", "请选择一条记录进行删除操作", "warning");
		return false;
	} else {
		// 打开删除提示消息对话框
		$.messager.confirm("操作提示", "您确定要执行删除操作吗？", function(data) {
			if (data) {
				// 调用删除的功能代码
				$.post('/delLedList', rowData, function (data) {
					if (data.status == 1) {
						searchLedList();
						$("#add_dialog").dialog("close");
						$.messager.alert("提示", data.message);
					} else if (data.status == 0){
						$.messager.alert("提示", data.message);
						return false;
					}
				});
			}
		});
	}
});

/**
 * 查询按钮和初始化查询事件
 */
function searchLedList() {
	var map = {};
	map.ledmc = $("#search_ledmc").textbox("getValue");// 站牌名称
	map.ledbh = $("#search_ledbh").textbox("getValue");//站牌编号
	/*map.xlbh = $("#search_xl").combobox("getValue");//线路
	map.zdbh = $("#search_zd").combobox("getValue");//站点
	map.zxzt = $("#search_zxzt").combobox("getValue");//在线状态
	map.sbzt = $("#search_sbzt").combobox("getValue");//设备状态 隐藏的
*/	$('#datagrid').datagrid({
		url: "/selectLedListAdd",
		queryParams: map,
		loadFilter: pagerFilter,
		view: detailview,
		detailFormatter: function(index, row){
			return '<div id="detailDiv'+index+'"></div>'
		}
	});
}

/**
 * 设备状态 “+” 点击事件
 */
var sbztFormatterFun = function(value,row,index) {
	if (value == '异常') {
		return '异常' + '<span class="datagrid-row-expander datagrid-row-expand" style="display:inline-block;width:16px;height:16px;cursor:pointer;" onclick="showSbzt('+index+')"></span>'
	}
	return value;
}

/**
 * 站点数量 “+” 点击事件
 */
var zdsmFormatterFun = function(value,row,index){
	return value + '<span class="datagrid-row-expander datagrid-row-expand" style="display:inline-block;width:16px;height:16px;cursor:pointer;" onclick="showZdsl('+index+')"></span>'
}

/**
 * 设备状态展示
 */
var showSbzt = function(index) {
		var rowData = $("#datagrid").datagrid("getData").rows[index];
		var table = $('<table>');
		var tr = $('<tr>');
		var td1 = $('<td>');
		var td2 = $('<td>');
		var td3 = $('<td>');
		var td4 = $('<td>');
		var td5 = $('<td>');
		var td6 = $('<td>');
		var td7 = $('<td>');
		var td8 = $('<td>');
		var td9 = $('<td>');
		var td10 = $('<td>');
		td1.text("震动传感器采集值").css('border','2').css('border-color','black').css('width','130px');
		td2.text("门禁传感器").css('border','2').css('border-color','black').css('width','130px');
		td3.text("水位传感器").css('border','2').css('border-color','black').css('width','130px');
		td4.text("加温模块状态").css('border','2').css('border-color','black').css('width','130px');
		td5.text("液晶开启状态").css('border','2').css('border-color','black').css('width','130px');
		td6.text("LED开启状态").css('border','2').css('border-color','black').css('width','130px');
		td7.text("风扇开启状态").css('border','2').css('border-color','black').css('width','130px');
		td8.text("照明开启状态").css('border','2').css('border-color','black').css('width','130px');
		td9.text("DVR开启状态").css('border','2').css('border-color','black').css('width','130px');
		td10.text("4G路由器开启状态").css('border','2').css('border-color','black').css('width','140px');
		tr.append(td1).css('border','2').css('border-color','black');
		tr.append(td2).css('border','2').css('border-color','black');
		tr.append(td3).css('border','2').css('border-color','black');
		tr.append(td4).css('border','2').css('border-color','black');
		tr.append(td5).css('border','2').css('border-color','black');
		tr.append(td6).css('border','2').css('border-color','black');
		tr.append(td7).css('border','2').css('border-color','black');
		tr.append(td8).css('border','2').css('border-color','black');
		tr.append(td9).css('border','2').css('border-color','black');
		tr.append(td10).css('border','2').css('border-color','black');
		table.append(tr);
		var tr1 = $('<tr>');
		var td11 = $('<td>');
		var td12 = $('<td>');
		var td13 = $('<td>');
		var td14 = $('<td>');
		var td15 = $('<td>');
		var td16 = $('<td>');
		var td17 = $('<td>');
		var td18 = $('<td>');
		var td19 = $('<td>');
		var td20 = $('<td>');
		$.each(rowData.ledStateMap, function(i, val){
			if (i == 'zdcgqcjz') {
				var state = rowData.ycStateMap[i];
				if (state == undefined || state == null || state == '') {
					td11.text("正常").css('border','2').css('border-color','black').css('width','130px');
				} else {
					td11.text("有震动").css('border','2').css('border-color','black').css('width','130px').css('background-color', 'red');
				}
			}
			if (i == 'mjcgq') {
				var state = rowData.ycStateMap[i];
				if (state == undefined || state == null || state == '') {
					td12.text("正常").css('border','2').css('border-color','black').css('width','130px');
				} else {
					td12.text("开启").css('border','2').css('border-color','black').css('width','130px').css('background-color', 'red');
				}
			}
			if (i == 'swcgq') {
				var state = rowData.ycStateMap[i];
				if (state == undefined || state == null || state == '') {
					td13.text("正常").css('border','2').css('border-color','black').css('width','130px');
				} else {
					td13.text("水浸").css('border','2').css('border-color','black').css('width','130px').css('background-color', 'red');
				}
			}
			if (i == 'jwmkzt') {
				var state = rowData.ycStateMap[i];
				if (state == undefined || state == null || state == '') {
					td14.text("开启").css('border','2').css('border-color','black').css('width','130px');
				} else {
					td14.text("关闭").css('border','2').css('border-color','black').css('width','130px').css('background-color', 'red');
				}
			}
			if (i == 'yjkqzt') {
				var state = rowData.ycStateMap[i];
				if (state == undefined || state == null || state == '') {
					td15.text("开启").css('border','2').css('border-color','black').css('width','130px');
				} else {
					td15.text("关闭").css('border','2').css('border-color','black').css('width','130px').css('background-color', 'red');
				}
			}
			if (i == 'ledkqzt') {
				var state = rowData.ycStateMap[i];
				if (state == undefined || state == null || state == '') {
					td16.text("开启").css('border','2').css('border-color','black').css('width','130px');
				} else {
					td16.text("关闭").css('border','2').css('border-color','black').css('width','130px').css('background-color', 'red');
				}
			}
			if (i == 'fskqzt') {
				var state = rowData.ycStateMap[i];
				if (state == undefined || state == null || state == '') {
					td17.text("开启").css('border','2').css('border-color','black').css('width','130px');
				} else {
					td17.text("关闭").css('border','2').css('border-color','black').css('width','130px').css('background-color', 'red');
				}
			}
			if (i == 'zmkqzt') {
				var state = rowData.ycStateMap[i];
				if (state == undefined || state == null || state == '') {
					td18.text("开启").css('border','2').css('border-color','black').css('width','130px');
				} else {
					td18.text("关闭").css('border','2').css('border-color','black').css('width','130px').css('background-color', 'red');
				}
			}
			if (i == 'dvrkqzt') {
				var state = rowData.ycStateMap[i];
				if (state == undefined || state == null || state == '') {
					td19.text("开启").css('border','2').css('border-color','black').css('width','130px');
				} else {
					td19.text("关闭").css('border','2').css('border-color','black').css('width','130px').css('background-color', 'red');
				}
			}
			if (i == 'glyqkqzt') {
				var state = rowData.ycStateMap[i];
				if (state == undefined || state == null || state == '') {
					td20.text("开启").css('border','2').css('border-color','black').css('width','130px');
				} else {
					td20.text("关闭").css('border','2').css('border-color','black').css('width','130px').css('background-color', 'red');
				}
			}
		})
		tr1.append(td11).css('border','2').css('border-color','black');
		tr1.append(td12).css('border','2').css('border-color','black');
		tr1.append(td13).css('border','2').css('border-color','black');
		tr1.append(td14).css('border','2').css('border-color','black');
		tr1.append(td15).css('border','2').css('border-color','black');
		tr1.append(td16).css('border','2').css('border-color','black');
		tr1.append(td17).css('border','2').css('border-color','black');
		tr1.append(td18).css('border','2').css('border-color','black');
		tr1.append(td19).css('border','2').css('border-color','black');
		tr1.append(td20).css('border','2').css('border-color','black');
		table.append(tr1);
		// 绘制表格 显示所有的状态，并标明异常状态
		$("#detailDiv"+index).html(table);
	
}

/**
 * 设备数量展示
 */
var showZdsl = function(index){
		var rowData = $("#datagrid").datagrid("getData").rows[index];
		var table = $('<table>');
		var tr = $('<tr>');
		var td1 = $('<td>');
		var td2 = $('<td>');
		var td3 = $('<td>');
		var td4 = $('<td>');
		var td5 = $('<td>');
		var td6 = $('<td>');
		td1.text("线路名称").css('border','2').css('border-color','black').css('width','130px');
		td2.text("线路编号").css('border','2').css('border-color','black').css('width','130px');
		td3.text("站点名称").css('border','2').css('border-color','black').css('width','130px');
		td4.text("站点编号").css('border','2').css('border-color','black').css('width','130px');
		td5.text("当前站序").css('border','2').css('border-color','black').css('width','130px');
		td6.text("站牌显示站序").css('border','2').css('border-color','black').css('width','130px');
		tr.append(td1).css('border','2').css('border-color','black');
		tr.append(td2).css('border','2').css('border-color','black');
		tr.append(td3).css('border','2').css('border-color','black');
		tr.append(td4).css('border','2').css('border-color','black');
		tr.append(td5).css('border','2').css('border-color','black');
		tr.append(td6).css('border','2').css('border-color','black');
		table.append(tr);
		$.each(rowData.stationList, function(i, val){
			var tr1 = $('<tr>');
			var td11 = $('<td>');
			var td12 = $('<td>');
			var td13 = $('<td>');
			var td14 = $('<td>');
			var td15 = $('<td>');
			var td16 = $('<td>');
			td11.text(val.xlmc).css('border','2').css('border-color','black').css('width','130px');
			td12.text(val.xlbh).css('border','2').css('border-color','black').css('width','130px');
			td13.text(val.zdmc).css('border','2').css('border-color','black').css('width','130px');
			td14.text(val.zdbh).css('border','2').css('border-color','black').css('width','130px');
			td15.text(val.dqzx).css('border','2').css('border-color','black').css('width','130px');
			td16.text(val.zpxssx).css('border','2').css('border-color','black').css('width','130px');
			tr1.append(td11).css('border','2').css('border-color','black');
			tr1.append(td12).css('border','2').css('border-color','black');
			tr1.append(td13).css('border','2').css('border-color','black');
			tr1.append(td14).css('border','2').css('border-color','black');
			tr1.append(td15).css('border','2').css('border-color','black');
			tr1.append(td16).css('border','2').css('border-color','black');
			table.append(tr1);
		})
		
		$("#detailDiv"+index).html(table);
}


/**
 * 更新电子站牌
 * @returns
 */

$("#update_btu").bind("click", function() {
	$('#add_fqqy_datagrid').datagrid('showColumn', 'aaa');
	var rowData = $("#datagrid").datagrid("getSelected");
	var checkedRows = $("#datagrid").datagrid('getSelections');
	if (checkedRows.length > 1) {
		$.messager.alert("提示", "请选择一条数据进行修改");
		return false;
	} else if(rowData != null && rowData != "") {
		$("#add_dialog").dialog("open");
		$("#csmbdiv").hide();
		$("#csmbiddiv").hide();
		showInput(false);
		var mbid = {};
		mbid.ledid = rowData.ledid;
		$.ajax({
	        url : "/selectLedList",  
	        type : 'POST',
	        data : mbid,	
	        success : function (data) {
        		clearAndInitFqmbpz(UPDATETYPE, data[0]);
	        }
		});
	} else {
		$.messager.alert("提示", "请选择一条数据");
		return false;
	}
	
})

/**
 * 查看电子站牌
 * @returns
 */

$("#look_btu").bind("click", function() {
	$('#add_fqqy_datagrid').datagrid('hideColumn', 'aaa');
	var rowData = $("#datagrid").datagrid("getSelected");
	var checkedRows = $("#datagrid").datagrid('getSelections');
	if (checkedRows.length > 1) {
		$.messager.alert("提示", "请选择一条数据进行查看");
		return false;
	} else if(rowData != null && rowData != "") {
		$("#add_dialog").dialog("open");
		$("#csmbdiv").hide();
		$("#csmbiddiv").hide();
		showInput(true);
		var mbid = {};
		mbid.ledid = rowData.ledid;
		$.ajax({
	        url : "/selectLedList",  
	        type : 'POST',
	        data : mbid,	
	        success : function (data) {
        		clearAndInitFqmbpz(UPDATETYPE, data[0].stationList);
	        }
		});
	} else {
		$.messager.alert("提示", "请选择一条数据");
		return false;
	}
})

/**
 * 导入
 * @returns
 */
$("#import_btu").bind('click', function(){
	// 重新渲染filebox
	$("input[name=upfile]").val();
	$("#upfile").filebox({
		buttonText: "选择文件"
	});
	// 选择文件按钮右边显示
	$("#reportDialog span a:first").css("marginLeft",'80%');
	// 打开上传
	$("#reportDialog").dialog('open');
});

/**
 * 导入按钮
 * 
 * @returns
 */
$("#reportUploadButton").bind("click", function() {
	// 判断选择的文件是否是.xls文件
	var file = $("#upfile").filebox("getValue");
	var suffix = file.substring(file.lastIndexOf('.') + 1);
	// 校验上传格式
	if (file == '' || file == null) {
		$.messager.alert("提示", "请选择上传文件", "warning");
		return false;
	} else if (suffix != "xls") {
		$.messager.alert("提示", "请选择模版下载的excel文件", "warning");
		return false;
	} else {
		var formData = new FormData();
		formData.append("file", $("input[name=upfile]")[0].files[0]);
		$.ajax({
			url : '/importLedExcel',
			type : 'POST',
			async : false,
			data : formData,
			// 告诉jQuery不要去处理发送的数据
			processData : false,
			// 告诉jQuery不要去设置Content-Type请求头
			contentType : false,
			success : function(data) {
				$("#reportDialog").dialog('close');

				// 根据状态判断显示信息
				if (data.status == 0) {
					if (data.filePath != null || data.filePath != undefined) {
						 $.messager.confirm('确认对话框','导入失败，是否查看错误详情？',function (r) {
						      if (r){  
						    	  	var form = $("<form></form>")
						            form.attr('action','/downErrorFile')
						            form.attr('method','post')
						            form.appendTo("body")
						            form.css('display','none')
						            form.submit()
						        }
						 });
					} else {
						$.messager.alert("操作提示", data.message, "warning");
					}
					return false;
				} else if (data.status == 1) {
					searchLedList();
					$.messager.alert("操作提示",data.message, "info");
				}
			}
		});
	}

});

/**
 * 查询参数
 * @returns
 */

$("#search_param_btu").bind("click", function() {
	var rowData = $("#datagrid").datagrid("getSelected");
	var checkedRows = $("#datagrid").datagrid('getSelections');
	if (checkedRows.length > 1) {
		$.messager.alert("提示", "请选择一条数据进行查询");
		return false;
	} else if(rowData != null && rowData != "") {
		var mbid = {};
		mbid.ledid = rowData.ledid;
		$.ajax({
	        url : "/selectLedParaInfo",  
	        type : 'POST',
	        data : mbid,	
	        success : function (data) {
        		if (data.status == "1") {
        			// 展示数据
        			$("#add_tcpip").textbox('setValue', data.tcpycfwqipdz);
        			$("#add_tcpport").textbox('setValue', data.yctcpfwdk);
        			$("#add_sbyxsjks").datebox('setValue', data.sbyxksrq);
        			$("#add_sbyxsjjs").datebox('setValue', data.sbyxjzrq);
        			$("#add_mtxhsjd1").timespinner('setValue', data.xhsjdkssj);
        			$("#add_mtxhsjd2").timespinner('setValue', data.xhsjdjzsj);
        			$("#add_sbwkfw").textbox('setValue', data.sbwdfwks + "," + data.sbwdfwjz);
        			$("#add_wbfs").textbox('setValue', data.wbfsks + "," + data.wbfsjz);
        			$("#add_hlfs").textbox('setValue', data.hlfsks + "," + data.hlfsjz);
        			$("#add_jwmk").textbox('setValue', data.jwmkks + "," + data.jwmkjz);
        			$("#add_kzms").combobox('setValue', data.kzms);
        			$("#add_dwfw").textbox('setValue', data.dwfw);
        			$("#add_sbglbh").textbox('setValue', data.sbglbhks + "," + data.sbglbhjz);
        			$("#add_sbsjbh").combobox('setValue', data.sbsjbh);
        			$("#add_sbfwcq").combobox('setValue', data.sbfwzq);
        			$("#ledpara_dialog").dialog("open");
        		} else {
        			$.messager.alert("提示", data.message);
        		}
        		
	        }
		});
	} else {
		$.messager.alert("提示", "请选择一条数据");
		return false;
	}
})


/**
 * 部分参数设置
 */
$("#some_param_set_btu").bind('click', function () {
	saveLedPara = [];
	$("#save_ledpara_form").form('reset');
	var checkedRows = $("#datagrid").datagrid('getSelections');
	if (checkedRows.length < 1) {
		$.messager.alert("提示", "请选择至少一条数据");
		return false;
	} else if (checkedRows.length == 1) {
		saveLedPara.push(checkedRows[0].ledid);
		$("#csmb_subtitle").hide();
		$("#csmb_row_clearfix").hide();
		var mbid = {};
		mbid.ledid = checkedRows[0].ledid;
		$.ajax({
	        url : "/selectLedParaInfo",  
	        type : 'POST',
	        data : mbid,	
	        success : function (data) {
        		if (data.status == "1") {
        			// 展示数据
        			$("#edit_tcpip").textbox('setValue', data.tcpycfwqipdz);
        			$("#edit_tcpport").textbox('setValue', data.yctcpfwdk);
        			$("#edit_sbyxsjks").datebox('setValue', data.sbyxksrq);
        			$("#edit_sbyxsjjs").datebox('setValue', data.sbyxjzrq);
        			$("#edit_mtxhsjd1").timespinner('setValue', data.xhsjdkssj);
        			$("#edit_mtxhsjd2").timespinner('setValue', data.xhsjdjzsj);
        			$("#edit_sbwkfw").textbox('setValue', data.sbwdfwks + "," + data.sbwdfwjz);
        			$("#edit_wbfs").textbox('setValue', data.wbfsks + "," + data.wbfsjz);
        			$("#edit_hlfs").textbox('setValue', data.hlfsks + "," + data.hlfsjz);
        			$("#edit_jwmk").textbox('setValue', data.jwmkks + "," + data.jwmkjz);
        			$("#edit_kzms").combobox('setValue', data.kzms);
        			$("#edit_dwfw").textbox('setValue', data.dwfw);
        			$("#edit_sbglbh").textbox('setValue', data.sbglbhks + "," + data.sbglbhjz);
        			$("#edit_sbsjbh").combobox('setValue', data.sbsjbh);
        			$("#edit_sbfwcq").combobox('setValue', data.sbfwzq);
        			$("#save_ledpara_dialog").dialog("open");
        		} else {
        			$.messager.alert("提示", data.message);
        		}
	        }
		});
	} else {
		// 获取选中行
		var checkedRows = $("#datagrid").datagrid('getSelections');
		$.each(checkedRows, function (i, val) {
			saveLedPara.push(val.ledid);
		})
		$("#csmb_subtitle").show();
		$("#csmb_row_clearfix").show();
		$("#save_ledpara_dialog").dialog("open");
	}
});

/**
 * 全部参数设置
 */
$("#all_param_set_btu").bind('click', function () {
	saveLedPara = [];
	// 设置全部选中
	$('#datagrid').datagrid('selectAll');
	// 获取选中行
	var checkedRows = $("#datagrid").datagrid('getSelections');
	$.each(checkedRows, function (i, val) {
		saveLedPara.push(val.ledid);
	})
	$("#save_ledpara_form").form('reset');
	$("#save_ledpara_dialog").dialog("open");
	
});

/**
 * 读取模版
 * @returns
 */
$("#read_csmb_btn").bind('click', function(){
	var csmbid = $("#edit_csmb").combobox('getValue');
	if (csmbid == '' || csmbid == null || csmbid == undefined) {
		$.messager.alert("提示", "请选择参数模版！");
		return false;
	}
	var mbid = {};
	mbid.mbid = csmbid;
	$.ajax({
        url : "/selectLedCsszbByMbinfo",  
        type : 'POST',
        data : mbid,	
        success : function (data) {
			// 展示数据
			$("#edit_tcpip").textbox('setValue', data[0].tcpip);
			$("#edit_tcpport").textbox('setValue', data[0].tcpport);
			$("#edit_sbyxsjks").datebox('setValue', data[0].sbyxsjks);
			$("#edit_sbyxsjjs").datebox('setValue', data[0].sbyxsjjs);
			$("#edit_mtxhsjd1").timespinner('setValue', data[0].mtxhsjd1);
			$("#edit_mtxhsjd2").timespinner('setValue', data[0].mtxhsjd2);
			$("#edit_sbwkfw").textbox('setValue', data[0].sbwkfw);
			$("#edit_wbfs").textbox('setValue', data[0].wbfs);
			$("#edit_hlfs").textbox('setValue', data[0].hlfs);
			$("#edit_jwmk").textbox('setValue', data[0].jwmk);
			$("#edit_kzms").combobox('setValue', data[0].kzms);
			$("#edit_dwfw").textbox('setValue', data[0].dwfw);
			$("#edit_sbglbh").textbox('setValue', data[0].sbglbh);
			$("#edit_sbsjbh").combobox('setValue', data[0].sbsjbh);
			$("#edit_sbfwcq").combobox('setValue', data[0].sbfwcq);
        }
	});
});

/**
 * 保存或保存并下发参数信息
 * @param isSend 是否下发
 * @returns
 */
function  getLedParaSaveAndSend(isSend){
	// 获取表单中的数据
	var getLedParaFormData = $("#save_ledpara_form").serializeJson();
	var param = {};
	param.ledidList = JSON.stringify(saveLedPara);
	param.ledPara = JSON.stringify(getLedParaFormData);
	param.isSend = isSend;
	$.ajax({
        url : "/saveOrSaveSendLedPara",  
        type : 'POST',
        data : param,	
        success : function (data) {
        	if (data.status == "1") {
        		searchLedList();
        		$.messager.alert("提示", data.message);
        		$("#save_ledpara_dialog").dialog("close");
        	} else{
        		$.messager.alert("提示", data.message);
        		return false;
        	}
        }
	});
}

/**
 * 控制站点编辑页面标签
 * @param status
 * @returns
 */
function showInput(status){
	if (status) {
		$("#add_form :input").attr("disabled", "disabled");
		$('#add_form .easyui-combobox').combobox({disabled:true});
		$("#add_fqqysz_btu").hide();
		$("#add_save_btu").hide();
		$("table .easyui-linkbutton").hide();
	} else {
		$("#add_form :input").removeAttr("disabled", "disabled");
		$('#add_form .easyui-combobox').combobox({disabled:false});
		$("#add_fqqysz_btu").show();
		$("#add_save_btu").show();
		$("table .easyui-linkbutton").show();
	}
}

/**
 * 点击添加或修改跳转方法
 * @param type 判断添加或修改
 * @param data 后台查回数据
 * @returns
 */
function clearAndInitFqmbpz(type, paramdData) {
	$("#add_dialog").dialog("open");
	$("#add_form input.easyui-textbox").textbox("setValue", "");
	$("#add_dialog").attr("type", type);
	$("#cssz" ).css("display", "none"); // 参数设置
	$("#ipziym" ).css("display", "none"); // 本地ip 子网掩码
	$("#fqmb" ).css("display", "none"); // 分区模板
	if(ADDTYPE == type) {//增加
		
		//添加站牌的值 写死
		// 经度
		$("#add_jd").textbox('setValue','126');
		// 维度
		$("#add_wd").textbox('setValue','45');
		// 网关
		$("#add_wg").textbox('setValue','192.168.1.1');
		// 本地设备ip 地址
		$("#add_bdsbipdz").textbox('setValue','192.168.1.120');
		// 子网掩码
		$("#add_zwym").textbox('setValue','255.255.255.0');
		
		
		
		// 隐藏
//		$("#jw" ).css("display", "none"); //经纬度
//		$("#fxzt" ).css("display", "none"); // 方向状态
		$("#cssz" ).css("display", "none"); // 参数设置
		$("#ipziym" ).css("display", "none"); // 本地ip 子网掩码
		$("#fqmb" ).css("display", "none"); // 分区模板
//		$("#zpmc" ).css("display", "none"); //站牌名称标题
//		$("#zpmcz" ).css("display", "none"); //站牌名称
		
		mbdataCombobox();
		// 新建
		// 表格加载为空，数据空
		add_fqqy_datagrid.datagrid("loadData", {
			total : 0,
			rows : []
		});
		// 站点停靠和站点启用默认设置为默认值，停靠和禁用
		$("#change_zdtk").combobox('setValue', 0);
		$("#change_qyzd").combobox('setValue', 0);
	} else if(UPDATETYPE == type) {
		// 更新
		// 赋值文本框和表格加载
		//var rowData = $("#datagrid").datagrid("getSelected");
		$("#add_ledid").textbox("setValue", paramdData.ledid);
		$("#add_ledmc").textbox("setValue", paramdData.ledmc);	
		$("#add_ledbh").textbox("setValue", paramdData.ledbh);
		$("#add_jd").textbox("setValue", paramdData.jd);
		$("#add_wd").textbox("setValue", paramdData.wd);
		$("#add_ledfx").combobox('setValue', paramdData.ledfxbm);
		$("#add_qyzt").combobox('setValue', paramdData.qyzt);
		$("#change_qyzd").combobox('setValue', paramdData.qyzd);
		$("#add_wg").textbox("setValue", paramdData.wg);
		$("#add_bdsbipdz").textbox("setValue", paramdData.bdsbipdz);
		$("#add_zwym").textbox("setValue", paramdData.zwym);
		$("#add_fqmbid").combobox('setValue', paramdData.ledmbid);
		$("#change_zdtk").combobox('setValue', paramdData.zdtk);
		loadAddFqqyDatagrid(paramdData)
		
	}
}
function loadAddFqqyDatagrid(data) {
	add_fqqy_datagrid.datagrid({
		data: data,
		onLoadSuccess: function(data) {
			$('[name=fqqy_datagrid_delRowBtn]').click(delRowFun);
			$('[name=fqqy_datagrid_updateRowBtn]').click(updateRowFun);
		}
	});
}

/**
 * 行内设置按钮
 */
function updateRowFun() {
	var index = $('[name=fqqy_datagrid_updateRowBtn]').index(this);
	openDialogIndex = index;
	// 选中行数据
	var row = getRowdataForIndex(index);
	var a =  $.isEmptyObject(row)
	// 修改将选中行数据展示到dialog
	if (!a) {
		zdfx = row.fx;
		$("#change_lpmc").combobox('setValue', row.xlbh);
		$("#change_xlmc").textbox('setValue', row.xlbh);
		$("#fx").combobox('setValue', row.fx);
		$("#change_zdmc").combobox('setValue', row.zdbh);
		$("#change_zdbh").textbox('setValue', row.zdbh);
		$("#change_dqzx").textbox('setValue', row.dqzx);
		$("#change_zpxssx").textbox('setValue', row.zpxssx);
		$("#change_zdtk").combobox('setValue', row.zdtk);
		$("#change_qyzd").combobox('setValue', row.qyzd);
	} else {
		comboboxInit(1);
		$("#change_zpxssx").textbox('setValue', "");
		$("#change_xlmc").textbox('setValue', "");
		$("#change_zdbh").textbox('setValue', "");
		$("#change_dqzx").textbox('setValue', "");
	}
	// 打开dialog
	$("#add_zdsz_dialog").dialog("open");
}

/**
 * 站点数据保存
 * @returns
 */
$("#add_zdsz_save_btu").bind('click', function(){
	// 验证数据，数据都通过保存提交
	if (!$("#textCss_yssz_form").form("validate")){
        return false;
    }
	// 获取全部form中填入的数据
	var getFormData = $("#textCss_yssz_form").serializeJson();
	// 处理线路名称
	getFormData.xlmc = $('#change_lpmc').combobox('getText');
	// 处理站点名称
	getFormData.zdmc = $('#change_zdmc').combobox('getText');
	// 数据写入表格对应行中
	$('#add_fqqy_datagrid').datagrid('updateRow',{
		index: openDialogIndex,	
		row: getFormData
	});
	$('[name=fqqy_datagrid_delRowBtn]').eq(openDialogIndex).click(delRowFun);
	$('[name=fqqy_datagrid_updateRowBtn]').eq(openDialogIndex).click(updateRowFun);
	// 关闭弹窗
	$("#add_zdsz_dialog").dialog("close");
})

/**
 * 获取当前行数据
 */
function getRowdataForIndex(index) {
	// 获取当前行数据，开启弹窗
	var rows = add_fqqy_datagrid.datagrid('getRows');//获得所有行
    var row = rows[index];
    return row;
}

/**
 * 下拉列表数据
 * 
 * @returns
 */
function comboboxInit(status) {
	// 初始化的时候，加载下拉列表数据，修改的时候不需要再加载
	if (status == null) {
		//获取数据字典下拉列表数据
		var param = {};
		param.pzdbmList = JSON.stringify(zdbmList);
		param.tag = "1";
		$.post('/selectLedSjzdByPzdbmList' , param, function(data) {
			$.each(zdbmMap, function(key, value) {
				if ($('#' + value).length > 0) {
					$('#' + value).combobox({
						valueField: 'zdbm',
						textField: 'zdmc',
						data: data[key],
						onLoadSuccess: function () { 
							//加载完成后,设置选中第一项
				            var val = $(this).combobox("getData");
				            if(val[0].pzdbm==15){//led 电子站牌方向
				            	for (var item in val[0]) {
				            		if (item == "zdbm") {
				            			$(this).combobox("select", val[1][item]);
				            		}
				            	}
				            }else if(val[0].pzdbm==20){// 
				            	for (var item in val[0]) {
				            		if (item == "zdbm") {
				            			$(this).combobox("select", val[1][item]);
				            		}
				            	}
				            }else{
				            	for (var item in val[0]) {
				            		if (item == "zdbm") {
				            			$(this).combobox("select", val[0][item]);
				            		}
				            	}
				            }
				        }
					})
				}
			})
		});
	}
	// 线路需要加站点关联
	// 线路下拉框数据加载
	$('#search_xl').combobox({
		url : '/selectXlxx?tag=1',
		valueField : 'xlmc',
		textField : 'lpmc',
//		onLoadSuccess: function () { 
//			//加载完成后,设置选中第一项
//            var val = $(this).combobox("getData");
//            for (var item in val[0]) {
//                if (item == "xlmc") {
//                    $(this).combobox("select", val[0][item]);
//                }
//            }
//        },
		onChange : function(value) {
			// 站点下拉框数据加载
			$('#search_zd').combobox({
				url : '/selectZdxx?tag=1&xlmc=' + value,
				valueField : 'zdid',
				textField : 'zdmc'
			});
		}
	});
	// 站点下拉列表
	$('#search_zd').combobox({
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
	});

	// 站点设置时下拉列表数据
	// 线路下拉框数据加载
	$('#change_lpmc').combobox({
		url : '/selectXlxx',
		valueField : 'xlmc',
		textField : 'lpmc',
//		onLoadSuccess: function () {
//			//加载完成后,设置选中第一项
//            var val = $(this).combobox("getData");
//            if (val.length > 0) {
//            	var firstData = val[0];
//            	$(this).combobox('setValue', firstData.xlmc);
//            }
//        },
		onChange : function(value) {
			$("#change_xlmc").textbox("setValue",value);
//    		var zdfx = $('#fx').combobox("getValue");
			// 站点下拉框数据加载
			$('#change_zdmc').combobox({
				url : '/selectZdxx?xlmc=' + value + '&zdfx=' + zdfx,
				valueField : 'zdid',
				textField : 'zdmc',
//            	onLoadSuccess: function () { 
//            		//加载完成后,设置选中第一项
//            		var val = $(this).combobox("getData");
//            		for (var item in val[0]) {
//            			if (item == "zdid") {
//            				$(this).combobox("select", val[0][item]);
//            			}
//            		}
//            	},
            	onSelect: function(rowData) {
            		$("#change_zdbh").textbox("setValue",rowData.zdid);
            		$("#change_dqzx").textbox("setValue", rowData.zdxh);
            	}
			});
		}
	});
	
	// 站点设置方向选择
	$('#fx').combobox({
		onChange : function(value) {
			var xlmc = $("#change_xlmc").textbox("getValue");
			// 如果没有选择线路，那么方向的变更逻辑被禁用。
			if (xlmc == undefined || xlmc == null || xlmc == '') {
				return false;
			}
    		zdfx = $('#fx').combobox("getValue");
			// 站点下拉框数据加载
			$('#change_zdmc').combobox({
				url : '/selectZdxx?xlmc=' + xlmc + '&zdfx=' + zdfx,
				valueField : 'zdid',
				textField : 'zdmc',
//            	onLoadSuccess: function () { 
//            		//加载完成后,设置选中第一项
//            		var val = $(this).combobox("getData");
//            		for (var item in val[0]) {
//            			if (item == "zdid") {
//            				$(this).combobox("select", val[0][item]);
//            			}
//            		}
//            	},
            	onSelect: function(rowData) {
            		$("#change_zdbh").textbox("setValue",rowData.zdid);
            		$("#change_dqzx").textbox("setValue", rowData.zdxh);
            	}
			});
		}
	});
	
	// 模版信息只有初始化的时候加载，其余状态不加载
	if (status == null) {
		mbdataCombobox();
	}

}

/**
 * 参数模版和分区模版下拉列表
 * @returns
 */
function mbdataCombobox() {
	// 参数模版下拉列表
	$('#add_csmbid').combobox({
		url : '/selectCsmbCombox',
		valueField : 'mbid',
		textField : 'mbmc',
		onLoadSuccess : function() {
			// 加载完成后,设置选中第一项
			var val = $(this).combobox("getData");
			for ( var item in val[0]) {
				if (item == "mbid") {
					//加载完成后,设置选中第一项
					//$(this).combobox("select", val[0][item]);
					//加载完成后,设置选中第二项
					$(this).combobox("select", val[1][item]);
				}
			}
		}
	});
	// 分区模版下拉列表
	$('#add_fqmbid').combobox({
		url : '/selectTemplateList',
		valueField : 'mbid',
		textField : 'mbmc',
		onLoadSuccess : function() {
			// 加载完成后,设置选中第一项
			var val = $(this).combobox("getData");
			for ( var item in val[0]) {
				if (item == "mbid") {
					// 加载完成后,设置选中第一项
					//$(this).combobox("select", val[0][item]);
					// 加载完成后,设置选中第四项
					$(this).combobox("select", val[3][item]);
				}
			}
		}
	});
}


/**
 * 行内删除按钮
 */
function delRowFun() {
	// 删除一行
	add_fqqy_datagrid.datagrid('deleteRow', $('[name=fqqy_datagrid_delRowBtn]').index(this));
}

/**
 * dialog中新增按钮，表格新增一行
 */
$("#add_fqqysz_btu").click(function() {
	var rowData = {};
//	rowData.addType = ADDTYPE;
	// 创建一行新数据
	add_fqqy_datagrid.datagrid('appendRow', rowData);
	// 设置新增行选中
	var editIndex = add_fqqy_datagrid.datagrid('getRows').length-1;
	add_fqqy_datagrid.datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
	bindAddRowBtn(editIndex);
})

/**
 * 
 * @param index
 * @returns
 */
function bindAddRowBtn(index) {
	$('[name=fqqy_datagrid_delRowBtn]').eq(index).click(delRowFun);
	$('[name=fqqy_datagrid_updateRowBtn]').eq(index).click(updateRowFun);
}

/**
 * 分区模板配置表格操作栏加载方法
 */
function formatterOperation(value,row,index) {
	var add = '<a href="javascript:;" class="easyui-linkbutton zd-btn" style="width:65px;height:24px;" name="fqqy_datagrid_updateRowBtn">设置</a>';
	var del = '<a href="javascript:;" class="easyui-linkbutton zd-btn" style="width:65px;height:24px;" name="fqqy_datagrid_delRowBtn">删除</a>'
	return add + del;
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

