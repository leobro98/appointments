package com.leobro.appointment.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AppointmentRepository extends CrudRepository<AppointmentEntity, Long> {
}
