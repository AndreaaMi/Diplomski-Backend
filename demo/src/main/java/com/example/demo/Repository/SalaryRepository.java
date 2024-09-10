package com.example.demo.Repository;

import com.example.demo.Model.Salary;
import com.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {
    Salary save(Salary salary);

    void delete(Salary salary);
    Salary findByUser(User user);
    Salary findByUserAndSalaryMonth(User user, LocalDate salaryMonth);
    List<Salary> findAllByUser(User user);
    List<Salary> findByUserAndSalaryMonthBetween(User user, LocalDate start, LocalDate end);

}

