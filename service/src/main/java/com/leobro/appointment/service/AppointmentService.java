package com.leobro.appointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.leobro.appointment.service.ResponseFactory.*;

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

	public ServiceResponse createRandomAppointments(String quantity, String endDate) {
		return null;
	}
}
