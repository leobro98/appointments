package com.leobro.appointment.service;

import java.util.List;

/**
 * Creates the response from the service to its client.
 */
class ResponseFactory {

	static final String MESSAGE_ERROR = "Service error";

	/**
	 * Creates the response of successful processing.
	 *
	 * @param payload the content of the response.
	 * @return The service response from the successful operation.
	 */
	static ServiceResponse createOkResponse(Object payload) {
		return new ServiceResponse(ServiceResponse.ResultType.OK, payload);
	}

	/**
	 * Creates the response from handled errors, usually caused by wrong input data.
	 *
	 * @param errors the explanation of the errors.
	 * @return The service response without a useful results caused by a wrong request.
	 */
	static ServiceResponse createErrorResponse(List<String> errors) {
		return new ServiceResponse(ServiceResponse.ResultType.ERROR, errors);
	}

	/**
	 * Creates the response about some unexpected error in the service.
	 *
	 * @return The service response telling about an error on the service side.
	 */
	static ServiceResponse createFatalResponse() {
		return new ServiceResponse(ServiceResponse.ResultType.FATAL, MESSAGE_ERROR);
	}
}
