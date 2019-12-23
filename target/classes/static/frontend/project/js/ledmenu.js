$(function(){
	$("iframe").prop('frameborder', '0');
	loadMenu();
})

/**
 * 加载菜单数据
 * @returns
 */
function loadMenu(){
	$.get('/getSystemMenuTrees',function(data){
		if (data.userName != null && data.userName.length > 0) {
			$("#loginName").text("您好，" + data.userName);
		}
		if (data.dataList != null && data.dataList.length > 0) {
			menuView(data.dataList);
		} else {
			return false;
		}
	})
}

/**
 * 
 * @param 需要创建菜单的数据
 * @returns
 */
function menuView(data) {
	var ul = $("<ul>");
	$.each(data, function(i, obj) {
		var chooseSeq = i + 1;
		$.each(data, function(j, obj1) {
			if (obj1.levelInfo == '1' && obj1.seq == chooseSeq) {
				var li = $("<li>");
				var i = $("<img>");
				var ul2 = $("<ul>");
				var li2 = $("<li>");
				var p = $("<p>");
				var a = $("<a>");
				var a2 = $("<a>");
				a.prop("href", obj1.urlPath).prop('target',"iframe_a").attr('style','color:white;').text(obj1.name);
				p.append(a);
				li2.append(p);
				ul2.append(li2);
				// 控制图标的显示，页面可能出现碎图，原因因为历史菜单中可能没有图标数据，建议给默认图标
				if (obj1.tb == undefined || obj1.tb == null) {
					i.prop('src', '').attr('style','width:10px;height:10px;');
				} else {
					i.prop('src', obj1.tb).attr('style','width:10px;height:10px;');
				} 
				a2.prop("href", obj1.urlPath).prop('target',"iframe_a").attr('style','color:white;');
				a2.append(i);
				li.prop('title', obj1.name).append(a2).append(ul2);
				ul.append(li);
			}
		});
	});
	$("#leftSide").append(ul);
}
