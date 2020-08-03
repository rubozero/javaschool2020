package com.nearsoft.javaschool.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Url> addUser(@RequestBody Url url) {
         Url responseUrl = urlService.addUrl(url);
         if (responseUrl == null){
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         }
        return new ResponseEntity<>(responseUrl, HttpStatus.OK);
    }

    @GetMapping(value = "/{alias}")
    public ResponseEntity<Object> getUserById(@PathVariable("alias") String alias) throws URISyntaxException {
        String urlToRedirect = urlService.getUrl(alias);
        URI redirectUrl = new URI(urlToRedirect);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

}
