package rest.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rest.SMService;
import rest.model.ResponseXml;
import rest.model.SolvedXml;


@Controller
@RequestMapping("/sm")
public class SmServiceController {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> smDocumentation(){
        log.info("New request on documentation page.");
        return new ResponseEntity<>("POST an XML file for solving a specific type of stable matching problem.", HttpStatus.OK);
    }

    @RequestMapping(value = "/algorithm/{param}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE,
            headers = "Accept=application/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getMatching(@PathVariable("param") String param, @RequestBody String document){
        log.info("New request on /algorithm/" + param);

        SMService service = new SMService();
        ResponseXml xml = service.manage(param, document);

        if (xml instanceof SolvedXml)
            return new ResponseEntity<>(xml, HttpStatus.OK);
        else return new ResponseEntity<>(xml, HttpStatus.BAD_REQUEST);
    }
}




