package com.example.demo.Service;
import com.example.demo.DTO.SalaryDTO;
import com.example.demo.Model.Salary;
import com.example.demo.Model.User;
import com.example.demo.Repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalaryService {
    @Autowired
    private SalaryRepository salaryRepository;
    @Autowired
    private UserService userService;
    public Salary save(Salary salary){
        return this.salaryRepository.save(salary);
    }
    public void delete(Salary salary) {
        salaryRepository.delete(salary);
    }
    public Salary getByUser(User user) {
        return salaryRepository.findByUser(user);
    }
    public Salary getByUserAndMonth(User user, LocalDate month) {
        return salaryRepository.findByUserAndSalaryMonth(user, month);
    }

    public boolean canAssignSalary(User user, LocalDate month) {
        return salaryRepository.findByUserAndSalaryMonth(user, month) == null;
    }

    public List<Salary> findAllByUser(User user) {
        return salaryRepository.findAllByUser(user);
    }

    public boolean isSalaryAssigned(User user, LocalDate date) {
        return salaryRepository.findByUserAndSalaryMonth(user, date) != null;
    }
    public List<Salary> getByUserAndMonthRange(User user, LocalDate start, LocalDate end) {
        return salaryRepository.findByUserAndSalaryMonthBetween(user, start, end);
    }

    public List<SalaryDTO> generateSalaryReport(Integer userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        List<Salary> salaries = findAllByUser(user);
        return salaries.stream().map(salary -> new SalaryDTO(
                salary.getSalaryMonth(),
                salary.getBaseSalary(),
                salary.getOvertimeHours(),
                salary.getHolidayWorkHours(),
                salary.getNightShiftHours(),
                salary.getSickLeaveHours(),
                salary.getOvertimePayRate(),
                salary.getHolidayPayRate(),
                salary.getNightShiftPayRate(),
                salary.getSickLeaveType(),
                salary.getTotalSalary()
        )).collect(Collectors.toList());
    }

}

