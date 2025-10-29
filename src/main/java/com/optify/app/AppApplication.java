package com.optify.app;

import com.optify.views.ConsoleView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {

		SpringApplication.run(AppApplication.class, args);
		ConsoleView console = new ConsoleView();
		console.showConsole();

	}

}
