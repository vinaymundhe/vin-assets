package com.va.vinassets.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserDetails {
    @Id
    private String email;
    private String phoneNumber;
    private String firstName;
    private String middleName;
    private String lastName;

}
