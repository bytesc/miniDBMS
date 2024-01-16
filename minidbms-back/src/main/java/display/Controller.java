package display;

import display.Status.ResponseVO;
import function.Lg;
import function.Use_Db;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/mydbms")
public class Controller {

    @Autowired
    Lg login;

    @RequestMapping("/index")
    public ResponseVO index(String statement) throws DocumentException, IOException {

        ResponseVO responseVO = new ResponseVO(statement, login.work(statement));

        return responseVO;
    }

    @RequestMapping("/dbname")
    public ResponseVO getDbname() {
        return new ResponseVO("返回数据库名", login.lookDbname());
    }

    @Bean
    public Lg getLogin() {
        return new Lg();
    }
}
