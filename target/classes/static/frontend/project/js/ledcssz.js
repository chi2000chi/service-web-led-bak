var addForm = $("#add_form");

$(function(){
	
	initTable();
	searchLedcssz();
	 // 查询事件
	$("#search_btn").click(searchLedcssz);
	// 添加按钮点击事件
	$("#add_btu").click(function() {
		addForm.form("reset");
		$("#add_dialog").attr("type", "add");
		$("#add_dialog").dialog("open");
	});
	// 修改按钮点击事件
	$("#update_btu").click(updateFun);
	// 删除按钮点击事件
	$("#del_btu").click(delFun);
	// 添加dialog弹窗中保存按钮事件
	$("#add_save_btu").click(saveData);
	
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
})

/**
 * 保存数据
 */
function saveData() {
	if (!addForm.form("validate")){
        return false;
    }
	if(!checkDate()) {
		 return false;
	}
	// 获取是添加还是更新
	var type = $("#add_dialog").attr("type");
	// 获取数据
	var saveData = $("#add_form").serializeJson();
	if("add" == type) {
		// 新建
		$.ajax({
	        url : "/insertLedCsszb",
	        type : 'POST',
	        contentType: "application/json",
	        data : JSON.stringify(saveData),
	        success : function (data) {
	        	if(data.status != 0) {
	        		$("#add_dialog").dialog("close");
	        		searchLedcssz();
	        	}
	        	$.messager.alert("操作提示", data.message, "info");
	        }
	    });
	} else if("update" == type) {
		// 更新
		$.ajax({
	        url : "/updateLedCsszbById",
	        type : 'POST',
	        contentType: "application/json",
	        data : JSON.stringify(saveData),
	        success : function (data) {
	        	if(data.status != 0) {
	        		$("#add_dialog").dialog("close");
	        		searchLedcssz();
	        	}
	        	$.messager.alert("操作提示", data.message, "info");
	        }
	    });
	}
}

/**
 * 查询按钮和初始化查询事件
 */
function searchLedcssz() {
	var map = {};
	map.mbmc = $("#search_mbmc").textbox("getValue");
	map.mbbh = $("#search_mbbh").textbox("getValue");
	$('#datagrid').datagrid({
		url: "/selectLedCsszbByMbinfo",
		queryParams: map,
		loadFilter: pagerFilter
	});
}

/**
 * 修改
 */
function updateFun() {
	addForm.form("reset");
	$("#add_dialog").attr("type", "update");
	var rowData = $("#datagrid").datagrid("getSelected");
	if(rowData != null && rowData != "") {
		fillForm("add", rowData);
		$("#add_dialog").dialog("open");
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
				$.ajax({
			        url : "/deleteLedCsszbById",
			        type : 'POST',
			        data : mbid,	
			        success : function (data) {
			        	if(data != 0) {
			        		$.messager.alert("操作提示", "删除成功", "info");
			        	} else {
			        		$.messager.alert("操作提示", "删除失败", "info");
			        	}
			        	searchLedcssz();
			        }
			    });
			}
		});
	} else {
		$.messager.alert("提示", "请选择一条数据");
	}
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

/**
 * 日期范围控件
 * 
 * @returns
 */
function checkDate() {
	var startDate = $("#add_sbyxsjks").datebox("getValue");
	var endDate = $("#add_sbyxsjjs").datebox("getValue");
	var startDateYear = startDate.substring(0, 4);
	var endDateYear = endDate.substring(0, 4);
	var startDateMonth = startDate.substring(5);
	var endDateMonth = endDate.substring(5);
	var startDateDay = startDate.substring(8);
	var endDateDay = endDate.substring(8);
	if (startDate != null && endDate != "") {
		if (!(endDateYear > startDateYear || endDateYear > startDateYear || (endDateYear == startDateYear && parseInt(endDateMonth) >= parseInt(startDateMonth)))
				|| (endDateYear == startDateYear
						&& parseInt(endDateMonth) == parseInt(startDateMonth) && parseInt(startDateDay) > parseInt(endDateDay))) {
			$.messager.alert('提示', '结束日期不能小于开始日期!');
			return false;
		} else {
			return true;
		}
	}
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
 * 系统参数设置按钮
 * @returns
 */
$("#system_para_btu").bind('click', function() {
	// 查询线路列表
	selectSysParaList();
	// 打开dialog
	$("#system_para_dialog").dialog('open');
		
});

/**
 * 初始化查询事件
 */
function selectSysParaList() {
	// 获取选中的线路
	$.post('/selectSystemPara', function(data){
		$("#select1").empty();
		$("#select2").empty();
		// 装配select的option
		$.each(data.usedXlxxList, function(i, val){
			var option = '<option value="' +data.usedXlxxList[i].xlmc +'">'+ data.usedXlxxList[i].lpmc + '</option>'
			$("#select2").append(option);
		});
		$.each(data.xlxxList, function(i, val){
			var option = '<option value="' +data.xlxxList[i].xlmc +'">'+ data.xlxxList[i].lpmc + '</option>'
			$("#select1").append(option);
		});
		$("#led_ad_show_time").val(data.ledAdShowTime);
		$("#led_line_show_time").val(data.ledLineShowTime);
	})
}

/**
 * 保存线路分配
 * @returns
 */
$("#add_system_para_save_btu").bind('click', function() {
	// 获取已分配线路列表
	var roomIds = $("#select2 option").map(function(){
		return $(this).val();
		}).get().join(',');
	
	// 获取选中模版数据
	var param = {};
	param.un_show_line = roomIds;
	param.led_ad_show_time = $("#led_ad_show_time").val();
	param.led_line_show_time = $("#led_line_show_time").val();
	$.post('/updateSystemPara', param, function(data){
		if (data.status == "0") {
			$.messager.alert("提示", data.message);
			return false;
		} else {
			$("#system_para_dialog").dialog("close");
			$.messager.alert("提示", data.message);
			searchLedcssz();
		}
	});
});