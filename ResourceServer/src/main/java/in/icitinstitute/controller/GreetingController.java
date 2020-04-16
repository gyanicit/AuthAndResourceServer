package in.icitinstitute.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	@PostMapping("/hello")
	public String sayHello(@RequestBody String name) {
		return "hello "+name;
	}
}
