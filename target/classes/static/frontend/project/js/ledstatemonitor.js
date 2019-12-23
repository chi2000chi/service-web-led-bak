// 全局数据存储
var mapData;
var left_data1;
var left_data2;
// 定义色块显示状态 默认为true
var green_btn = true;
var red_btn = true;
var black_btn = true;
// 初始化地图和聚合参数，用来加载页面和聚合物
var map = new BMap.Map("container");
map.centerAndZoom(new BMap.Point(126.541, 45.808), 13);
map.enableScrollWheelZoom();
var markerClusterer;
// 页面加载
$(function() {
	$("#info-panel").hide();
	// 移除加载弹出框
	$('#loading').remove();

	searchLed("", "");

	$(".icon-check").bind("click", function() {
		$(this).toggleClass("on");
		if ($(this).hasClass("on")) {
			$(this).next().val(1)
		} else {
			$(this).next().val(0)
		}
	});

	$(".btn-arrow").bind("click", function() {
		$("#right-panel").toggleClass("on");
		$(this).toggleClass("on");
		$("#video-panel").toggleClass("on");
		$("#bottom-panel").toggleClass("on");
	});

	$("#right-panel-over").niceScroll({
		cursorborder : "",
		cursorcolor : "#666",
		boxzoom : false
	}); // First scrollable DIV
	$("#left-panel3-table").niceScroll({
		cursorborder : "",
		cursorcolor : "#1d4380",
		boxzoom : false
	}); // First scrollable DIV

	comboboxInit();
});

/**
 * 定时任务，每分钟刷新一次页面上的点
 */
setInterval(function() {clock()},1000 * 60);

/**
 * 定时任务调用方法
 * 
 * @returns
 */
function clock() {
	// 获取选中的线路和站点信息
	xlbh = $("#search_xl").combobox("getValue");
	zdbh = $("#search_zd").combobox("getValue");
	// 将线路和站点信息发送到后台
	searchLed(xlbh, zdbh);
	// 清除页面上的点
	markerClusterer.clearMarkers();
}

/**
 * 蓝色按钮
 * 
 * @returns
 */
$(".blue").bind('click', function() {
	// 清除页面上的点
	markerClusterer.clearMarkers();
	// 设置色块属性显示
	green_btn = true;
	red_btn = true;
	black_btn = true;
	// 创建地图上的点
	createMap();
});

/**
 * 绿色按钮
 * 
 * @returns
 */
$(".green").bind('click', function() {
	// 清除页面上的点
	markerClusterer.clearMarkers();
	// 设置色块属性显示
	green_btn = true;
	red_btn = false;
	black_btn = false;
	// 创建地图上的点
	createMap();
});

/**
 * 黑色按钮
 * 
 * @returns
 */
$(".black").bind('click', function() {
	// 清除页面上的点
	markerClusterer.clearMarkers();
	// 设置色块属性显示
	green_btn = false;
	red_btn = false;
	black_btn = true;
	// 创建地图上的点
	createMap();
});

/**
 * 红色按钮
 * 
 * @returns
 */
$(".red").bind('click', function() {
	// 清除页面上的点
	markerClusterer.clearMarkers();
	// 设置色块属性显示
	green_btn = false;
	red_btn = true;
	black_btn = false;
	// 创建地图上的点
	createMap();
});

/**
 * 定义百度地图，绘制坐标点
 * 
 * @returns
 */

function createMap() {
	// 点集合
	var markers = [];
	var pt = null;
	$.each(mapData.jwd, function(i, obj) {
		// 循环电子站牌放置经纬度和消息
		pt = new BMap.Point(obj.jd, obj.wd);
		// 标注点颜色
		var myIcon;
		if (obj.color == 1 && green_btn == true) {
			myIcon = new BMap.Icon("/frontend/project/images/marker_green_sprite.png", new BMap.Size(15, 29));
			// 标注点点击事件
			var marker = new BMap.Marker(pt, {icon : myIcon});
			marker.addEventListener("click", function(e) {
				createPointInfo(marker.getPosition().lng, marker.getPosition().lat);
			});
			markers.push(marker);
		} else if (obj.color == 0 && black_btn == true) {
			myIcon = new BMap.Icon("/frontend/project/images/marker_black_sprite.png", new BMap.Size(15, 29));
			// 标注点点击事件
			var marker = new BMap.Marker(pt, {icon : myIcon});
			marker.addEventListener("click", function(e) {
				createPointInfo(marker.getPosition().lng, marker.getPosition().lat);
			});
			markers.push(marker);
		} else if (obj.color == 2 && red_btn == true) {
			myIcon = new BMap.Icon( "/frontend/project/images/marker_red_sprite.png", new BMap.Size(15, 29));
			// 标注点点击事件
			var marker = new BMap.Marker(pt, {icon : myIcon});
			marker.addEventListener("click", function(e) {
				createPointInfo(marker.getPosition().lng, marker.getPosition().lat);
			});
			markers.push(marker);
		}
	});

	// 最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
	markerClusterer = new BMapLib.MarkerClusterer(map, {markers : markers});
}

