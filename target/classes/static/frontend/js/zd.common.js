(function (window) {
	//统一引用easy ui 扩展方法
	var zd_common = {};
	zd_common = (function (zd_common) {
		return zd_common;
	}
		(zd_common));
	/*!
	 * easyui-datebox 格式化yyyy-mm-dd
	 *
	 * Written by lifengwei<lifw@ehualu.com>
	 * Version 1.0.0
	 *
	 */
	var zd_formatter = function zd_parser(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
	};
	/*!
	 * easyui-datebox 日期解析yyyy-mm-dd
	 *
	 * Written by lifengwei<lifw@ehualu.com>
	 * Version 1.0.0
	 *
	 */
	var zd_parser = function zd_parser(s) {
		if (!s)
			return new Date();
		var ss = (s.split('-'));
		var y = parseInt(ss[0], 10);
		var m = parseInt(ss[1], 10);
		var d = parseInt(ss[2], 10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
			return new Date(y, m - 1, d);
		} else {
			return new Date();
		}
	};
	/*!
	 * easyui-datagrid 格式化千分符
	 */
	var zd_thousands = function zd_thousands(num) {
		if(num == undefined){
			return num;
		}else if(num.toString().indexOf("年")>0 && num.toString().indexOf("月")>0 && num.toString().indexOf("日")>0 ){
			return num;
		} else{
			var str=num.toString();
			var newStr = "";
			var count = 0;
			 
			if(str.indexOf(".")==-1){
			   for(var i=str.length-1;i>=0;i--){
			 if(count % 3 == 0 && count != 0){
			   newStr = str.charAt(i) + "," + newStr;
			 }else{
			   newStr = str.charAt(i) + newStr;
			 }
			 count++;
			   }
			   str = newStr ; //自动补小数点后两位
//			   console.log(str)
			}
			else
			{
			   for(var i = str.indexOf(".")-1;i>=0;i--){
				 if(count % 3 == 0 && count != 0){
				   newStr = str.charAt(i) + "," + newStr;
				 }else{
				   newStr = str.charAt(i) + newStr; //逐个字符相接起来
				 }
				 count++;
			   }
			   if(str.indexOf("%")>0){
					console.log(str);
					str = newStr + (str).substr((str).indexOf("."),3)+"%";
				}else{
					str = newStr + (str).substr((str).indexOf("."),3);
				}
			  
//			   console.log(str)
			 }
			return str;
		}
	};
	/*!
	 * basePath
	 *
	 * Written by lifengwei<lifw@ehualu.com>
	 * Version 1.0.0
	 *
	 */
	var location = (window.location + '').split('/');
	var basePath = location[0] + '//' + location[2] + '/' + location[3];
	var zd_basePath = basePath;
	//主功能区页面跳转加载
	function gotoUrl(url) {
		$('#mycontents').attr('src', zd_basePath + '/' + url);
	};
	/*!
	 * 判断字符串是否为yyyy-mm-dd格式的日期
	 * 参数："yyyy-mm-dd"
	 * Written by lifengwei<lifw@ehualu.com>
	 * Version 1.0.0
	 *
	 */
	var zd_isdate = function IsDate(str, dF) {
		if (dF == "yyyy-mm-dd") {
			if (str.length != 0) {
				var reg = /^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))$/;
				var r = str.match(reg);
				if (r == null) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	};
	/*!
	 * 根据文件名判断Data URI scheme类型
	 * 参数：文件名 例如：'my.png'
	 * Written by lifengwei<lifw@ehualu.com>
	 * Version 1.0.0
	 */
	zd_common = (function (zd_common) {
		var module = function (fileName) {
			var DataUriScheme = '';
			if (fileName.indexOf('.png') != -1 || fileName.indexOf('.PNG') != -1) {
				DataUriScheme = 'data:image/png';
			} else if (fileName.indexOf('.jpeg') != -1 || fileName.indexOf('.JPEG') != -1
				 || fileName.indexOf('.jpg') != -1 || fileName.indexOf('.JPG') != -1) {
				DataUriScheme = 'data:image/jpeg';
			} else if (fileName.indexOf('.gif') != -1 || fileName.indexOf('.GIF') != -1) {
				DataUriScheme = 'data:image/gif';
			} else if (fileName.indexOf('.icon') != -1 || fileName.indexOf('.ICON') != -1) {
				DataUriScheme = 'data:image/x-icon';
			} else if (fileName.indexOf('.css') != -1 || fileName.indexOf('.CSS') != -1) {
				DataUriScheme = 'data:text/css';
			} else if (fileName.indexOf('.js') != -1 || fileName.indexOf('.JS') != -1) {
				DataUriScheme = 'data:text/javascript';
			} else if (fileName.indexOf('.html') != -1 || fileName.indexOf('.HTML') != -1) {
				DataUriScheme = 'data:text/html';
			} else if (fileName.indexOf('.txt') != -1 || fileName.indexOf('.TXT') != -1) {
				DataUriScheme = 'data:text/plain';
			}
			return DataUriScheme;
		};
		zd_common.zd_getDataUriScheme = module;
		return zd_common;
	}
		(zd_common));
	/**/
	/**
	 * 检查输入的一串字符是否包含汉字
	 * 输入:str  字符串
	 * 返回:true 或 flase; true表示包含汉字
	 * Written by linjiafeng<linjf@ehualu.com>
	 * Version 1.0.0
	 */
	zd_common = (function (zd_common) {
		var zd_isChinese = function checkChinese(str) {
			if (escape(str).indexOf("%u") != -1) {
				return true;
			} else {
				return false;
			}
		};
		zd_common.zd_isChinese = zd_isChinese;
		return zd_common;
	}
		(zd_common));
	
	/**/
	/**
	 * 检查输入的邮箱格式是否正确
	 * 输入:str  字符串
	 * 返回:true 或 flase; true表示格式正确
	 *  Written by linjiafeng<linjf@ehualu.com>
	 * Version 1.0.0
	 */
	zd_common = (function (zd_common) {
		var zd_isEmail = function checkEmail(str) {
			if (str.match(/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/) == null) {
				return false;
			}
			else {
				return true;
			}
		};
		zd_common.zd_isEmail = zd_isEmail;
		return zd_common;
	}(zd_common));

	/**//**
	 * 检查输入的手机号码是否正确
	 * 输入:str  字符串
	 * 返回:true 或 flase; true表示格式正确
	 *  Written by linjiafeng<linjf@ehualu.com>
	 * Version 1.0.0
	 */
	zd_common = (function(zd_common){
		var zd_istelePhone = function checkTelephone(str){
			if (!(/^1[3|4|5|7|8]\d{9}$/.test(str))) {
				return false;
			}
			else {
				return true;
			}
		};
		zd_common.zd_istelePhone = zd_istelePhone;
		return zd_common;
	}(zd_common));

	/**//**
	 * 检查QQ的格式是否正确
	 * 输入:str  字符串
	 *  返回:true 或 flase; true表示格式正确
	 *   Written by linjiafeng<linjf@ehualu.com>
	 * Version 1.0.0
	 */
	zd_common = (function(zd_common){
		var zd_isQQ = function checkQQ(str){
			if (str.match(/^\\d{5,10}$/) == null) {
				return false;
			}
			else {
				return true;
			}
		};
		zd_common.zd_isQQ = zd_isQQ;
		return zd_common;
	}(zd_common));

	/**//**
	 * 检查输入的身份证号是否正确
	 * 输入:str  字符串
	 *  返回:true 或 flase; true表示格式正确
	 *  Written by linjiafeng<linjf@ehualu.com>
	 * Version 1.0.0
	 */
	zd_common = (function(zd_common){
		var zd_isCard =	function checkCard(str){
			//15位数身份证正则表达式
			var arg1 = /^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$/;
			//18位数身份证正则表达式
			var arg2 = /^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$/;
			if (str.match(arg1) == null && str.match(arg2) == null) {
				return false;
			}
			else {
				return true;
			}
		};
		zd_common.zd_isCard = zd_isCard;
		return zd_common;
	}(zd_common));
	/**
	 * 判断文件格式是否EXCEL文档格式
	 * 参数：path *.xls、*。xls、*。xlsx、*。Xlsx、*。Xls
	 * Written by ehualu
	 * Version 1.0.0
	 *
	 */
    var zd_isXlsOrXlsx = function IsXlsOrXlsx(fileLx){
	  if (fileLx == "xls" ||fileLx == "xlsx" || fileLx == "Xlsx" || fileLx == "Xls")  {
	      return true;
	  }  else  {
	      return false;
	  }
    };
	/**//**
	 * 检查输入的固定电话/传真是否正确
	 * 输入:str  字符串
	 *  返回:true 或 flase; true表示格式正确
	 *  Written by linjiafeng<linjf@ehualu.com>
	 * Version 1.0.0
	 */
	zd_common = (function(zd_common){
		var zd_isPhone = function checkPhone(str){
			if(!/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test(str)){
				return false;
			}else {
				return true;
			}
		};
		zd_common.zd_isPhone = zd_isPhone;
		return zd_common;
	}(zd_common));

	zd_common.zd_formatter = zd_formatter;
	zd_common.zd_parser = zd_parser;
	zd_common.zd_basePath = basePath;
	zd_common.zd_isdate = zd_isdate;
	zd_common.zd_isXlsOrXlsx = zd_isXlsOrXlsx;
	//旧版将共通方法入口直接暴露到全局中，为了兼容老版代码继续将部分共通方法直接暴露到全局
	window.zd_formatter = zd_formatter;
	window.zd_parser = zd_parser;
	window.zd_basePath = basePath;
	window.zd_isdate = zd_isdate;
	window.zd_thousands = zd_thousands;
	//将zd_common模块声明到全局中
	window.zd_common = zd_common;
})(window);