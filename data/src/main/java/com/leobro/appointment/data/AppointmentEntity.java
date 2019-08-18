package com.leobro.appointment.data;

import com.leobro.appointment.service.Appointment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data entity for mapping the object to the database table.
 */
@Entity
@Table(name = "appointment")
class AppointmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "client_name")
	private String clientName;

	private LocalDateTime time;
	private double price;

	@Enumerated(EnumType.STRING)
	private Appointment.AppStatus status;

	Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}

	String getClientName() {
		return clientName;
	}

	void setClientName(String clientName) {
		this.clientName = clientName;
	}

	LocalDateTime getTime() {
		return time;
	}

	void setTime(LocalDateTime time) {
		this.time = time;
	}

	double getPrice() {
		return price;
	}

	void setPrice(double price) {
		this.price = price;
	}

	Appointment.AppStatus getStatus() {
		return status;
	}

	void setStatus(Appointment.AppStatus status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AppointmentEntity that = (AppointmentEntity) o;
		return Double.compare(that.price, price) == 0 &&
				Objects.equals(id, that.id) &&
				Objects.equals(clientName, that.clientName) &&
				Objects.equals(time, that.time) &&
				status == that.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, clientName, time, price, status);
	}
}
