package com.leobro.appointment.data;

import com.leobro.appointment.service.Appointment;
import com.leobro.appointment.service.AppointmentStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentStorageImpl implements AppointmentStorage {

	private final AppointmentRepository repository;

	@Autowired
	AppointmentStorageImpl(AppointmentRepository repository) {
		this.repository = repository;
	}

	@Override
	public long createAppointment(Appointment app) {
		AppointmentEntity entity = createAppointmentEntity(app);
		AppointmentEntity newEntity = repository.save(entity);
		return newEntity.getId();
	}

	private static AppointmentEntity createAppointmentEntity(Appointment app) {
		AppointmentEntity entity = new AppointmentEntity();

		entity.setClientName(app.getClientName());
		entity.setTime(app.getTime());
		entity.setPrice(app.getPrice());
		entity.setStatus(app.getStatus());

		return entity;
	}
}
