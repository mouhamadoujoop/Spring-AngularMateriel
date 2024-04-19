package com.momo.angular_spring.web;

import com.momo.angular_spring.entities.Payment;
import com.momo.angular_spring.entities.PaymentStatus;
import com.momo.angular_spring.entities.PaymentType;
import com.momo.angular_spring.entities.Student;
import com.momo.angular_spring.repository.PaymentRepository;
import com.momo.angular_spring.repository.StudentRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
public class PaymentRestController {
    private StudentRepository studentRepository;
    private PaymentRepository paymentRepository;
    public PaymentRestController(StudentRepository studentRepository, PaymentRepository paymentRepository){
            this.paymentRepository=paymentRepository;
            this.studentRepository=studentRepository;
    }
    @GetMapping (path = "/payments")
    public List<Payment> allPayments(){
        return paymentRepository.findAll();
    }
    @GetMapping (path = "/students/{code}/payments")
    public List<Payment> paymentByStudent(@PathVariable String code){
        return paymentRepository.findByStudentCode(code);
    }
    @GetMapping (path = "/payments/byStatus")
    public List<Payment> paymentByStatus(@RequestParam PaymentStatus status){
        return paymentRepository.findByStatus(status);
    }
    @GetMapping (path = "/payments/byType")
    public List<Payment> paymentByType(@RequestParam PaymentType type){
        return paymentRepository.findByType(type);
    }
    @GetMapping (path = "/payments/{id}")
    public Payment getPaymentById(@PathVariable Long id){
        return paymentRepository.findById(id).get();
    }
    @GetMapping (path = "/students")
    public List<Student> allStudents(){
        return studentRepository.findAll();
    }
    @GetMapping (path = "/students/{code}")
    public Student getStudentByCode(@PathVariable String code){
        return studentRepository.findByCode(code);
    }
    @GetMapping (path = "/studentsByProgramId")
    public List<Student> getStudentByProgramId(@RequestParam String programId){
        return studentRepository.findByProgramId(programId);
    }
    @PutMapping("/payments/{id}")
    public  Payment updatePaymentStatus(@RequestParam PaymentStatus status, @PathVariable Long id){
        Payment payment = paymentRepository.findById(id).get();
        payment.setStatus(status);
        return paymentRepository.save(payment);

    }
    @PostMapping(path = "/payments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Payment savePayment(@RequestParam MultipartFile file, LocalDate date, double amount,
                               PaymentType type, String studentCode) throws IOException {
       Path folderPath = Paths.get(System.getProperty("user.home"),"mom-data", "payments");
       if(!Files.exists(folderPath)){
          Files.createDirectories(folderPath);
       }
        String fileName = UUID.randomUUID().toString();
        Path filePath = Paths.get(System.getProperty("user.home"),"momo-data","payments", fileName+".pdf");
        Files.copy(file.getInputStream(),filePath);
        Student student = studentRepository.findByCode(studentCode);
        Payment payment = Payment.builder()
                .date(date).type(type).student(student)
                .amount(amount)
                .file(filePath.toUri().toString())
                .status(PaymentStatus.CREATED)
                .build();
        return paymentRepository.save(payment);
    }
    @GetMapping(path = "/paymentFile/{paymentId}", produces = {MediaType.APPLICATION_PDF_VALUE})
    public  byte[] getPaymentFile(@PathVariable Long paymentId) throws IOException{
        Payment payment = paymentRepository.findById(paymentId).get();
        return Files.readAllBytes(Path.of(payment.getFile()));
    }
}
