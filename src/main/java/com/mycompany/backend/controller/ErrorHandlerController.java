package com.mycompany.backend.controller;



import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class ErrorHandlerController implements ErrorController {
    // Spring Boot는 404 에러일 경우, /error로 포워딩
  @RequestMapping("/error")
  public ResponseEntity<String> error(HttpServletResponse response) {
    int status = response.getStatus(); //response의 객체를 통해 상태코드를 얻을 수 있는데 
    if(status == 404) { //404일 경우(엉뚱한 url을 입력했을경우)  .ok는 200, .status는 
      return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY) //HttpStatus.MOVED_PERMANETLY=301
          .location(URI.create("/"))
          .body("");
    } else {
      return ResponseEntity.status(status).body(""); //404가 아닌 경우 그대로 frontend로 전달
    }
  }
}
