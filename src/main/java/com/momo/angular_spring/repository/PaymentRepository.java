package com.momo.angular_spring.repository;

import com.momo.angular_spring.entities.PaymentStatus;
import com.momo.angular_spring.entities.PaymentType;
import com.momo.angular_spring.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByStudentCode(String code);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByType(PaymentType type);

}
