package com.leobro.appointment.data;

import com.leobro.appointment.service.Appointment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class AppointmentStorageImplTest {

	private static final long NEW_ID = 123L;
	private static final String CLIENT_NAME = "Test Client";
	private static final int YEAR = 2019;
	private static final int DAY = 20;
	private static final int HOUR = 12;
	private static final int MINUTE = 0;
	private static final LocalDateTime DATE_TIME = LocalDateTime.of(YEAR, Month.AUGUST, DAY, HOUR, MINUTE);
	private static final int PRICE = 30;
	private static final Appointment.AppStatus APP_STATUS = Appointment.AppStatus.PASS;

	private AppointmentRepository repository;
	private AppointmentStorageImpl storage;

	@Before
	public void setUp() {
		repository = Mockito.mock(AppointmentRepository.class);
		AppointmentEntity entity = new AppointmentEntity();
		entity.setId(NEW_ID);
		Mockito.when(repository.save(any(AppointmentEntity.class))).thenReturn(entity);

		storage = new AppointmentStorageImpl(repository);
	}

	private static Appointment createAppointment() {
		Appointment app = new Appointment();

		app.setClientName(CLIENT_NAME);
		app.setTime(DATE_TIME);
		app.setPrice(PRICE);
		app.setStatus(APP_STATUS);

		return app;
	}

	private static AppointmentEntity createAppointmentEntity() {
		AppointmentEntity entity = new AppointmentEntity();

		entity.setClientName(CLIENT_NAME);
		entity.setTime(DATE_TIME);
		entity.setPrice(PRICE);
		entity.setStatus(APP_STATUS);

		return entity;
	}

	@Test
	public void when_createAppointment_then_callsRepositorySave() {
		Appointment appointment = createAppointment();
		AppointmentEntity entity = createAppointmentEntity();

		long id = storage.createAppointment(appointment);

		Mockito.verify(repository).save(entity);
		assertThat(id, is(NEW_ID));
	}
}
