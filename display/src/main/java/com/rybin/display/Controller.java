package com.rybin.display;

import com.rybin.display.Status.ResponseVO;
import function.Lg;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/mydbms")
public class Controller {

    @RequestMapping("/index")
    public ResponseVO index(String statement) throws DocumentException, IOException {
        return new ResponseVO(statement, Lg.main(statement));
    }
}
