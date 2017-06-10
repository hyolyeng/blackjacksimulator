package blackjack

import org.slf4j.LoggerFactory
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
class Application {

	private val log = LoggerFactory.getLogger(Application::class.java)

	@RequestMapping("/")
	fun home() : String {
		return "Hello World!"
	}
}

fun main(args: Array<String>) {
	SpringApplication.run(Application::class.java, *args)
}
