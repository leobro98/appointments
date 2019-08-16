package com.leobro.appointment.service;

import java.util.NoSuchElementException;

public interface AppointmentStorage {

	long createAppointment(Appointment appointment);

	Appointment getAppointment(long id) throws NoSuchElementException;
}
