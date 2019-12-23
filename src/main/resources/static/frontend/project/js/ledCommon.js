$(document).ready(function(){

    // 扩展jquery 添加获取form中值为json格式函数：serializeJson
    (function($){
        $.fn.serializeJson=function(){
            var serializeObj={};
            var array=this.serializeArray();
            $(array).each(function(){
                if(serializeObj[this.name]){
                    if($.isArray(serializeObj[this.name])){
                        serializeObj[this.name].push(this.value);
                    }else{
                        serializeObj[this.name]=[serializeObj[this.name],this.value];
                    }
                }else{
                    serializeObj[this.name]=this.value;
                }
            });
            return serializeObj;
        };
        // 获取form表单数据，如果数据为空，则此key都不会保存
        $.fn.serializeJson2=function(){
            var serializeObj={};
            var array=this.serializeArray();
            $(array).each(function(){
                if(serializeObj[this.name]){
                    if($.isArray(serializeObj[this.name])){
                    	if(this.value != "") {
                    		serializeObj[this.name].push(this.value);
                    	}
                    }else{
                    	if(this.value != "") {
                    		serializeObj[this.name]=[serializeObj[this.name],this.value];
                    	}
                    }
                }else{
                	if(this.value != "") {
                		serializeObj[this.name]=this.value;
                	}
                }
            });
            return serializeObj;
        };
    })(jQuery);

    // 扩展easyui 数字验证正则(在使用textbox,但只允许输入数字的场景下使用,比如 00001)
    $.extend($.fn.textbox.defaults.rules, {
        number: {
            validator: function (value, param) {
                return /^[0-9]*$/.test(value);
            },
            message: "请输入数字"
        },
        // 只能输入固定长度的字符串
        lengthForNum: {
            validator: function (value, param) {
            	return value.length == param;
            },
            message: "请输入16位字符"
        },
        // 验证ip地址
        checkIp: {
        	validator: function (value, param) {
        		var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        		return reg.test(value);
            },
            message: "请输入正确格式ip地址"
        },
    });
    
});

/**
 * 使用json数据填充form
 *
 * @param prefix 要填充数据控件id的前缀
 * @param obj json数据
 * @param notDisableArray 不需要禁用的控件key列表
 */
function fillForm(prefix,obj){
    var key,value;
    for(key in obj){
        var domId = "#"+prefix+"_"+key;
        value = obj[key];
        var className = $(domId).attr('class');
        if(className != undefined) {
            if(className.indexOf('easyui-datetimebox') != -1){
                $(domId).datetimebox("setValue", value);
            }else if(className.indexOf('easyui-datebox') != -1){
                $(domId).datebox("setValue", value);
            }else if(className.indexOf('easyui-numberbox') != -1){
                $(domId).numberbox("setValue", value);
            }else if(className.indexOf('easyui-combobox') != -1){
                $(domId).combobox("setValue", value);
            }else if(className.indexOf('easyui-textbox') != -1){
                $(domId).textbox("setValue", value);
            }else if(className.indexOf('easyui-timespinner') != -1){
                $(domId).timespinner("setValue", value);
            }
        }
        if("show" == prefix){
    		$(domId).textbox({
    			disabled: true
    		});
        }
    }
}

/**
 * 验数组是否从1开始的连续数组
 */
function valiList(arr) {
	var resBiaoshi = true;
	$.each(arr, function(i, obj) {
		if(arr[i] != parseInt(i+1)) {
			resBiaoshi = false;
		}
	})
	for(var i=0;i<arr.length;i++){
		if (arr[i]==arr[i+1]){
			return false;
		}
	}
	return resBiaoshi;
}

function copyData(data) {
	return JSON.parse(JSON.stringify(data));
}

/**
 * 数组排序，从小到大
 */
function sortXh(a, b) {
	return parseInt(a.xh) - parseInt(b.xh);
}

function getSjc() {
	return new Date().getTime();
}

/**
 * 前台分页 loadFilter方法调用
 */
function pagerFilter(data){
    if (typeof data.length == 'number' && typeof data.splice == 'function'){    // 判断数据是否是数组
        data = {
            total: data.length,
            rows: data
        }
    }
    var dg = $(this);
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;
            pager.pagination('refresh',{
                pageNumber:pageNum,
                pageSize:pageSize
            });
            dg.datagrid('loadData',data);
        }
    });
    if(typeof(data.rows) != "undefined") {
    	if (!data.originalRows){
    		data.originalRows = (data.rows);
    	}
    	var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
    	var end = start + parseInt(opts.pageSize);
    	data.rows = (data.originalRows.slice(start, end));
    } else {
    	data.originalRow = {};
    	data.rows = {};
    }
    return data;
}