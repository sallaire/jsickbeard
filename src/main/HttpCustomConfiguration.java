//package org.sallaire.controller.conf;
//
//import java.nio.charset.Charset;
//
//import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
//import org.springframework.context.annotation.*;
//import org.springframework.http.converter.*;
//
//@Configuration
//public class HttpCustomConfiguration {
//
//    @Bean
//    public HttpMessageConverters customConverters() {
//        HttpMessageConverter<?> additional = new StringHttpMessageConverter(Charset.forName("UTF-8"));
//        return new HttpMessageConverters(additional);
//    }
//
//}