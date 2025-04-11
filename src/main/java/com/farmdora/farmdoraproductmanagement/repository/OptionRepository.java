package com.farmdora.farmdoraproductmanagement.repository;

import com.farmdora.farmdoraproductmanagement.entity.Options;
import com.farmdora.farmdoraproductmanagement.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Options, Integer> {

    List<Options> findBySale(Sale sale);
}