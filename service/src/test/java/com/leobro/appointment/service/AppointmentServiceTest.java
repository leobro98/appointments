package com.leobro.appointment.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AppointmentServiceTest {

	private static final String CLIENT_NAME = "Test Client";
	private static final int YEAR = 2019;
	private static final int DAY = 20;
	private static final int HOUR = 12;
	private static final int MINUTE = 0;
	private static final LocalDateTime DATE_TIME = LocalDateTime.of(YEAR, Month.AUGUST, DAY, HOUR, MINUTE);
	private static final int PRICE = 30;
	private static final Appointment.AppStatus APP_STATUS = Appointment.AppStatus.PASS;
	private static final long CREATED_ID = 1L;

	private AppointmentService service;
	private AppointmentStorage storage;
	private Appointment appointment;

	@Before
	public void setUp() {
		appointment = createAppointment();
		storage = Mockito.mock(AppointmentStorage.class);

		service = new AppointmentService(storage);
	}

	private static Appointment createAppointment() {
		Appointment app = new Appointment();

		app.setClientName(CLIENT_NAME);
		app.setTime(DATE_TIME);
		app.setPrice(PRICE);
		app.setStatus(APP_STATUS);

		return app;
	}

	@Test
	public void when_createAppointment_then_callsStorageCreateAppointment() {
		Mockito.when(storage.createAppointment(Mockito.any(Appointment.class))).thenReturn(CREATED_ID);

		ServiceResponse response = service.createAppointment(appointment);

		Mockito.verify(storage).createAppointment(appointment);
		assertThat(response.getResult(), is(ServiceResponse.ResultType.OK));
		assertThat(response.getPayload(), is(CREATED_ID));
	}

	@Test
	public void when_createRandomAppointments_then_callsStorageCreateAppointment() {
		ServiceResponse response = service.createRandomAppointments(5, LocalDate.now().plusDays(1));

		Mockito.verify(storage, Mockito.atLeastOnce()).createAppointment(Mockito.any(Appointment.class));
		assertThat(response.getResult(), is(ServiceResponse.ResultType.OK));
	}

	@Test
	public void when_getAppointment_then_callsStorageGetAppointment() {
		Mockito.when(storage.getAppointment(Mockito.anyLong())).thenReturn(appointment);

		ServiceResponse response = service.getAppointment(1);

		Mockito.verify(storage).getAppointment(Mockito.anyLong());
		assertThat(response.getResult(), is(ServiceResponse.ResultType.OK));
		assertThat(response.getPayload(), is(appointment));
	}

	@Test
	public void when_getAllAppointments_then_callsStorageGetAllAppointments() {
		List<Appointment> appointments = new ArrayList<>();
		Mockito.when(storage.getAllAppointments(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
				.thenReturn(appointments);

		LocalDate startDate = LocalDate.of(YEAR, Month.AUGUST, 20);
		LocalDate endDate = LocalDate.of(YEAR, Month.AUGUST, 21);
		ServiceResponse response = service.getAllAppointments(startDate, endDate);

		Mockito.verify(storage).getAllAppointments(startDate, endDate);
	}
}
