//存下拉列表父编码
var zdbmList = ["18"];
//存下拉列表编码对应key数据
var zdbmMap = {18:"search_sbzt"};
$(function(){
	
	// 初始化表格
	initTable();
	// 初始化下拉列表
	comboboxInit();
	 // 查询事件
	$("#search_btn").click(searchLedList);
	
	searchLedList();
	
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
 * 查询按钮和初始化查询事件
 */
function searchLedList() {
	var map = {};
	map.ledmc = $("#search_ledmc").textbox("getValue");
	map.ledbh = $("#search_ledbh").textbox("getValue");
	map.xlbh = $("#search_xl").combobox("getValue");
	map.zdbh = $("#search_zd").combobox("getValue");
	map.jssjks = $("#search_jssjks").combobox("getValue");
	map.jssjjs = $("#search_jssjjs").combobox("getValue");
	map.sbzt = $("#search_sbzt").combobox("getValue");
	// 加载电子站牌监控状态列表
	$('#datagrid').datagrid({
		url: "/selectLedStateInfo",
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
//		return '异常    <a src="javascript:void(0);" style="cursor: pointer;" onclick="showSbzt('+index+')"> [+] </a>';
		return '异常' + '<span class="datagrid-row-expander datagrid-row-expand" style="display:inline-block;width:16px;height:16px;cursor:pointer;" onclick="showSbzt('+index+')"></span>'
	}
	return value;
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
//	$('#datagrid').datagrid("expandRow", index);
	
}


/**
 * 导出
 * @returns
 */
$("#export_btu").bind('click', function(){
	var data = '/exportledstate';
	$("#export_btu").attr('href', data)
});

/**
 * 下拉列表数据
 * 
 * @returns
 */
function comboboxInit() {
	
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
			            for (var item in val[0]) {
			                if (item == "zdbm") {
			                    $(this).combobox("select", val[0][item]);
			                }
			            }
			        }
				})
			}
		})
	});

	// 线路需要加站点关联
	// 线路下拉框数据加载
	$('#search_xl').combobox({
		url : '/selectXlxx?tag=1',
		valueField : 'xlmc',
		textField : 'lpmc',
//		onLoadSuccess: function () { 
//			//加载完成后,设置选中第一项
//			var val = $(this).combobox("getData");
//			for (var item in val[0]) {
//				if (item == "xlmc") {
//					$(this).combobox("select", val[0][item]);
//				}
//			}
//       },
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
		onLoadSuccess: function () {
			//加载完成后,设置选中第一项
            var val = $(this).combobox("getData");
            if (val.length > 0) {
            	var firstData = val[0];
            	$(this).combobox('setValue', firstData.xlmc);
            }
        },
		onChange : function(value) {
			$("#change_xlmc").textbox("setValue",value);
			// 站点下拉框数据加载
			$('#change_zdmc').combobox({
				url : '/selectZdxx?xlmc=' + value,
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
            	},
            	onSelect: function(rowData) {
            		$("#change_zdbh").textbox("setValue",rowData.zdid);
            		$("#change_dqzx").textbox("setValue", rowData.zdxh);
            	}
			});
		}
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
