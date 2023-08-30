package org.example.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
//    Location findByLatAndLon(float lat, float lon);

    List<Location> findByLatAndLon(float lat, float lon);
}
