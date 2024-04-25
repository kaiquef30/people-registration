package com.kaiquef30.peopleregistration.repositories;

import com.kaiquef30.peopleregistration.domains.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByEmail(String email);

}
