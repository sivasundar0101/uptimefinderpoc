package com.poc.uptimefinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poc.uptimefinder.model.Urlstatus;
@Repository
public interface UrlstatusRepository extends JpaRepository<Urlstatus, Long>{
	List<Urlstatus> findByurl(String url);

}
