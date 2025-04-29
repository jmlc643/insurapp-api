package com.upao.insurApp.repos;

import com.upao.insurApp.models.Code;
import com.upao.insurApp.models.User;
import com.upao.insurApp.models.enums.TypeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, Integer> {

    Optional<Code> findByCode(String code);

    Optional<Code> findFirstByUser(User user);

    Optional<Code> findFirstByUserAndTypeCodeOrderByCodeIdDesc(User user, TypeCode typeCode);
}
