package com.example.demo.Service;

import com.example.demo.Model.Shift;
import com.example.demo.Model.User;
import com.example.demo.Repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;

    public Shift save(Shift shift){
        return this.shiftRepository.save(shift);
    }
    public Shift getById(Integer shiftId){
        return shiftRepository.getById(shiftId);
    }

    public List<Shift> findAll(){
        return shiftRepository.findAll();
    }

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public List<Shift> saveAll(List<Shift> shifts){
        return shiftRepository.saveAll(shifts); // Batch save method
    }

    public List<Shift> findByDate(LocalDate date) {
        return shiftRepository.findAll().stream()
                .filter(shift -> shift.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }
    public List<Shift> findAllByUserAndMonth(User user, LocalDate month) {
        LocalDateTime start = month.atStartOfDay(); // Start of the month at 00:00
        LocalDateTime end = month.withDayOfMonth(month.lengthOfMonth()).atTime(23, 59, 59); // End of the month at 23:59
        return shiftRepository.findByUserAndStartTimeBetween(user, start, end);
    }


}
