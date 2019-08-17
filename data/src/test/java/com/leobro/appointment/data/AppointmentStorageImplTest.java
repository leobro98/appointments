package com.leobro.appointment.data;

import com.leobro.appointment.service.Appointment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class AppointmentStorageImplTest {

	private static final long ID = 123L;
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
	private Appointment appointment;
	private AppointmentEntity entity;

	@Before
	public void setUp() {
		repository = Mockito.mock(AppointmentRepository.class);
		storage = new AppointmentStorageImpl(repository);
		appointment = createAppointment();
		entity = createAppointmentEntity();
	}

	private static Appointment createAppointment() {
		Appointment app = new Appointment();

		app.setId(ID);
		app.setClientName(CLIENT_NAME);
		app.setTime(DATE_TIME);
		app.setPrice(PRICE);
		app.setStatus(APP_STATUS);

		return app;
	}

	private static AppointmentEntity createAppointmentEntity() {
		AppointmentEntity entity = new AppointmentEntity();

		entity.setId(ID);
		entity.setClientName(CLIENT_NAME);
		entity.setTime(DATE_TIME);
		entity.setPrice(PRICE);
		entity.setStatus(APP_STATUS);

		return entity;
	}

	@Test
	public void when_createAppointment_then_callsRepositorySave_andReturnsId() {
		appointment.setId(null);
		entity.setId(null);
		AppointmentEntity entityWithId = new AppointmentEntity();
		entityWithId.setId(ID);
		Mockito.when(repository.save(any(AppointmentEntity.class))).thenReturn(entityWithId);

		long id = storage.createAppointment(appointment);

		Mockito.verify(repository).save(entity);
		assertThat(id, is(ID));
	}

	@Test
	public void when_getAppointment_then_callsRepositoryFindById_andReturnsAppointment() {
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(entity));

		Appointment app = storage.getAppointment(ID);

		Mockito.verify(repository).findById(ID);
		assertThat(app, is(appointment));
	}

	@Test(expected = NoSuchElementException.class)
	public void when_getAppointmentAndNotFound_then_throws() {
		Mockito.when(repository.findById(Mockito.anyLong())).thenThrow(NoSuchElementException.class);

		storage.getAppointment(ID);
	}

	@Test
	public void when_getAllAppointments_then_callsRepositoryFindByTimeBetween_andReturnsAppointments() {
		ArrayList<AppointmentEntity> entities = new ArrayList<>();
		entities.add(entity);
		Mockito.when(repository.findByTimeBetween(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
				.thenReturn(entities);
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusDays(1);

		List<Appointment> appointments = storage.getAllAppointments(startDate, endDate);

		Mockito.verify(repository).findByTimeBetween(startDate, endDate);
		assertThat(appointments.get(0), is(appointment));
	}

	@Test
	public void when_updateAppointmentStatus_then_callsRepositorySave() {
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(entity));

		storage.updateAppointmentStatus(ID, APP_STATUS);

		Mockito.verify(repository).save(Mockito.any(AppointmentEntity.class));
	}

	@Test(expected = NoSuchElementException.class)
	public void when_updateAppointmentStatusAndNotFound_then_throws() {
		Mockito.when(repository.findById(Mockito.anyLong())).thenThrow(NoSuchElementException.class);

		storage.updateAppointmentStatus(ID, APP_STATUS);
	}
}
