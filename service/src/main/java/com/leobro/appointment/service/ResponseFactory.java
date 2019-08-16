package com.leobro.appointment.service;

import java.util.List;

/**
 * Creates the response from the service to its client.
 */
class ResponseFactory {

	private static final String MESSAGE_SERVER_ERROR = "Unexpected server error";
	private static final String MESSAGE_NOT_FOUND = "The appointment is not found";

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
	 * Creates the response, when no appointment can be found in the data storage by the given ID.
	 *
	 * @return The service response with the error message.
	 */
	static ServiceResponse createNotFoundResponse() {
		return new ServiceResponse(ServiceResponse.ResultType.NOT_FOUND, MESSAGE_NOT_FOUND);
	}

	/**
	 * Creates the response about some unexpected error in the service.
	 *
	 * @return The service response telling about an error on the server side.
	 */
	static ServiceResponse createFatalResponse() {
		return new ServiceResponse(ServiceResponse.ResultType.FATAL, MESSAGE_SERVER_ERROR);
	}
}
