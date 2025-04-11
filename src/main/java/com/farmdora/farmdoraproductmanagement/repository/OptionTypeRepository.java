package com.farmdora.farmdoraproductmanagement.repository;

import com.farmdora.farmdoraproductmanagement.entity.OptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionTypeRepository extends JpaRepository<OptionType, Integer> {
    // 필요한 커스텀 쿼리 메서드 추가
    @Query("SELECT ot FROM OptionType ot JOIN FETCH ot.optionTypeBig WHERE ot.id = :id")
    Optional<OptionType> findByIdWithTypeBig(@Param("id") Integer id);
}