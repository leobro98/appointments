package com.leobro.appointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.leobro.appointment.service.ResponseFactory.createFatalResponse;
import static com.leobro.appointment.service.ResponseFactory.createOkResponse;

/**
 * Holds the business logic of the application. Serves requests from the REST controller.
 */
@Service
public class AppointmentService {

	private AppointmentStorage storage;

	@Autowired
	AppointmentService(AppointmentStorage storage) {
		this.storage = storage;
	}

	/**
	 * Creates a new appointment in the data storage according to the passed object.
	 *
	 * @param appointment the {@link Appointment} object containing data to be persisted.
	 * @return ID of the appointment created in the data storage.
	 */
	public ServiceResponse createAppointment(Appointment appointment) {
		try {
			long id = storage.createAppointment(appointment);
			return createOkResponse(id);
		} catch (Exception e) {
			return createFatalResponse();
		}
	}

	/**
	 * Randomly creates given number of test appointments from current moment to the end of the given date.
	 *
	 * @param quantity the number of appointments to create,
	 * @param endDate  the last date for the appointments.
	 * @return The number of actually created appointments.
	 */
	public ServiceResponse createRandomAppointments(int quantity, LocalDate endDate) {
		List<String> errors = verifyRandom(quantity, endDate);
		if (errors.size() > 0) {
			return ResponseFactory.createErrorResponse(errors);
		}

		List<Appointment> appointments = RandomHelper.getRandomAppointments(quantity, endDate);

		try {
			for (Appointment app : appointments) {
				storage.createAppointment(app);
			}
			return createOkResponse(appointments.size());
		} catch (Exception e) {
			return createFatalResponse();
		}
	}

	private static List<String> verifyRandom(int quantity, LocalDate endDate) {
		ArrayList<String> errors = new ArrayList<>();

		if (quantity <= 0) {
			errors.add("Quantity must be a positive integer.");
		}

		if (endDate.isBefore(LocalDate.now())) {
			errors.add("End date must be in future.");
		}
		return errors;
	}

	public ServiceResponse getAppointment(long id) {
		try {
			Appointment appointment = storage.getAppointment(id);
			return createOkResponse(appointment);
		} catch (NoSuchElementException e) {
			return ResponseFactory.createNotFoundResponse();
		} catch (Exception e) {
			return createFatalResponse();
		}
	}
}
