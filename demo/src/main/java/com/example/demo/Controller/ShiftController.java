package com.example.demo.Controller;

import com.example.demo.DTO.ShiftDTO;
import com.example.demo.Model.Bus;
import com.example.demo.Model.Route;
import com.example.demo.Model.Shift;
import com.example.demo.Model.Salary;
import com.example.demo.Model.User;
import com.example.demo.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/CityFlow/shift")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private UserService userService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private BusService busService;

    @Autowired
    private SalaryService salaryService;

    @GetMapping("/all")
    public ResponseEntity<List<ShiftDTO>> getAllShifts() {
        List<Shift> shifts = shiftService.getAllShifts();
        List<ShiftDTO> dtos = new ArrayList<>();
        for (Shift shift : shifts) {
            ShiftDTO dto = new ShiftDTO(
                    shift.getId(),
                    shift.getUser().getId(),
                    shift.getBus() != null ? shift.getBus().getId() : null,
                    shift.getStartTime(),
                    shift.getEndTime(),
                    shift.getLocation(),
                    shift.getExtraHours() // Now correctly fetching extra hours
            );
            dtos.add(dto);
        }
        if (shifts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addShift(@RequestBody ShiftDTO shiftDTO) {
        User user = userService.findById(shiftDTO.getUserId());
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }

        List<DayOfWeek> daysOfWeek = shiftDTO.getDaysOfWeek();  // Preuzimamo dane iz DTO-a
        LocalDate startDate = shiftDTO.getStartTime().toLocalDate();
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        List<Shift> shiftsToAdd = new ArrayList<>();
        int totalDays = 0;  // Ovo će nam pomoći da izračunamo ukupan broj dana

        while (startDate.isBefore(endDate) || startDate.equals(endDate)) {
            if (daysOfWeek.contains(startDate.getDayOfWeek())) {
                LocalDateTime startTime = LocalDateTime.of(startDate, shiftDTO.getStartTime().toLocalTime());
                LocalDateTime endTime = startTime.plusHours(8);
                if (shiftDTO.getExtraHours() != null && shiftDTO.getExtraHours() > 0) {
                    endTime = endTime.plusHours(shiftDTO.getExtraHours());
                }

                Shift shift = new Shift();
                shift.setUser(user);
                shift.setStartTime(startTime);
                shift.setEndTime(endTime);
                shift.setLocation(shiftDTO.getLocation());
                shift.setExtraHours(shiftDTO.getExtraHours());

                if ("ROLE_DRIVER".equals(user.getRoles())) {
                    Bus bus = busService.findById(shiftDTO.getBusId());
                    if (bus == null) {
                        return new ResponseEntity<>("Bus not found", HttpStatus.BAD_REQUEST);
                    }
                    shift.setBus(bus);
                }

                shiftsToAdd.add(shift);
                totalDays++;  // Povećavamo broj dana kada se smena dodaje
            }
            startDate = startDate.plusDays(1);
        }

        try {
            shiftService.saveAll(shiftsToAdd);

            // Ažuriramo platu samo ako već postoji za taj mesec
            updateSalaryForUserAndMonth(user.getId(), shiftDTO.getStartTime().getYear(), shiftDTO.getStartTime().getMonthValue(), totalDays, shiftDTO.getExtraHours());

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void updateSalaryForUserAndMonth(int userId, int year, int month, int totalDays, Integer extraHoursPerDay) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        LocalDate salaryDate = LocalDate.of(year, month, 1);

        // Pronađi postojeću platu za taj mesec
        Salary salary = salaryService.getByUserAndMonth(user, salaryDate);
        if (salary == null) {
            // Ako ne postoji plata za taj mesec, preskačemo ažuriranje
            return;
        }

        // Računamo ukupne ekstra sate
        double totalExtraHours = totalDays * (extraHoursPerDay != null ? extraHoursPerDay : 0);

        // Ažuriramo overtimeHours
        salary.setOvertimeHours(salary.getOvertimeHours() + totalExtraHours);

        // Ponovo preračunavamo ukupnu platu
        double totalSalary = salary.getBaseSalary() +
                (salary.getOvertimeHours() * salary.getOvertimePayRate()) +
                (salary.getHolidayWorkHours() * salary.getHolidayPayRate()) +
                (salary.getNightShiftHours() * salary.getNightShiftPayRate());

        salary.setTotalSalary(totalSalary);

        // Čuvamo ažuriranu platu
        salaryService.save(salary);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ShiftDTO>> getShiftsByDate(@PathVariable("date") String dateString) {
        LocalDate date;
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Shift> shifts = shiftService.findByDate(date);
        if (shifts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<ShiftDTO> dtos = shifts.stream()
                .map(shift -> new ShiftDTO(shift.getId(),
                        shift.getUser().getId(),
                        shift.getBus() != null ? shift.getBus().getId() : null,
                        shift.getStartTime(),
                        shift.getEndTime(),
                        shift.getLocation(),
                        shift.getExtraHours()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


}
