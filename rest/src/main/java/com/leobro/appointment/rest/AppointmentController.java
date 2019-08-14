package com.leobro.appointment.rest;

import com.leobro.appointment.service.Appointment;
import com.leobro.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller of the REST Web service. This is the first class hit by the client's request.
 * It returns results as JSON or XML string depending on the HTTP Accept header of the request.
 */
@RestController
@RequestMapping(path = "/")
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
	public ResponseEntity<?> createRandomAppointments(@RequestParam("quantity") String quantity,
													  @RequestParam("enddate") String endDate) {
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
	public ResponseEntity<?> getAppointment(@PathVariable("id") String id) {
		return ResponseEntity.ok().build();
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
	public ResponseEntity<?> getAllAppointments(@RequestParam("startdate") String startDate,
												@RequestParam("enddate") String endDate) {
		return ResponseEntity.ok().build();
	}

	/**
	 * Updates the status of the existing appointment.
	 *
	 * @param id     ID of the appointment.
	 * @param status new status for the appointment.
	 * @return An empty body.
	 */
	@PutMapping("appointments/{id}")
	public ResponseEntity<?> updateAppointmentStatus(@PathVariable("id") String id,
													 @RequestBody String status) {
		return ResponseEntity.ok().build();
	}

	/**
	 * Deletes the appointment with the passed ID.
	 *
	 * @param id ID of the appointment to be deleted.
	 * @return The deleted appointment.
	 */
	@DeleteMapping("appointments/{id}")
	public ResponseEntity<?> deleteAppointment(@PathVariable("id") String id) {
		return ResponseEntity.ok().build();
	}
}
