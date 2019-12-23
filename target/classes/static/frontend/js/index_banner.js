// JavaScript Document
$(document).ready(function(){
	var num = $('.banners>div').length;//全局变量轮播数
	//alert(num)
	 $('.banner-arrow-right').click(function(){
		 $('.con-tools>a').children('.icon-tool-1').css('display','block').siblings('.icon-tool-2').css('display','none');		 
		 for(i=-1; i<num; i++){
			 if($('.banner').eq(i).hasClass('active')){
				 $('.con-tools>a').eq(i+1).children('.icon-tool-1').css('display','none').siblings('.icon-tool-2').css('display','block');
				 }
			 }//end-for
		 });//单击向右切换按钮
	
	 $('.banner-arrow-left').click(function(){
		$('.con-tools>a').children('.icon-tool-1').css('display','block').siblings('.icon-tool-2').css('display','none');		 
		 for(i=num; i>=0; i--){
			 if($('.banner').eq(i).hasClass('active')){
				 $('.con-tools>a').eq(i-1).children('.icon-tool-1').css('display','none').siblings('.icon-tool-2').css('display','block');
				 }
			 }//end-for
		});//单击向左切换按钮
	 	
	 $('.arrow-btn').click(function(){
		$('.con-tools>a').children('.icon-tool-1').css('display','block').siblings('.icon-tool-2').css('display','none');
		var arrow_index = $(this).index();//6个切换按钮
		$('.con-tools>a').eq(arrow_index).children('.icon-tool-1').css('display','none').siblings('.icon-tool-2').css('display','block');		 
		 });//点击6个切换按钮	 
	
	 $('.con-tools>a').click(function(){
		 var icon_index = $(this).index();
		 $('.con-tools>a').children('.icon-tool-1').css('display','block').siblings('.icon-tool-2').css('display','none');
		 $(this).find('.icon-tool-1').css('display','none').siblings('.icon-tool-2').css('display','block');		
	});
	 
	 //返回首页后，轮播定位
	 var arrowIndex =$('#arrowIndex').val();
	 if(arrowIndex != "undefined" && arrowIndex != undefined && arrowIndex >= 0){
		 	//下层图标定位
			$('.con-tools>a').children('.icon-tool-1').css('display','block').siblings('.icon-tool-2').css('display','none');
			$('.con-tools>a').eq(arrowIndex).children('.icon-tool-1').css('display','none').siblings('.icon-tool-2').css('display','block');
			//移除轮播按钮初始有效active类
			$('.carousel-indicators>li').eq(0).removeClass("active");
			//轮播按钮定位
			$('.carousel-indicators>li').eq(arrowIndex).addClass("active");
			//移除轮播图片初始有效active类
			$('.banner').eq(0).removeClass("active");
			//轮播图片定位
			$('.banner').eq(arrowIndex).addClass("active");
	 } 
//	 $.ajax({
//			url : zd_basePath + '/getRoleSystems',
//			type : 'POST',
//			dataType : 'json',
//			success : function(data) {
//				var r0 = data.data['运营监测系统'];
//				var r1 = data.data['服务与监管'];
//				var r2 = data.data['应急指挥调度'];
//				var r3 = data.data['公共信息服务系统'];
//				var r4 = data.data['数据资源建设'];
//				var r5 = data.data['辅助决策系统'];
//				var r6 = data.data['平台管理系统'];
//				if (r0 == false) {
//					$("a[href='mainIndex?systemId=1']").attr('href', 'javascript:return false;').attr('style', 'top:280px;left:140px;cursor:not-allowed');
//				}
//				if (r1 == false) {
//					$("a[href='mainIndex?systemId=2']").attr('href', 'javascript:return false;').attr('style', 'top:280px;right:180px;cursor:not-allowed');
//				}
//				if (r2 == false) {
//					$("a[href='mainIndex?systemId=3']").attr('href', 'javascript:return false;').attr('style', 'top:280px;right:180px;cursor:not-allowed');
//				}
//				if (r3 == false) {
//					$("a[href='mainIndex?systemId=4']").attr('href', 'javascript:return false;').attr('style', 'top:280px;left:140px;cursor:not-allowed');
//				}
//				if (r4 == false) {
//					$("a[href='mainIndex?systemId=5']").attr('href', 'javascript:return false;').attr('style', 'top:280px;right:180px;cursor:not-allowed');
//				}
//				if (r5 == false) {
//					$("a[href='mainIndex?systemId=6']").attr('href', 'javascript:return false;').attr('style','top:280px;right:168px;cursor:not-allowed');
//				}
//				if (r6 == false) {
//					$("a[href='mainIndex?systemId=7']").attr('href', 'javascript:return false;').attr('style', 'top:280px;left:140px;cursor:not-allowed');
//				}
//				
//				
//			},
//			error : function() {
//				$.messager.alert('提示', '请求失败!');
//			}
//		});
	 
	 $('#myCarousel').on('slid.bs.carousel', function () {
	        if($(".carousel-indicators li.active").attr("data-slide-to")>6)
	        {
	            $(".con-tools").animate({"top":-140}, 500, function(){});
	        }
	        else{
	            $(".con-tools").animate({"top":0}, 500, function(){});
	        }
	    });

	    $(".tools-arrow-left, .tools-arrow-right").click(function () {

	        if(parseInt($('.con-tools').position().top) >= 0)
	        {
	            $(".con-tools").animate({"top":-140}, 500, function(){});
	        }
	        else{
	            $(".con-tools").animate({"top":0}, 500, function(){});
	        }
	    });
	 
	});