package com.example.dlocal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dlocal.model.Sale;

@Repository
public interface SaleDao extends JpaRepository<Sale, Integer> {

	@Query(value = "select count(*) from sale where status=?1", nativeQuery = true)
	int getSaleByStatus(Short status);
}
