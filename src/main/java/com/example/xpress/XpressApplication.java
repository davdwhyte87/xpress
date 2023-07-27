package com.example.xpress;

import com.example.xpress.models.User;
import com.example.xpress.utils.UserAuthInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class XpressApplication {

	public static void main(String[] args) {
		SpringApplication.run(XpressApplication.class, args);
	}


	@Configuration
	public class AppConfig implements WebMvcConfigurer {
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(new UserAuthInterceptor()).addPathPatterns("/airtime/*");
		}
	}
}




