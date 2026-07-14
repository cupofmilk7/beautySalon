package ru.cupofmilk.model.user;

import jakarta.persistence.*;
import lombok.Data;
import tech.ailef.snapadmin.external.annotations.Filterable;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    @Filterable
    private Long id;

    @Column(nullable = false)
    @Filterable
    private String name;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String number;
}