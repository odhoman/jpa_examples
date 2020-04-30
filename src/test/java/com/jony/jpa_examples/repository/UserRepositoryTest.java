package com.jony.jpa_examples.repository;


import static org.junit.Assert.assertEquals;

import com.jony.jpa_examples.JpaExamplesApplication;
import com.jony.jpa_examples.entity.User;
import java.util.List;
import java.util.stream.StreamSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaExamplesApplication.class)
@SpringBootTest
public class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Test
  public void testFindAllSorted() {

    Iterable<User> users1 = userRepository.findAll(Sort.by("name").descending());
    Iterable<User> users2 = userRepository.findAll(Sort.by("name").ascending());

    validateFirstUserNameAndSize(users1, "zzzzzz", 40L);
    validateFirstUserNameAndSize(users2, "AAAAAA", 40L);
  }

  @Test
  public void testFindAllPageable() {
    Iterable<User> users1 = userRepository.findAll(PageRequest.of(0, 20));
    Iterable<User> users2 = userRepository.findAll(PageRequest.of(1, 20));

    validateFirstUserNameAndSize(users1, "aaaaaa", 20L);
    validateFirstUserNameAndSize(users2, "iiiiii", 20L);
  }

  @Test
  public void testFindAllSortAndPageable() {

    Pageable sortedByAgeDescNameAsc =
        PageRequest.of(0, 5, Sort.by("age").descending().and(Sort.by("name")));

    Iterable<User> users1 = userRepository.findAll(sortedByAgeDescNameAsc);

    validateFirstUserNameAndSize(users1, "zzzzzz", 5L);
  }

  @Test
  public void testQueryMethodsStaticOrdering() {
    List<User> users1 = userRepository.findByNameIgnoreCaseOrderByAge("AAAAAA");
    validateFirstUserNameAndSize(users1, "aaaaaa", 20L);
  }

  @Test
  public void testQueryMethodsDistinct() {
    List<User> users1 = userRepository
        .findUsersDistinctByNameIgnoreCaseOrLastNameIgnoreCase("AAAAAA", "lastname");
    verifySizeExpected(users1, 20L);
  }

  @Test
  public void testQueryMethodsQuantityOperator() {
    List<User> users1 = userRepository
        .findUsersByAgeLessThan(40);
    verifySizeExpected(users1, 39L);
  }

  @Test
  public void testQueryMethodsPropertyExpressions() {
    List<User> users1 = userRepository
        .findByAddressZipCode("9999");
    verifySizeExpected(users1, 1L);
  }

  @Test
  public void testQueryMethodsSpecialParameterHandlingPageable() {
    Page<User> users1 = userRepository
        .findByLastName("aaaaaal",
            PageRequest.of(0, 20, Sort.by("age").descending().and(Sort.by("name"))));

    verifySizeExpected(users1, 20L);

    users1 = userRepository
        .findByLastName("aaaaaal", users1.nextPageable());

    verifySizeExpected(users1, 18L);

    assertEquals(users1.getTotalElements(), 38L);
    assertEquals(users1.getTotalPages(), 2);
    assertEquals(users1.getSort().isSorted(), true);
  }

  private void validateFirstUserNameAndSize(Iterable<User> users, String userName,
      Long sizeExpected) {
    assertEquals(StreamSupport.stream(users.spliterator(), false).findFirst().get().getName()
        , userName);
    verifySizeExpected(users, sizeExpected.longValue());
  }

  private void verifySizeExpected(Iterable<User> users, Long sizeExpected) {
    assertEquals(StreamSupport.stream(users.spliterator(), false).count(),
        sizeExpected.longValue());
  }

}
