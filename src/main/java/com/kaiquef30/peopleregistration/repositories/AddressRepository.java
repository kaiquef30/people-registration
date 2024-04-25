package com.kaiquef30.peopleregistration.repositories;

import com.kaiquef30.peopleregistration.domains.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
