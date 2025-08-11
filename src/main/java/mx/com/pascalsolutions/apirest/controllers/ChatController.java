package mx.com.pascalsolutions.apirest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController

public class ChatController extends BaseController{

	@GetMapping("/chat/helloword")
	public String hello(@RequestParam(value="name", defaultValue="World") String name) {
		return "Hello "+name+"!!";
	}
}
