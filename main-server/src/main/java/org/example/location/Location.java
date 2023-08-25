package org.example.location;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "location")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "lat")
    float lat;

    @Column(name = "lon")
    float lon;

}