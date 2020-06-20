package curso.springboot.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // Vai ser um controller de mvc. Vai interceptar requisições de uma url
public class IndexController {
	
	@RequestMapping(value = "/")
	private String index() {
		return "index";
	}
}
