package com.leobro.appointment.service;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The appointment with a client.
 */
public class Appointment {

	/**
	 * The assignment gives no idea what the business sense the appointment status has, so this is just
	 * a hypothetical example.
	 * <p> OBTAIN - client will come to obtain the repaired car,
	 * <p> WAIT - client will wait while the car is repaired,
	 * <p> PASS - client will pass the car for handling.
	 */
	public enum AppStatus {
		OBTAIN,
		WAIT,
		PASS;
	}

	private static final String NAME_MISSING_ERROR = "Name is mandatory";
	private static final String TIME_MISSING_ERROR = "Time is mandatory";
	private static final String TIME_IN_PAST_ERROR = "The time is in the past";
	private static final String PRICE_INVALID_ERROR = "Price must be positive, the maximal value is 99 999";
	private static final String STATUS_MISSING_ERROR = "Status is mandatory";

	private Long id;

	@NotBlank(message = NAME_MISSING_ERROR)
	private String clientName;

	@NotNull(message = TIME_MISSING_ERROR)
	@Future(message = TIME_IN_PAST_ERROR)
	private LocalDateTime time;

	@Positive(message = PRICE_INVALID_ERROR)
	@DecimalMax(value = "99999.99", message = PRICE_INVALID_ERROR)
	private double price;

	@NotNull(message = STATUS_MISSING_ERROR)
	private AppStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public AppStatus getStatus() {
		return status;
	}

	public void setStatus(AppStatus status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Appointment that = (Appointment) o;
		return id == that.id &&
				Double.compare(that.price, price) == 0 &&
				Objects.equals(clientName, that.clientName) &&
				Objects.equals(time, that.time) &&
				status == that.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, clientName, time, price, status);
	}
}
