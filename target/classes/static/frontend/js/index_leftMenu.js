// 获取菜单
(function($, window, document, undefined) {
	var pluginName = "leftSideMenu";
	var defaults = {
		speed: 300,
		showDelay: 0,
		hideDelay: 0,
		singleOpen: true,
		clickEffect: true
	};
	function Plugin(element, options) {
		this.element = element;
		this.settings = $.extend({},
			defaults, options);
		this._defaults = defaults;
		this._name = pluginName;
		this.init();
	};
	$.extend(Plugin.prototype, {
		init: function() {
			this.openSubmenu();
			this.submenuIndicators();
			if (defaults.clickEffect) {
				this.addClickEffect();
			}
		},
		openSubmenu: function() {
			$(this.element).children("ul").find("li").bind("click touchstart",
				function(e) {
					e.stopPropagation();
					e.preventDefault();
					$("#leftSide-menu li, #leftSide-menu li a").removeClass("active");
					$(this).addClass("active");
					$(this).children("a").eq(0).addClass("active");
					//$("#leftSide-menu .active>a").addClass("active");
					if($(this).parent().hasClass("submenu")){
						$(this).parent().prev("a").addClass("active");
						$(this).parent().prev("a").parent("li").addClass("active");
					}
					if($(this).parent().prev("a").parent("li").parent("ul").hasClass("submenu")){
						$(this).parent().prev("a").parent("li").parent("ul").prev("a").addClass("active");
						$(this).parent().prev("a").parent("li").parent("ul").prev("a").parent("li").addClass("active");
					}
					
					if ($(this).children(".submenu").length > 0) {
						if ($(this).children(".submenu").css("display") == "none") {
							$(this).children(".submenu").delay(defaults.showDelay).slideDown(defaults.speed);
							$(this).children(".submenu").siblings("a").addClass("submenu-indicator-minus");
							//$(this).addClass("active");
							
							$(".submenu").removeClass("submenu-on");
							$(this).children(".submenu").addClass("submenu-on");
							
							if (defaults.singleOpen) {
								$(this).siblings().children(".submenu").slideUp(defaults.speed);
								$(this).siblings().children(".submenu").siblings("a").removeClass("submenu-indicator-minus");
							}
							return false;
						} else {
							$(this).children(".submenu").delay(defaults.hideDelay).slideUp(defaults.speed);

						}
						if ($(this).children(".submenu").siblings("a").hasClass("submenu-indicator-minus")) {
							$(this).children(".submenu").siblings("a").removeClass("submenu-indicator-minus");
							$(this).removeClass("active");

						}
					}
				});
		},
		submenuIndicators: function() {
			if ($(this.element).find(".submenu").length > 0) {
				$(this.element).find(".submenu").siblings("a").append("<span class='submenu-indicator'>+</span>");
			}
		},
		addClickEffect: function() {
			var ink, d, x, y;
			$(this.element).find("a").bind("click touchstart",
				function(e) {		
				
					//添加水纹动效
					$(".ink").remove();
					if ($(this).children(".ink").length === 0) {
						$(this).prepend("<span class='ink'></span>");
					}
					ink = $(this).find(".ink");
					ink.removeClass("animate-ink");
					if (!ink.height() && !ink.width()) {
						d = Math.max($(this).outerWidth(), $(this).outerHeight());
						ink.css({
							height: d,
							width: d
						});
					}
					x = e.pageX - $(this).offset().left - ink.width() / 2;
					y = e.pageY - $(this).offset().top - ink.height() / 2;
					ink.css({
						top: y + 'px',
						left: x + 'px'
					}).addClass("animate-ink");
				});
		}
	});
	$.fn[pluginName] = function(options) {
		this.each(function() {
			if (!$.data(this, "plugin_" + pluginName)) {
				$.data(this, "plugin_" + pluginName, new Plugin(this, options));
			}
		});
		return this;
	};
})(jQuery, window, document);

function showLeft() {
    $("#center-body").toggleClass("showLeftMenu");
}



$(function () {
	var systemId = $('#systemId').val(); //系统ID
	var arrowIndex = $('#arrowIndex').val(); //轮播序列号
	jQuery("#leftSide-menu").leftSideMenu();
});

function leftMenuUrl(url){
	var location = (window.location + '').split('/');
	var basePath = location[0] + '//' + location[2] + '/' + location[3];

	if(url != "undefined"&&url != undefined){
		if(url.indexOf("http")>-1){
			$('#mycontents').attr('src',url);
		}else{
			$('#mycontents').attr('src',basePath+'/'+url);
		}
	}
}