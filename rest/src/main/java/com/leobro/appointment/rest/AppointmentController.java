package com.leobro.appointment.rest;

import com.leobro.appointment.service.Appointment;
import com.leobro.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * Controller of the REST Web service. This is the first class hit by the client's request.
 * It returns results as JSON or XML string depending on the HTTP Accept header of the request.
 */
@RestController
@RequestMapping(path = "/")
@Validated
public class AppointmentController {

	private final AppointmentService service;

	@Autowired
	public AppointmentController(AppointmentService service) {
		this.service = service;
	}

	/**
	 * Creates a new appointment according to the request body content.
	 *
	 * @return The created appointment ID.
	 */
	@PostMapping("appointments")
	public ResponseEntity<?> createAppointment(@Valid @RequestBody Appointment app) {
		return ResponseFactory.createResponse(
				service.createAppointment(app));
	}

	/**
	 * Creates a set of new appointments with random client names for a random time in the given date interval.
	 * This functionality is required for testing the Web service.
	 *
	 * @param quantity The number of appointments to randomly create. The actually created appointment count may
	 *                 differ, if some of them are discarded when they overlap with already created ones.
	 * @param endDate  The end date of the date interval starting from the current date.
	 * @return The number of actually created appointments.
	 */
	@PostMapping("schedule")
	public ResponseEntity<?> createRandomAppointments(@RequestParam("quantity") int quantity,
													  @RequestParam("enddate")
													  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return ResponseFactory.createResponse(
				service.createRandomAppointments(quantity, endDate));
	}

	/**
	 * Retrieves the appointment with the passed ID.
	 *
	 * @param id ID of the appointment.
	 * @return The appointment with the requested ID.
	 */
	@GetMapping("appointments/{id}")
	public ResponseEntity<?> getAppointment(@PathVariable("id") long id) {
		return ResponseFactory.createResponse(
				service.getAppointment(id));
	}

	/**
	 * Retrieves all appointments that are scheduled between the passed date range. The appointments are sorted by
	 * their price.
	 *
	 * @param startDate The start date of the date interval.
	 * @param endDate   The end date of the date interval.
	 * @return All appointments within the date interval sorted by the price.
	 */
	@GetMapping("appointments")
	public ResponseEntity<?> getAllAppointments(@RequestParam("startdate")
												@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
												@RequestParam("enddate")
												@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return ResponseFactory.createResponse(
				service.getAllAppointments(startDate, endDate));
	}

	/**
	 * Updates the status of the existing appointment.
	 *
	 * @param id     ID of the appointment.
	 * @param status new status for the appointment.
	 * @return An empty body.
	 */
	@PutMapping("appointments/{id}")
	public ResponseEntity<?> updateAppointmentStatus(@PathVariable("id") long id,
													 @RequestBody Appointment.AppStatus status) {
		return ResponseFactory.createResponse(
				service.updateAppointmentStatus(id, status));
	}

	/**
	 * Deletes the appointment with the passed ID.
	 *
	 * @param id ID of the appointment to be deleted.
	 * @return The deleted appointment.
	 */
	@DeleteMapping("appointments/{id}")
	public ResponseEntity<?> deleteAppointment(@PathVariable("id") long id) {
		return ResponseFactory.createResponse(
				service.deleteAppointment(id));
	}
}
