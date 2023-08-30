package org.example.customText.model;

import lombok.*;

import javax.persistence.*;

@Table(name = "bar")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    public Bar(String name) {
        this.name = name;
    }
}
