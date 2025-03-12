package com.learn.block_academy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    private String bio;

    private String profilePictureUrl;

    @ElementCollection
    private List<String> interests;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Certificate> certificates;
}
