package com.leobro.appointment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public interface AppointmentStorage {

	long createAppointment(Appointment appointment);

	Appointment getAppointment(long id) throws NoSuchElementException;

	List<Appointment> getAllAppointments(LocalDate startDate, LocalDate endDate);
}
