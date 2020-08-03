package com.nearsoft.javaschool.url;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UrlRepository extends CrudRepository<Url, Long> {
    Optional<Url> findByAlias(String alias);
    Optional<Url> findByUrl(String url);
}
