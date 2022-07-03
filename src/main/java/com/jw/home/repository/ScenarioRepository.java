package com.jw.home.repository;

import com.jw.home.domain.Scenario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScenarioRepository extends MongoRepository<Scenario, String> {
}
