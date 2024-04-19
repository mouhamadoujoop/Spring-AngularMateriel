package com.momo.angular_spring;

import com.momo.angular_spring.entities.PaymentStatus;
import com.momo.angular_spring.entities.PaymentType;
import com.momo.angular_spring.entities.Payment;
import com.momo.angular_spring.entities.Student;
import com.momo.angular_spring.repository.PaymentRepository;
import com.momo.angular_spring.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class AngularSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(AngularSpringApplication.class, args);
	}
@Bean
	CommandLineRunner commandLineRunner(StudentRepository studentRepository,
										PaymentRepository paymentRepository){
		return args -> {
			studentRepository.save(Student.builder().id(UUID.randomUUID().toString())
					.firstName("Modou").code("12222").programId("GL")
					.build());
			studentRepository.save(Student.builder().id(UUID.randomUUID().toString())
					.firstName("Yacine").code("12223").programId("RI")
					.build());

			studentRepository.save(Student.builder().id(UUID.randomUUID().toString())
					.firstName("Fatou").code("12224").programId("RT")
					.build());
			studentRepository.save(Student.builder().id(UUID.randomUUID().toString())
					.firstName("Mohamed").code("12225").programId("GL")
					.build());


			PaymentType[] paymentTypes = PaymentType.values();
			Random random = new Random();
			studentRepository.findAll().forEach(st->{
				for (int i=0; i<10; i++){
					int index = random.nextInt(paymentTypes.length);
					Payment payment = Payment.builder()
							.amount(1000+(int)(Math.random()*20000))
							.type(paymentTypes[index])
							.status(PaymentStatus.CREATED)
							.date(LocalDate.now())
							.student(st)
							.build();
					paymentRepository.save(payment);
				}
			});
		};
}
}
