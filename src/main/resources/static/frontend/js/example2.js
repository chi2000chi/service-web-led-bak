/*!
 * 
 * 示例
 */

//列表json结构
var td_data  = '{"total":24,"rows":[{"name":"厂家名称","type1":"类型1","type2":"操作类型1","ver":"1.0","date":"2017-1-6"},{"name":"厂家名称","type1":"类型1","type2":"操作类型1","ver":"1.0","date":"2017-1-6"},{"name":"厂家名称","type1":"类型1","type2":"操作类型1","ver":"1.0","date":"2017-1-6"},{"name":"厂家名称","type1":"类型1","type2":"操作类型1","ver":"1.0","date":"2017-1-6"},{"name":"厂家名称","type1":"类型1","type2":"操作类型1","ver":"1.0","date":"2017-1-6"}]}';
var td_obj = JSON.parse(td_data);

//列表json2结构
var td_data2  = '{"total":5,"rows":[{"name":"厂家名称","type":"类型1","fileName":"文件名称1","txxyName":"通讯协议1","ver":"1.0","remark":"备注"},{"name":"厂家名称","type":"类型1","fileName":"文件名称1","txxyName":"通讯协议1","ver":"1.0","remark":"备注"},{"name":"厂家名称","type":"类型1","fileName":"文件名称1","txxyName":"通讯协议1","ver":"1.0","remark":"备注"},{"name":"厂家名称","type":"类型1","fileName":"文件名称1","txxyName":"通讯协议1","ver":"1.0","remark":"备注"},{"name":"厂家名称","type":"类型1","fileName":"文件名称1","txxyName":"通讯协议1","ver":"1.0","remark":"备注"},{"name":"厂家名称","type":"类型1","fileName":"文件名称1","txxyName":"通讯协议1","ver":"1.0","remark":"备注"}]}';
var td_obj2 = JSON.parse(td_data2);

//页面加载
$(function(){
	//移除加载弹出框
	$('#loading').remove();
	//机构查看页面
	$("#show_btn").click(showInfo);
	//初始化隐藏dialog
	$('#show_dlg').dialog({
		closed : true
	});
	$('#edit_dlg').dialog({
		closed : true
	});
	setHeight();
	//绑定数据列表
	getData_dataGrid();
	
	//弹出层保存
	$("#save_btn").click(function(){
		$("#show_dlg").dialog('close');
	});
	
	//弹出层关闭
	$("#close_btn").click(function(){
		$("#show_dlg").dialog('close');
	});

	$('#testdlg1').dialog('close');
	$('#testdlg2').dialog('close');
	$('#testdlg3').dialog('close');
	$('#testdlg4').dialog('close');
	$('#testdlg5').dialog('close');
	$('#testdlg6').dialog('close');
	
	$('.btn-subShowDetail').click(function(){
		$(this).parent().next().slideToggle();
		$(this).toggleClass("on");
	});


	$('.btn-searchDetail').click(function(){
		$(".panel-controller").eq(0).toggleClass("hide");
		$(this).toggleClass("on");
		if($(this).hasClass("on")){
			$(this).html("展开搜索<span></span>")
		}else{
			$(this).html("收起搜索<span></span>")
		}
		setHeight();
	});



});

$("#test_show_btn1").bind("click", function() {
	$("#testdlg1").dialog("open");
	$("#testdlg1").window('center');
});
$("#test_show_btn2").bind("click", function() {
	$("#testdlg2").dialog("open");
	$("#testdlg2").window('center');

});
$("#btn5").bind("click", function() {
	$("#testdlg3").dialog("open");
	$("#testdlg3").window('center');

});
$("#test_show_btn4").bind("click", function() {
	$("#testdlg4").dialog("open");
	$("#testdlg4").window('center');
});
$("#test_show_btn5").bind("click", function() {
	$("#testdlg5").dialog("open");
	$("#testdlg5").window('center');
});

$("#test_show_btn6").bind("click", function() {
	$("#testdlg6").dialog("open");
	$("#testdlg6").window('center');
});

$(".showPanel-tab a").bind("click", function() {
	$(".showPanel-tab a").removeClass("on");
	$(this).addClass("on");

	$(".showPanel-tabPanel").css("left","-105%");
	$(".showPanel-tabPanel").eq($(this).index()).css("left",0);
});

//获取列表数据
function getData_dataGrid() {
	$('#datagrid').datagrid({
		striped: true,
		singleSelect: false,
		selectOnCheck:true,
		data: td_obj,
		loadMsg: '数据加载中请稍后……'
	});
}

function formatOper(val,row,index){
	return '<a href="#" class="icon-show icon-oper" onclick="operShowInfo('+index+')"></a><a href="#" class="icon-edit icon-oper" onclick="operEditInfo('+index+')"></a><a href="#" class="icon-del icon-oper" onclick="operDelInfo('+index+')"></a>';
}

function operShowInfo(index){
	$("#show_dlg").dialog('open');
	$("#show_dlg").window('center');
}
function operEditInfo(index){
	$("#show_dlg").dialog('open');
	$("#show_dlg").window('center');
}
function operDelInfo(index){
	$("#show_dlg").dialog('open');
	$("#show_dlg").window('center');
}

//弹出查看窗口
function showInfo(){
	$("#show_dlg").dialog('open');
	$("#show_dlg").window('center');
	getData_showDataGrid();
}

//获取弹出窗口列表数据
function getData_showDataGrid() {
	$('#shwo_datagrid').datagrid({
		striped: true,
		singleSelect: true,
		data: td_obj2,
		loadMsg: '数据加载中请稍后……'
	});
}
function setHeight(){
	var h = $(window).height() -($(".xl-nav").height()+10+10)-($(".zd-controller").height()+$(".panel-controller").height()) -70;
	$('#datagrid').datagrid('resize',{
		height: h
	});
}

$("#btn_test3").bind("click",function(){
	$("#testdlg2").dialog('open');
	$("#testdlg2").window('center');
});