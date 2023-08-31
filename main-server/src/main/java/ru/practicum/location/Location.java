package ru.practicum.location;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "locations")
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

    public Location(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }
}