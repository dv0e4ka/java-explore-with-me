package ru.practicum.user.model;

import lombok.*;

import javax.persistence.*;

//TODO: доделать сущность

@Getter
@Setter
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String email;
}
