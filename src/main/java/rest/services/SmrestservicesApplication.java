package rest.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rest.model.ResponseXml;
import rest.model.SolvedXml;
import sm.SMService;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@RestController
@RequestMapping("/sm")
@EnableSwagger2
public class SmrestservicesApplication {

    public static void main(String[] args) {
		SpringApplication.run(SmrestservicesApplication.class, args);
	}

	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> sayHello(){
	    return new ResponseEntity<>("POST an XML file for solving a specific type of stable matching problem.", HttpStatus.OK);
    }

    @RequestMapping(value = "/algorithm/{param}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, headers = "Accept=application/xml")
    public ResponseXml getMatching(@PathVariable("param") String param, @RequestBody String document){
        SMService service = new SMService();
        return service.manage(param, document);
    }
}
