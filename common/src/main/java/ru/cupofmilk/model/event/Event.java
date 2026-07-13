package ru.cupofmilk.model.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "first_date")
    private LocalDateTime firstDate;

    @Column(name = "second_date")
    private LocalDateTime secondDate;

    @Column(name = "message_date")
    private LocalDateTime messageDate;
}
