package com.leobro.appointment.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
interface AppointmentRepository extends CrudRepository<AppointmentEntity, Long> {

	@Query(value = "SELECT * FROM appointment WHERE time BETWEEN ?1 AND ?2",
			nativeQuery = true)
	List<AppointmentEntity> findByTimeBetween(LocalDate startDate, LocalDate endDate);
}
