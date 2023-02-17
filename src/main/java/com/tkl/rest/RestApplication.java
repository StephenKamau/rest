package com.tkl.rest;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.tkl.rest.models.Todo;
import com.tkl.rest.models.TodoResponse;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class RestApplication {

	private static final Logger logger = LoggerFactory.getLogger(RestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	@Bean
	public CommandLineRunner commandLineRunner(RestTemplate restTemplate) {
		return args -> {
			TodoResponse todos = restTemplate.getForObject("https://dummyjson.com/todos", TodoResponse.class);
			logger.info(todos.toString());
			// Flux<TodoResponse> todoResponseFlux = WebClient.create()
			// .get()
			// .uri("https://dummyjson.com/todos")
			// .retrieve()
			// .bodyToFlux(TodoResponse.class);
			// todoResponseFlux.subscribe(response -> logger.info(response.toString()));
			Predicate<Todo> completedTodo = todo -> todo.isCompleted() == true;
			List<Todo> completed = todos.getTodos().stream().filter(completedTodo)
					.collect(Collectors.toList());
			completed.forEach(todo -> logger.info(todo.toString()));
		};
	}

}
