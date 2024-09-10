package com.example.demo.Repository;

import com.example.demo.Model.Salary;
import com.example.demo.Model.Shift;
import com.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {

    Shift save(Shift shift);
    Shift getById(Integer shiftId);
    List<Shift> findAll();
    List<Shift> findByUserAndStartTimeBetween(User user, LocalDateTime start, LocalDateTime end);
}
