package com.leobro.appointment.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Month;

public class AppointmentServiceTest {

	private static final String CLIENT_NAME = "Test Client";
	private static final int YEAR = 2019;
	private static final int DAY = 20;
	private static final int HOUR = 12;
	private static final int MINUTE = 0;
	private static final LocalDateTime DATE_TIME = LocalDateTime.of(YEAR, Month.AUGUST, DAY, HOUR, MINUTE);
	private static final int PRICE = 30;
	private static final Appointment.AppStatus APP_STATUS = Appointment.AppStatus.PASS;

	private AppointmentService service;
	private AppointmentStorage storage;

	@Before
	public void setUp() {
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
		Appointment appointment = createAppointment();
		service.createAppointment(appointment);
		Mockito.verify(storage).createAppointment(appointment);
	}
}
