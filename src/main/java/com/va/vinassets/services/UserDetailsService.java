package com.va.vinassets.services;

import com.va.vinassets.dao.UserDetailsRepository;
import com.va.vinassets.models.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public String addUserDetails(String email, String phoneNumber, String firstName, String middleName, String lastName) {
        UserDetails existingUser = userDetailsRepository.findByEmail(email);

        if (existingUser == null) {
            UserDetails newUser = new UserDetails();
            newUser.setEmail(email);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setFirstName(firstName);
            newUser.setMiddleName(middleName);
            newUser.setLastName(lastName);

            userDetailsRepository.save(newUser);
            return "New user details added!";
        } else {
            return "Failed! User with this email already exists.";
        }
    }

    public List<UserDetails> getUserDetails(){
        return userDetailsRepository.findAll();
    }
}