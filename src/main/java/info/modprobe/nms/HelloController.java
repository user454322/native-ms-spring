package info.modprobe.nms;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> greeting(@RequestParam(value = "name",
            defaultValue = "World") final String name) {

        return new ResponseEntity<>(format("Hello %s", name), OK);
    }
}
