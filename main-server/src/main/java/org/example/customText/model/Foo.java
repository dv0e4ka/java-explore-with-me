package org.example.customText.model;

import lombok.*;
import org.example.customText.model.Bar;

import javax.persistence.*;

@Table(name = "foo")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Foo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bar")
    private Bar bar;

    public Foo(String name, Bar bar) {
        this.name = name;
        this.bar = bar;
    }
}
