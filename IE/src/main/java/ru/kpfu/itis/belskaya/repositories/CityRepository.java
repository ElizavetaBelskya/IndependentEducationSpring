package ru.kpfu.itis.belskaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.belskaya.models.City;

/**
 * @author Elizaveta Belskaya
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
