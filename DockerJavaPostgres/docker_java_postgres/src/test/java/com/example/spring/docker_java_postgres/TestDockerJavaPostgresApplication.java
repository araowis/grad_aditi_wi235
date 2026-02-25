package com.example.spring.docker_java_postgres;

import org.springframework.boot.SpringApplication;

public class TestDockerJavaPostgresApplication {

	public static void main(String[] args) {
		SpringApplication.from(DockerJavaPostgresApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