/**
 * 点击站点详情展示
 * 
 * @param lng 经度
 * @param lat 纬度
 * @returns
 */
function createPointInfo(lng, lat) {
	var info_data = {};
	$.each(mapData.key, function(i, datas) {
		// 获取选中站点的经纬度
		if (parseFloat(datas.jd) == parseFloat(lng)	&& parseFloat(datas.wd) == parseFloat(lat)) {
			// 电子站牌名称
			info_data.dzzpmc = datas.ledmc;
			// 电子站牌编号
			info_data.dzzpbh = datas.ledbh;
			// 经度
			info_data.jd = datas.jd;
			// 纬度
			info_data.wd = datas.wd;
			// 状态
			if (datas.sbzt == "正常") {
				info_data.zt = datas.zxzt + '/正常';
			} else {
				info_data.zt = datas.zxzt + '/故障';
			}
			// 判断是否存在故障类型
			if (datas.ycStateMap != undefined && datas.ycStateMap != null && datas.ycStateMap != "") {
				var gzlxList = [];
				$.each(datas.ycStateMap, function(i, val) {
					if (val == 'zdcgqcjz') {
						gzlxList.push("震动传感器采集值");
					}
					if (val == 'mjcgq') {
						gzlxList.push("门禁传感器");
					}
					if (i == 'swcgq') {
						gzlxList.push("水位传感器");
					}
					if (i == 'jwmkzt') {
						gzlxList.push("加温模块状态");
					}
					if (i == 'yjkqzt') {
						gzlxList.push("液晶开启状态");
					}
					if (i == 'ledkqzt') {
						gzlxList.push("LED开启状态");
					}
					if (i == 'fskqzt') {
						gzlxList.push("风扇开启状态");
					}
					if (i == 'zmkqzt') {
						gzlxList.push("照明开启状态");
					}
					if (i == 'dvrkqzt') {
						gzlxList.push("DVR开启状态");
					}
					if (i == 'glyqkqzt') {
						gzlxList.push("4G路由器开启状态");
					}
				})
				info_data.gzlx = gzlxList.join(";</br>");
			} else {
				info_data.gzlx = "无";
			}
			// 关联站点
			var tableData = [];
			$.each(datas.stationList, function(i, val) {
				var maps = {};
				maps.line = val.xlmc;
				maps.station = val.zdmc;
				if (val.fx == "0") {
					maps.dir = "上行";
				} else {
					maps.dir = "下行";
				}
				tableData.push(maps);
			})
			info_data.tableData = tableData;
		}
	});
	// 打开标注点的dialog
	$("#info-panel").show();
	// 移除标注点详情信息中关联站点，防止出现消息堆叠
	$("#right-site-table tr td").remove();
	// 加载数据
	show_rightInfo('info-panel', info_data);
}

/**
 * 查询按钮
 * 
 * @returns
 */
$("#search").bind('click', function() {
	// 获取选中的线路和站点信息
	xlbh = $("#search_xl").combobox("getValue");
	zdbh = $("#search_zd").combobox("getValue");
	// 将线路和站点信息发送到后台
	searchLed(xlbh, zdbh);
	// 清除页面上的点
	markerClusterer.clearMarkers();
});

/**
 * 查询电子站牌信息
 * 
 * @param map xlbh:线路编号；zdbh:站点编号
 * @returns
 */
function searchLed(xlbh, zdbh) {
	var map = {};
	map.xlbh = xlbh;
	map.zdbh = zdbh;
	$.post("/selectLedStateMonitor", map, function(data) {
		mapData = data;
		// 设置报表数据展示电子站牌总数
		$("#left-panel3-num1").text(mapData.ledAllNum);
		// 设置报表数据展示在线电子站牌数
		$("#left-panel3-num2").text(
				mapData.ledZxzcNum + mapData.ledZxycNum + mapData.ledLxycNum);
		// 设置报表数据展示在线电子站牌数
		$("#left-panel4-num1").text(
				mapData.ledZxzcNum + mapData.ledZxycNum + mapData.ledLxycNum);
		// 设置报表数据展示在线故障数
		$("#left-panel4-num2").text(mapData.ledZxycNum);
		// 饼状图数据
		var left_data1 = {
			title : '在线率统计',
			subTitle : mapData.dateNowStr,
			name1 : '在线',
			value1 : mapData.ledZxzcNum + mapData.ledZxycNum + mapData.ledLxycNum,
			name2 : '离线',
			value2 : mapData.ledLxzcNum
		};
		var left_data2 = {
			title : '故障率统计',
			subTitle : mapData.dateNowStr,
			name1 : '可用',
			value1 : mapData.ledZxzcNum + mapData.ledZxycNum + mapData.ledLxycNum,
			name2 : '故障',
			value2 : mapData.ledZxycNum
		};
		// 设置饼状图数据
		set_leftChart_pie(left_panel3_chart, left_data1);
		set_leftChart_pie(left_panel4_chart, left_data2);
		// 设置地图上的点信息
		createMap();
	})
}

