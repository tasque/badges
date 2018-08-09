package org.badges.db.repository;


import org.badges.db.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface NewsRepository extends JpaRepository<News, Long>, QueryDslPredicateExecutor<News> {


}
