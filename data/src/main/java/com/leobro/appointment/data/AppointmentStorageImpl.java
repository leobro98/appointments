package com.leobro.appointment.data;

import com.leobro.appointment.service.Appointment;
import com.leobro.appointment.service.AppointmentStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AppointmentStorageImpl implements AppointmentStorage {

	private final AppointmentRepository repository;

	@Autowired
	AppointmentStorageImpl(AppointmentRepository repository) {
		this.repository = repository;
	}

	@Override
	public long createAppointment(Appointment app) {
		AppointmentEntity entity = mapAppointmentEntity(app);
		AppointmentEntity newEntity = repository.save(entity);
		return newEntity.getId();
	}

	private static AppointmentEntity mapAppointmentEntity(Appointment app) {
		AppointmentEntity entity = new AppointmentEntity();

		entity.setClientName(app.getClientName());
		entity.setTime(app.getTime());
		entity.setPrice(app.getPrice());
		entity.setStatus(app.getStatus());

		return entity;
	}

	@Override
	public Appointment getAppointment(long id) throws NoSuchElementException {
		Optional<AppointmentEntity> optional = repository.findById(id);
		AppointmentEntity entity = optional.get();
		return mapAppointment(entity);
	}

	private Appointment mapAppointment(AppointmentEntity entity) {
		Appointment app = new Appointment();

		app.setId(entity.getId());
		app.setClientName(entity.getClientName());
		app.setTime(entity.getTime());
		app.setPrice(entity.getPrice());
		app.setStatus(entity.getStatus());

		return app;
	}

	@Override
	public List<Appointment> getAllAppointments(LocalDate startDate, LocalDate endDate) {
		List<AppointmentEntity> entities = repository.findByTimeBetween(startDate, endDate);
		return entities.stream()
				.map(this::mapAppointment)
				.collect(Collectors.toList());
	}
}
