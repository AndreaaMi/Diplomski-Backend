package com.example.demo.DTO;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class ShiftDTO {
    private Integer id;
    private Integer userId;
    private Integer busId; // New field
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private Integer extraHours;
    private List<DayOfWeek> daysOfWeek;  // Add this field


    public ShiftDTO() {
    }

    public ShiftDTO(Integer id, Integer userId, Integer busId, LocalDateTime startTime, LocalDateTime endTime, String location, Integer extraHours) {
        this.id = id;
        this.userId = userId;
        this.busId = busId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.extraHours = extraHours;
    }


    public Integer getId() {
        return id;
    }

    public ShiftDTO(Integer id, Integer userId, Integer busId, LocalDateTime startTime, LocalDateTime endTime, String location, Integer extraHours, List<DayOfWeek> daysOfWeek) {
        this.id = id;
        this.userId = userId;
        this.busId = busId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.extraHours = extraHours;
        this.daysOfWeek = daysOfWeek;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getExtraHours() {
        return extraHours;
    }

    public void setExtraHours(Integer extraHours) {
        this.extraHours = extraHours;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}
