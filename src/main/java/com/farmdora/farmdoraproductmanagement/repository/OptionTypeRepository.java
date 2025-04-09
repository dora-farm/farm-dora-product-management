package com.farmdora.farmdoraproductmanagement.repository;

import com.farmdora.farmdoraproductmanagement.entity.OptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionTypeRepository extends JpaRepository<OptionType, Integer> {
    // 필요한 커스텀 쿼리 메서드 추가
}