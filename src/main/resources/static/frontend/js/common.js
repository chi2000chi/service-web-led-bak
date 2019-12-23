/*!
 * common v1.0.0
 * 公共部分js存放公共框架内容
 */

//格式化日期格式
function myformatter(date){
	if( typeof date == 'string'){
		date=new Date(date.replace(/-/g,'/'));
	}
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    var d = date.getDate();
    return y+'年'+(m<10?('0'+m):m)+'月'+(d<10?('0'+d):d)+'日';
}
function myparser(s){
    if (!s) return new Date();
    var ss = (s.split('-'));
    var y = parseInt(ss[0],10);
    var m = parseInt(ss[1],10);
    var d = parseInt(ss[2],10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
        return new Date(y,m-1,d);
    } else {
        return new Date();
    }
}


// JavaScript Document
$(document).ready(function(){
	//搜索区域点击清除按钮
	$(".clear").click(function(){
		$(".user-box input").val("");		
		$(".user-box select").val("");
		$(this).val("取消");
		$(this).siblings("input").val("查询");
	});//点击清除按钮	


	//搜索区域点击展开或收起
	$(".spread-btn").click(function(){
		if($(this).children("i").hasClass("glyphicon-menu-up")){
			$(this).html("");//清空内容
			$(this).html("展开&nbsp;&nbsp;<i class='glyphicon glyphicon-menu-down' data-toggle='collapse' data-target='#collapseExample'></i>");	 
		}else{
			$(this).html("");
			$(this).html("收起&nbsp;&nbsp;<i class='glyphicon glyphicon-menu-up' data-toggle='collapse' data-target='#collapseExample'></i>");
		}
	});//点击展开按钮 
});



//添加loading部分
$(document).ajaxStart(function(){
	addLoading();
});
$(document).ajaxStop(function(){
	$('#loading').remove();
});

function addLoading(){
	var loadId = $("#loading");
	if(loadId.length <= 0){//当页面没有loading正在显示的时候弹出loading层  
		var loadPanel = '<div id="loading"><div id="loading-main"><div class="loadEffect"><span></span><span></span><span></span><span></span><span></span><span></span> <span></span><span></span><p>加载中...</p></div></div></div>';
        $("body").prepend(loadPanel);
	}
}