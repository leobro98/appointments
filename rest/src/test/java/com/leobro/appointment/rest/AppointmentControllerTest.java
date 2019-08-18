package com.leobro.appointment.rest;

import com.leobro.appointment.service.Appointment;
import com.leobro.appointment.service.AppointmentService;
import com.leobro.appointment.service.ServiceResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {

	private static final String APPOINTMENTS_URL = "/appointments";
	private static final String SCHEDULE_URL = "/schedule";
	private static final String APPOINTMENTS_ID_URL = APPOINTMENTS_URL + "/{id}";

	private static final String QUANTITY_PARAMETER = "quantity";
	private static final String START_DATE_PARAMETER = "startdate";
	private static final String END_DATE_PARAMETER = "enddate";
	private static final String LOCATION_HEADER = "Location";
	private static final String APPOINTMENTS_LOCATION = "http://localhost" + APPOINTMENTS_URL + "/";

	private static final long ID = 123;
	private static final String APPOINTMENT_JSON = "{\n" +
			"  \"clientName\": \"Silvester Stallone\",\n" +
			"  \"time\": \"2019-08-20T17:00\",\n" +
			"  \"price\": 9.99,\n" +
			"  \"status\": \"WAIT\"\n" +
			"}";
	private static final int NUMBER_APPOINTMENTS_CREATED = 5;

	private static final String APP_CLIENT_NAME = "Julia Roberts";
	private static final LocalDateTime APP_TIME = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
			.withHour(12).withMinute(11).withSecond(22);
	private static final double APP_PRICE = 30.0;
	private static final Appointment.AppStatus APP_STATUS = Appointment.AppStatus.WAIT;
	private static final String APP_STATUS_JSON = "\"" + APP_STATUS.name() + "\"";

	private static final String MESSAGE_SERVICE_ERROR = "Service error";
	private static final String MESSAGE_VALIDATION_ERROR = "Validation error";
	private static final String MESSAGE_NOT_FOUND = "Resource not found";

	private static final String ID_PATH = "id";
	private static final String CLIENT_NAME_PATH = "clientName";
	private static final String TIME_PATH = "time";
	private static final String PRICE_PATH = "price";
	private static final String STATUS_PATH = "status";

	@MockBean
	private AppointmentService service;

	@Autowired
	private MockMvc mvc;

	private Appointment appointment;

	@Before
	public void setUp() {
		appointment = createAppointment();
	}

	private static Appointment createAppointment() {
		Appointment app = new Appointment();

		app.setId(ID);
		app.setClientName(APP_CLIENT_NAME);
		app.setTime(APP_TIME);
		app.setPrice(APP_PRICE);
		app.setStatus(APP_STATUS);

		return app;
	}

	@Test
	public void when_createAppointment_then_statusCreatedAndValuesFromService() throws Exception {
		given(service.createAppointment(any(Appointment.class)))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.CREATED, ID));
		mvc.perform(
				MockMvcRequestBuilders.post(APPOINTMENTS_URL)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(APPOINTMENT_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(header().string(LOCATION_HEADER,
						containsString(APPOINTMENTS_LOCATION)))
				.andExpect(jsonPath("$", is((int) ID)));
	}

	@Test
	public void when_createAppointment_andException_then_statusServerError() throws Exception {
		given(service.createAppointment(any(Appointment.class)))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.FATAL, MESSAGE_SERVICE_ERROR));
		mvc.perform(
				MockMvcRequestBuilders.post(APPOINTMENTS_URL)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(APPOINTMENT_JSON))
				.andExpect(status().is5xxServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().string(MESSAGE_SERVICE_ERROR));
	}

	@Test
	public void when_createRandomAppointments_then_statusOkAndValuesFromService() throws Exception {
		given(service.createRandomAppointments(anyInt(), any(LocalDate.class)))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.OK, NUMBER_APPOINTMENTS_CREATED));
		mvc.perform(
				MockMvcRequestBuilders.post(SCHEDULE_URL)
						.accept(MediaType.APPLICATION_JSON)
						.param(QUANTITY_PARAMETER, String.valueOf(NUMBER_APPOINTMENTS_CREATED))
						.param(END_DATE_PARAMETER, LocalDate.now().plusDays(1).toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", is(NUMBER_APPOINTMENTS_CREATED)));
	}

	@Test
	public void when_createRandomAppointments_andValidationError_then_statusBadRequest() throws Exception {
		given(service.createRandomAppointments(anyInt(), any(LocalDate.class)))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.ERROR, MESSAGE_VALIDATION_ERROR));
		mvc.perform(
				MockMvcRequestBuilders.post(SCHEDULE_URL)
						.accept(MediaType.APPLICATION_JSON)
						.param(QUANTITY_PARAMETER, String.valueOf(NUMBER_APPOINTMENTS_CREATED))
						.param(END_DATE_PARAMETER, LocalDate.now().plusDays(1).toString()))
				.andExpect(status().is4xxClientError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().string(MESSAGE_VALIDATION_ERROR));
	}

	@Test
	public void when_getAppointment_then_statusOkAndValuesFromService() throws Exception {
		given(service.getAppointment(anyLong()))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.OK, appointment));
		mvc.perform(
				MockMvcRequestBuilders.get(APPOINTMENTS_ID_URL, ID)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$." + ID_PATH, is((int) ID)))
				.andExpect(jsonPath("$." + CLIENT_NAME_PATH, is(APP_CLIENT_NAME)))
				.andExpect(jsonPath("$." + TIME_PATH, is(APP_TIME.toString())))
				.andExpect(jsonPath("$." + PRICE_PATH, is(APP_PRICE)))
				.andExpect(jsonPath("$." + STATUS_PATH, is(APP_STATUS.name())));
	}

	@Test
	public void when_getAppointment_andNotFound_then_statusNotFound() throws Exception {
		given(service.getAppointment(anyLong()))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.NOT_FOUND, MESSAGE_NOT_FOUND));
		mvc.perform(
				MockMvcRequestBuilders.get(APPOINTMENTS_ID_URL, ID)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().string(MESSAGE_NOT_FOUND));
	}

	@Test
	public void when_getAllAppointments_then_statusOkAndValuesFromService() throws Exception {
		List<Appointment> appointments = Collections.singletonList(appointment);
		String now = LocalDate.now().toString();
		String tomorrow = LocalDate.now().plusDays(1).toString();

		given(service.getAllAppointments(any(LocalDate.class), any(LocalDate.class)))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.OK, appointments));
		mvc.perform(
				MockMvcRequestBuilders.get(APPOINTMENTS_URL)
						.accept(MediaType.APPLICATION_JSON)
						.param(START_DATE_PARAMETER, now)
						.param(END_DATE_PARAMETER, tomorrow))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0]."+ ID_PATH,is((int) ID)))
				.andExpect(jsonPath("$[0]."+ CLIENT_NAME_PATH, is(APP_CLIENT_NAME)))
				.andExpect(jsonPath("$[0]."+ TIME_PATH, is(APP_TIME.toString())))
				.andExpect(jsonPath("$[0]."+ PRICE_PATH, is(APP_PRICE)))
				.andExpect(jsonPath("$[0]."+ STATUS_PATH, is(APP_STATUS.name())));
	}

	@Test
	public void when_updateAppointmentStatus_then_statusOkAndValuesFromService() throws Exception {
		given(service.updateAppointmentStatus(anyLong(), any(Appointment.AppStatus.class)))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.OK, null));
		mvc.perform(
				MockMvcRequestBuilders.put(APPOINTMENTS_ID_URL, ID)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(APP_STATUS_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(isEmptyOrNullString()));
	}

	@Test
	public void when_deleteAppointment_then_statusOkAndValuesFromService() throws Exception {
		given(service.deleteAppointment(anyLong()))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.OK, appointment));
		mvc.perform(
				MockMvcRequestBuilders.delete(APPOINTMENTS_ID_URL, ID)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$." + ID_PATH, is((int) ID)))
				.andExpect(jsonPath("$." + CLIENT_NAME_PATH, is(APP_CLIENT_NAME)))
				.andExpect(jsonPath("$." + TIME_PATH, is(APP_TIME.toString())))
				.andExpect(jsonPath("$." + PRICE_PATH, is(APP_PRICE)))
				.andExpect(jsonPath("$." + STATUS_PATH, is(APP_STATUS.name())));
	}
}
