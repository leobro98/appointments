package com.leobro.appointment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * API specification for the proxy to a concrete data storage. Contains data manipulation methods.
 */
public interface AppointmentStorage {

	/**
	 * Creates a new appointment in the data storage according to the passed object.
	 *
	 * @param appointment the {@link Appointment} object containing data to be persisted.
	 * @return ID of the appointment created in the data storage.
	 */
	long createAppointment(Appointment appointment);

	/**
	 * Retrieves the appointment with the passed ID from the data storage.
	 *
	 * @param id ID of the appointment.
	 * @return Appointment with this ID.
	 */
	Appointment getAppointment(long id) throws NoSuchElementException;

	/**
	 * Retrieves all appointments that are scheduled between the passed date range sorted by their price.
	 *
	 * @param startDate start date of the date interval; must be earlier then endDate,
	 * @param endDate   end date of the date interval; must be later then startDate.
	 * @return All appointments within the date interval sorted by the price.
	 */
	List<Appointment> getAllAppointments(LocalDate startDate, LocalDate endDate);
}
