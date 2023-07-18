package com.example.win.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.win.entities.DataMatchs;
@RepositoryRestResource
public interface DataMatchRepository extends JpaRepository<DataMatchs, Long>{

}
