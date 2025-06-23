package com.va.vinassets.dao;

import com.va.vinassets.models.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, String> {
    UserDetails findByEmail(String email);
}
