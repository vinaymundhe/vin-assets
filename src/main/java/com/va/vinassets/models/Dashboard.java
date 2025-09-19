package com.va.vinassets.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dashboard")
public class Dashboard {

    @Id
    private String userId; // must match UserDetails.id type

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")   // shares PK with UserDetails
    private UserDetails userDetails;

    private Portfolio portfolio;
}
