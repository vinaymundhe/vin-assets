package com.va.vinassets.services;

import com.va.vinassets.dao.UserDetailsRepository;
import com.va.vinassets.models.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public String addUserDetails(String email, String phoneNumber, String firstName, String middleName, String lastName) {
        UserDetails existingUser = userDetailsRepository.findByEmail(email);

        if (existingUser != null) {
            existingUser.setEmail(email);
            existingUser.setPhoneNumber(phoneNumber);
            existingUser.setFirstName(firstName);
            existingUser.setMiddleName(middleName);
            existingUser.setLastName(lastName);

            userDetailsRepository.save(existingUser);
        }
        return "New user details added!";
    }
}