package com.va.vinassets.controllers;

import com.va.vinassets.models.UserDetails;
import com.va.vinassets.services.UserDetailsService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userdetails")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/add")
    public ResponseEntity<String> addUserDetails(@RequestParam String email,
                                                 @RequestParam String phoneNumber,
                                                 @RequestParam String firstName,
                                                 @RequestParam String middleName,
                                                 @RequestParam String lastName){
        String result = userDetailsService.addUserDetails(email, phoneNumber, firstName, middleName, lastName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDetails>> getUserDetails(){
        List<UserDetails> result = userDetailsService.getUserDetails();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
