package com.techzen.techlearn.repository;

import com.techzen.techlearn.dto.response.TeacherCalendarFreeResponseDTO;
import com.techzen.techlearn.entity.TeacherCalendarEntity;
import com.techzen.techlearn.entity.TeacherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TeacherCalendarRepository extends JpaRepository<TeacherCalendarEntity, Integer> {

    boolean existsByTeacherAndDateAppointmentAndTimeStartAndTimeEnd(TeacherEntity teacher, LocalDate dateAppointment,LocalTime timeStart, LocalTime timeEnd);

    @Query(nativeQuery = true, value = "select teacher.name, calendar.time_start, calendar.time_end, " +
            "teacher_calendar.date_appointment, teacher_calendar.is_all_day \n" +
            "from calendar join teacher_calendar on calendar.id = teacher_calendar.id_time \n" +
            "join teacher on teacher_calendar.id_teacher = teacher.id where teacher_calendar.status = 'Đang trống' \n" +
            "and teacher_calendar.date_appointment >= CURRENT_DATE or( teacher_calendar.date_appointment >= CURRENT_DATE" +
            " and calendar.time_end >= current_time)")
    List<Object[]> findAllTeacherFree();

    @Query("SELECT tc " +
            "FROM TeacherCalendarEntity tc " +
            "JOIN tc.teacher t " +
            "JOIN TechnicalEntity te ON te.teacherEntity.id = t.id " +
            "WHERE te.name = :technicalName " +
            "AND t.name = :teacherName " +
            "AND tc.dateAppointment >= CURRENT_DATE " +
            "AND tc.status = 'Booked'")
    List<TeacherCalendarEntity> findAppointmentsByTechnicalAndTeacher(
            @Param("technicalName") String technicalName,
            @Param("teacherName") String teacherName);

}
