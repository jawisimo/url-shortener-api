package com.ikg100.urlshortenerapi.url;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    @EntityGraph(attributePaths = "user")
    Optional<Url> findUrlByShortUrlCode(String shortUrlCode);

    @EntityGraph(attributePaths = "user")
    @Query(value = "SELECT u FROM Url u WHERE u.user.id = :userId")
    List<Url> findAllUrlsByUserId(@Param("userId") Long userId);

    boolean existsUrlByShortUrlCode(String shortUrlCode);
}
