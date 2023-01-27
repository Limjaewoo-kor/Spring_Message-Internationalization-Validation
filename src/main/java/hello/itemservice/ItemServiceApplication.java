package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	//	//스프링 메시시 소스 직접등록 방법 - 인터페이스 MessageSource의 구현체인 ResourceBundleMessageSource를 스프링 빈으로 등록하면된다.
//	// 하지만 스프링 부트를 사용하면 직접등록할 필요없이 자동으로 스프링 빈으로 등록해준다. application.properties 확인
//	@Bean
//	public MessageSource messageSource() {
//		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//		messageSource.setBasenames("messages", "errors");  //resources 밑에 있는 messages.properties파일을 읽는다.
//		messageSource.setDefaultEncoding("utf-8");
//		return messageSource;
//	}

}
