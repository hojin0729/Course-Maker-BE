package coursemaker.coursemaker;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@EnableMethodSecurity
@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy// AOP enable
@EnableAsync // 비동기 메서드 동작
public class CoursemakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoursemakerApplication.class, args);
	}

//	@Bean
//	public ServletWebServerFactory servletContainer() {
//		// Enable SSL Trafic
//		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//			@Override
//			protected void postProcessContext(Context context) {
//				SecurityConstraint securityConstraint = new SecurityConstraint();
//				securityConstraint.setUserConstraint("CONFIDENTIAL");
//				SecurityCollection collection = new SecurityCollection();
//				collection.addPattern("/*");
//				securityConstraint.addCollection(collection);
//				context.addConstraint(securityConstraint);
//			}
//		};
//
//		// Add HTTP to HTTPS redirect
//		tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector());
//
//		return tomcat;
//	}
//
//	/*
//    We need to redirect from HTTP to HTTPS. Without SSL, this application used
//    port 8082. With SSL it will use port 8443. So, any request for 8082 needs to be
//    redirected to HTTPS on 8443.
//     */
//	private Connector httpToHttpsRedirectConnector() {
//		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//		connector.setScheme("http");
//		connector.setPort(8080);
//		connector.setSecure(false);
//		connector.setRedirectPort(443);
//		return connector;
//	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		ConnectionProvider connectionProvider = ConnectionProvider.builder("custom")
				.maxConnections(300)
				.pendingAcquireMaxCount(3000)
				.pendingAcquireTimeout(Duration.ofSeconds(60))
				.maxIdleTime(Duration.ofSeconds(20))
				.maxLifeTime(Duration.ofMinutes(5))
				.build();

		HttpClient httpClient = HttpClient.create(connectionProvider)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000)
				.responseTimeout(Duration.ofMinutes(2))
				.doOnConnected(conn -> conn
						.addHandlerLast(new ReadTimeoutHandler(2, TimeUnit.MINUTES))
						.addHandlerLast(new WriteTimeoutHandler(2, TimeUnit.MINUTES)));

		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.codecs(clientCodecConfigurer -> clientCodecConfigurer
						.defaultCodecs()
						.maxInMemorySize(16 * 1024 * 1024)); // 데이터 버퍼 크기 16MB로 증가
	}

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.build();
	}
}