package com.leobro.appointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.leobro.appointment.service.ResponseFactory.*;

/**
 * Holds the business logic of the application. Serves requests from the REST controller.
 */
@Service
public class AppointmentService {

	private static final String QUANTITY_ERROR = "Quantity must be a positive integer.";
	private static final String END_DATE_ERROR = "End date must be in future.";
	private static final String DATES_ERROR = "Start date must be before the end date.";

	private AppointmentStorage storage;

	@Autowired
	AppointmentService(AppointmentStorage storage) {
		this.storage = storage;
	}

	/**
	 * Creates a new appointment in the data storage according to the passed object.
	 *
	 * @param appointment the {@link Appointment} object containing data to be persisted.
	 * @return The response with the ID of the appointment created in the data storage.
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
	 * @return The response with the number of actually created appointments.
	 */
	public ServiceResponse createRandomAppointments(int quantity, LocalDate endDate) {
		List<String> errors = verifyRandom(quantity, endDate);
		if (errors.size() > 0) {
			return createErrorResponse(errors);
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
			errors.add(QUANTITY_ERROR);
		}

		if (endDate.isBefore(LocalDate.now())) {
			errors.add(END_DATE_ERROR);
		}
		return errors;
	}

	/**
	 * Retrieves the appointment with the passed ID from the data storage.
	 *
	 * @param id ID of the appointment.
	 * @return The response with the appointment in its payload.
	 */
	public ServiceResponse getAppointment(long id) {
		try {
			Appointment appointment = storage.getAppointment(id);
			return createOkResponse(appointment);
		} catch (NoSuchElementException e) {
			return createNotFoundResponse();
		} catch (Exception e) {
			return createFatalResponse();
		}
	}

	/**
	 * Retrieves all appointments that are scheduled between the passed date range sorted by their price.
	 *
	 * @param startDate start date of the date interval; must be earlier then endDate,
	 * @param endDate   end date of the date interval; must be later then startDate.
	 * @return The response with all appointments within the date interval sorted by the price.
	 */
	public ServiceResponse getAllAppointments(LocalDate startDate, LocalDate endDate) {
		List<String> errors = verifyDates(startDate, endDate);
		if (errors.size() > 0) {
			return createErrorResponse(errors);
		}

		try {
			List<Appointment> appointments = storage.getAllAppointments(startDate, endDate);
			appointments.sort(Comparator.comparing(Appointment::getPrice));
			return createOkResponse(appointments);
		} catch (Exception e) {
			return createFatalResponse();
		}
	}

	private static List<String> verifyDates(LocalDate startDate, LocalDate endDate) {
		ArrayList<String> errors = new ArrayList<>();

		if (startDate.isAfter(endDate)) {
			errors.add(DATES_ERROR);
		}
		return errors;
	}

	/**
	 * Updates the status of the existing appointment.
	 *
	 * @param id     ID of the appointment,
	 * @param status new status for the appointment.
	 * @return Response with the empty payload.
	 */
	public ServiceResponse updateAppointmentStatus(long id, Appointment.AppStatus status) {
		try {
			storage.updateAppointmentStatus(id, status);
			return createOkResponse(null);
		} catch (NoSuchElementException e) {
			return createNotFoundResponse();
		} catch (Exception e) {
			return createFatalResponse();
		}
	}

	/**
	 * Deletes the appointment with the passed ID.
	 *
	 * @param id ID of the appointment to be deleted.
	 * @return Response with the deleted appointment.
	 */
	public ServiceResponse deleteAppointment(long id) {
		try {
			Appointment appointment = storage.deleteAppointment(id);
			return createOkResponse(appointment);
		} catch (NoSuchElementException e) {
			return createNotFoundResponse();
		} catch (Exception e) {
			return createFatalResponse();
		}
	}
}
