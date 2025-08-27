package com.springprojects.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "rating_summaries")
public class RatingSummary {

    @Id
    private UUID carId;

    private double averageRating;

    private int totalRatings;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

}
