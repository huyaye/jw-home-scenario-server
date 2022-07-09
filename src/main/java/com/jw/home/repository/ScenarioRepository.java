package com.jw.home.repository;

import com.jw.home.domain.Scenario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScenarioRepository extends MongoRepository<Scenario, String> {
    List<Scenario> findByHomeId(String homeId);
}
