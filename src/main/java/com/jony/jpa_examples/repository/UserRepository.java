package com.jony.jpa_examples.repository;

import com.jony.jpa_examples.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

  List<User> findByNameIgnoreCaseOrderByAge(String name);

  List<User> findUsersDistinctByNameIgnoreCaseOrLastNameIgnoreCase(String name, String lastName);

  List<User> findUsersByAgeLessThan(Integer age);

  List<User> findByAddressZipCode(String zipCode);

  Page<User> findByLastName(String lastName, Pageable pageable);

}
