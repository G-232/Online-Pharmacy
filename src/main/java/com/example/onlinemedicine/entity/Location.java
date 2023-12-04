package com.example.onlinemedicine.entity;

import com.example.onlinemedicine.entity.BaseEntity;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "location")
public class Location extends BaseEntity {
    @Column(nullable = false, unique = true)
    private double latitude;
    @Column(nullable = false, unique = true)
    private double longitude;
}
