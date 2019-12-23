package com.framework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.framework.util.SystemUtil;
import com.zd.filter.SSOAuth;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@MapperScan("com.framework.webClient.dao")
public class Application {
	//private Logger log = LoggerFactory.getLogger(this.getClass());
	public static void main(String[] args) {
		// SpringApplication.run(Application.class, args);
		String pro = SystemUtil.initSystem();
		new SpringApplicationBuilder(Application.class).properties(pro).run(args);
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
	//	templateResolver.addTemplateAlias("loading", "/templates/loading");
		//templateResolver.addTemplateAlias("textCss_div", "/templates/ledtemplateTextCss");
		//templateResolver.addTemplateAlias("imageCss_div", "/templates/ledtemplateImageCss");
		//templateResolver.addTemplateAlias("videoCss_div", "/templates/ledtemplateVideoCss");
    }
	
	@Value("${sso.ssoService}")
	private String ssoService;
	@Value("${sso.ssoLogin}")
	private String ssoLogin;
	@Value("${sso.ssoCookieName}")
	private String ssoCookieName;
	@Value("${sso.ssoAwayName}")
	private String ssoAwayName;
	@Value("${sso.ssoUrlPatterns}")
	private String ssoUrlPatterns;
	
	/**
	 * 单点登录拦截器
	 * @return  
	 */
	@Bean
	public FilterRegistrationBean ssoFilter() {
		
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setName("SSOAuth");
		registration.setFilter(new SSOAuth());
		registration.addInitParameter("SSOService", ssoService);
		registration.addInitParameter("SSOLogin", ssoLogin);
		registration.addInitParameter("cookieName", ssoCookieName);
		registration.addInitParameter("awayName", ssoAwayName);
		registration.addUrlPatterns(ssoUrlPatterns);
		registration.addUrlPatterns("/logout");
		registration.setOrder(1);
		return registration;
	}
}