/**
 * 线路下拉列表和站点关联数据加载
 * 
 * @returns
 */
function comboboxInit() {
	// 线路需要加站点关联
	// 线路下拉框数据加载
	$('#search_xl').combobox({
		url : '/selectXlxx?tag=1',
		valueField : 'xlmc',
		textField : 'lpmc',
//		onLoadSuccess : function() {
//			// 加载完成后,设置选中第一项
//			var val = $(this).combobox("getData");
//			for ( var item in val[0]) {
//				if (item == "xlmc") {
//					$(this).combobox("select", val[0][item]);
//				}
//			}
//		},
		onChange : function(value) {
			if (value == null || value == "" || value == undefined) {
				// 清空选中项
				$('#search_zd').combobox('clear');
				// 清空option选项
				$('#search_zd').combobox('loadData', {});
				return false;
			}
			// 站点下拉框数据加载
			$('#search_zd').combobox({
				url : '/selectZdxx?tag=1&xlmc=' + value,
				valueField : 'zdid',
				textField : 'zdmc',
				onLoadSuccess : function() {
					// 加载完成后,设置选中第一项
					var val = $(this).combobox("getData");
					for ( var item in val[0]) {
						if (item == "zdid") {
							$(this).combobox("select", val[0][item]);
						}
					}
				}
			});
		}
	});
}

/**
 * 重置按钮
 * 
 * @returns
 */
$("#reset").bind('click', function() {
	$("#search_xl").combobox("select", "");
	// 清空选中项
	$('#search_zd').combobox('clear');
	// 清空option选项
	$('#search_zd').combobox('loadData', {});
	// 将线路和站点信息置空
	xlbh = "";
	zdbh = "";
	// 将线路和站点信息发送到后台
	searchLed(xlbh, zdbh);
	// 清除页面上的点
	markerClusterer.clearMarkers();
	green_btn = true;
	red_btn = true;
	black_btn = true;
	comboboxInit();
});

/**
 * 电子站牌详情关闭操作
 * 
 * @returns
 */
$(".icon-close").bind('click', function() {
	var closeId = $(this).parent().attr("ID");
	$("#" + closeId).hide();
});

var left_panel3_chart = echarts.init(document.getElementById('left-panel3-chart'));
var left_panel4_chart = echarts.init(document.getElementById('left-panel4-chart'));

function show_rightInfo(obj, data, x, y) {
	var _html = '';
	var _this = $("#" + obj);
	x = x || '60%';
	y = y || '27%';
	_this.css("left", x);
	_this.css("top", y);
	var $l = _this.find("#right-info-table");
	var $t = _this.find("#right-site-table");
	$l.find("#dzzpmc").html(data.dzzpmc);
	$l.find("#dzzpbh").html(data.dzzpbh);
	$l.find("#jd").html(data.jd);
	$l.find("#wd").html(data.wd);
	$l.find("#zt").html(data.zt);
	$l.find("#gzlx").html(data.gzlx);
	$.each(data.tableData, function(i, n) {
		_html += '<tr><td style="width:30px;">' + (i + 1) + '</td><td>'
				+ n.line + '</td><td style="width:50%;">' + n.station
				+ '</td><td style="width:20%;">' + n.dir + '</td></tr>';
	});
	$t.append(_html);
}

function set_leftChart_pie(obj, data) {
	obj.setOption({
		title : {
			top : 0,
			left : 0,
			text : data.title,
			subtext : data.subTitle,
			textStyle : {
				color : '#000',
				fontSize : '12'
			}
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{c} ({d}%)"
		},
		series : [ {
			name : data.title,
			type : 'pie',
			radius : [ '50%', '75%' ],
			center : [ '50%', '56%' ],
			avoidLabelOverlap : false,
			label : {
				normal : {
					show : false,
					position : 'center'
				},
				emphasis : {
					show : true,
					textStyle : {
						fontSize : '12',
						fontWeight : 'bold',
						color : '#000'
					}
				}
			},
			labelstation : {
				normal : {
					show : false
				}
			},
			data : [ {
				value : data.value1,
				name : data.name1,
				itemStyle : {
					normal : {
						color : '#1d4380'
					}
				}
			}, {
				value : data.value2,
				name : data.name2,
				itemStyle : {
					normal : {
						color : '#03d8f3'
					}
				}
			}

			]
		} ]
	});
}