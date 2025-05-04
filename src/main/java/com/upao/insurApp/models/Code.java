package com.upao.insurApp.models;

import com.upao.insurApp.models.enums.EStatus;
import com.upao.insurApp.models.enums.TypeCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "codes")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Integer codeId;

    @Column(name = "code", length = 6, nullable = false, unique = true)
    private String code;

    @Column(name = "type_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeCode typeCode;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Column(name = "expired_date", nullable = false)
    private LocalDateTime expiredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;
}