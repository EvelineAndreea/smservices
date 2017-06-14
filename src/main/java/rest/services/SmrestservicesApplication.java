package rest.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import rest.model.ResponseXml;
import sm.SMService;
import sm.utils.xml.XMLDocument;

import javax.websocket.server.PathParam;

@SpringBootApplication
@RestController
@RequestMapping("/sm")
public class SmrestservicesApplication {
    private SMService service;

	public static void main(String[] args) {
		SpringApplication.run(SmrestservicesApplication.class, args);
	}

	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> sayHello(){
	    return new ResponseEntity<>("POST an XML file for solving a specific type of stable matching problem.", HttpStatus.OK);
    }

    @RequestMapping(value = "/{param}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, headers = "Accept=application/xml")
    public ResponseXml getMatching(@PathVariable("param") String param, @RequestBody String document){
        service = new SMService(param);
        return service.manage(param, document);
//        byte[] documentBody = xml.getXml().toString().getBytes();
//
//        return xml.getXml();
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(new MediaType("application", "xml"));
//        header.setContentLength(documentBody.length);
//        return new HttpEntity<>(documentBody, header);
//        return new ResponseEntity<>("A MERS!", HttpStatus.OK);
    }
}
