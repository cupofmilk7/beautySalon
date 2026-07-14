package ru.cupofmilk.model.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "filters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "filter_name",nullable = false)
    private String name;

    @Column(name = "first_date")
    private LocalDateTime firstDate;

    @Column(name = "second_date")
    private LocalDateTime secondDate;
}
