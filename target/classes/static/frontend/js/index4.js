//虚拟参数
var screen_para = {
	screen_para1:{//
		title:'车辆分布情况',
		x_data:['第一分公司','第二分公司','第三分公司','第四分公司'],
		card_name1:'车型一',
		card_value1:[320, 302, 301, 334],
		card_name2:'车型二',
		card_value2:[120, 132, 101, 134],
		card_name3:'车型三',
		card_value3:[220, 182, 191, 234],
		card_name4:'车型四',
		card_value4:[150, 212, 201, 154],
		card_name5:'车型五',
		card_value5:[120, 232, 301, 234]
	},
	screen_para2:{//
		title:'本月车辆维修情况',
		x_data:['第一分公司','第二分公司','第三分公司','第四分公司'],
		card_name:'维修车次',
		card_value:[56, 33, 96, 104]
	},
	screen_para3:{//
		title:'本月车辆维护情况',
		x_data:['第一分公司','第二分公司','第三分公司','第四分公司'],
		card_name1:'一级维护',
		card_value1:[320, 302, 301, 334],
		card_name2:'二级维护',
		card_value2:[120, 132, 101, 134],
		card_name3:'高级维护',
		card_value3:[220, 182, 191, 234]
	}
};
//页面加载
$(function(){
	//移除加载弹出框
	$('#loading').remove();

	setHeight();

	getData_echarts();
});

function setHeight(){
	$('#main').height($(window).height()-20);
}


function getData_echarts() {
	var home_chart1 = echarts.init(document.getElementById('home-chart1'));
	home_chart1.setOption ({
		/*title: {
			text: screen_para.screen_para1.title,
			textStyle:{
				fontWeight:'normal',
				fontSize:'14',
				align: 'center',
				width:'100%'
			}
		},*/
		tooltip : {
			trigger: 'axis'
		},
		grid: {
			top: '10%',
			left: '3%',
			right: '4%',
			bottom: '5%',
			containLabel: true
		},
		yAxis:  {
			type: 'value'
		},
		xAxis: {
			type: 'category',
			data: screen_para.screen_para1.x_data
		},
		series: [
			{
				name: screen_para.screen_para1.card_name1,
				type: 'bar',
				stack: '总量',
				data: screen_para.screen_para1.card_value1
			},
			{
				name: screen_para.screen_para1.card_name2,
				type: 'bar',
				stack: '总量',
				data: screen_para.screen_para1.card_value2
			},
			{
				name: screen_para.screen_para1.card_name3,
				type: 'bar',
				stack: '总量',
				data: screen_para.screen_para1.card_value3
			},
			{
				name: screen_para.screen_para1.card_name4,
				type: 'bar',
				stack: '总量',
				data: screen_para.screen_para1.card_value4
			},
			{
				name: screen_para.screen_para1.card_name5,
				type: 'bar',
				stack: '总量',
				data: screen_para.screen_para1.card_value5
			}
		]
	});

	var home_chart2 = echarts.init(document.getElementById('home-chart2'));
	home_chart2.setOption ({
		tooltip : {
			trigger: 'axis'
		},
		grid: {
			left: '3%',
			right: '4%',
			top: '10%',
			bottom:'5%',
			containLabel: true
		},
		xAxis : [
			{
				type : 'value',
				axisLine:{
					lineStyle:{
						color:"#333"
					}
				}
			}
		],
		yAxis : [
			{
				type : 'category',
				data : screen_para.screen_para2.x_data,
				axisLabel:{
					textStyle:{
						fontFamily:"SimHei",
						color:"#333"
					}
				}
			}
		],
		series : [
			{
				name: screen_para.screen_para2.card_name,
				type:'bar',
				stack:1,
				barGap:-1,
				barCategoryGap:'30%',
				zlevel:10,
				data: screen_para.screen_para2.card_value,
				itemStyle: {normal: {
					color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
						offset: 0, color: '#5ac5d8' // 0% 处的颜色
					}, {
						offset: 1, color: '#0367f3' // 100% 处的颜色
					}], false)
				}}
			}
		]
	});


	var home_chart3 = echarts.init(document.getElementById('home-chart3'));
	home_chart3.setOption ({
		tooltip : {
			trigger: 'axis'
		},
		grid: {
			top: '10%',
			left: '3%',
			right: '4%',
			bottom: '5%',
			containLabel: true
		},
		xAxis: [
			{
				type: 'category',
				data: screen_para.screen_para3.x_data,
				axisPointer: {
					type: 'shadow'
				}
			}
		],
		yAxis: [
			{
				type: 'value'
			}
		],
		series: [
			{
				name:screen_para.screen_para3.card_name1,
				type:'bar',
				data:screen_para.screen_para3.card_value1
			},
			{
				name:screen_para.screen_para3.card_name2,
				type:'bar',
				data:screen_para.screen_para3.card_value2
			},
			{
				name:screen_para.screen_para3.card_name3,
				type:'bar',
				data:screen_para.screen_para3.card_value3
			}
		]
	});
}
