package org.example.customText;

import org.example.customText.model.Foo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FooRepository extends JpaRepository<Foo, Long> {


    @Query("select f " +
            "from Foo f " +
            "join fetch f.bar b " +
            "where (:text is null or b.name = :text)")
    public List<Foo> findAllByParam(@Param("text") String text);

    @Query("select f " +
            "from Foo f " +
            "join fetch f.bar b " +
            "where (:ids is null or b.id in :ids)")
    public List<Foo> findAllByParams(@Param("ids") List<Long> ids);
}
