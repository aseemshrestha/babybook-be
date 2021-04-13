package com.babybook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
//https://github.com/eugenp/tutorials/tree/master/persistence-modules/spring-data-jpa/src/main/java/com/baeldung/boot/daos
public interface CommonRepository<T, ID extends Serializable> extends JpaRepository<T, ID>
{

}
