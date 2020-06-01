package com.chatwithstranger.demo.websocket.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean
  public Executor asyncThreadPool() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

    taskExecutor.setCorePoolSize(10); // 최초에 생성할 Pool 개수
    taskExecutor.setMaxPoolSize(30); // 몇개까지 Pool을 생성할 것인지
    taskExecutor.setQueueCapacity(10); // Async 처리 시 Queue Size
    // (설정하지 않으면 Integer.MAX이기 때문에 성능에 문제가 발생함)
    taskExecutor.setThreadNamePrefix("AsyncExecutor-"); // Thread 생성 시 가장 앞에 붙는 이름을 뭘로 할 것인지
    taskExecutor.setDaemon(true); // 데몬 쓰레드로 설정
    taskExecutor.initialize();

    return taskExecutor;
  }
}
