package com.jony.jpa_examples.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "LASTNAME")
  private String lastName;

  @Column(name = "AGE")
  private Integer age;

  @OneToOne
  @JoinColumn(name = "ADDRESSID")
  private Address address;


}
