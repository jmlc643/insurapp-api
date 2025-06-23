package com.upao.insurApp.repos;

import com.upao.insurApp.models.Reserve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Integer> {
    @Query("SELECT r FROM Reserve r WHERE r.field.fieldId = :field_id AND r.bookingDate = :bookingDate")
    List<Reserve> findReserveByFieldAndDate(@Param("field_id") Integer field_id, @Param("bookingDate") LocalDate bookingDate);

    @Query("SELECT r FROM Reserve r WHERE r.field.fieldId = :field_id AND r.bookingDate = :bookingDate")
    List<Reserve> findByFieldIdAndBookingDate(@Param("field_id") Integer fieldId, @Param("bookingDate") LocalDate bookingDate);

    Optional<Reserve> findByQrUrl(String qrUrl);

    List<Reserve> findByUserUserIdOrderByReserveIdDesc(Integer userId);
}
