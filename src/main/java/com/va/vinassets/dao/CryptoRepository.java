package com.va.vinassets.dao;

import com.va.vinassets.models.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoRepository extends JpaRepository<Crypto,Long> {
}
