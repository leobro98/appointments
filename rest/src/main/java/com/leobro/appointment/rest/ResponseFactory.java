package com.leobro.appointment.rest;

import com.leobro.appointment.service.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Creates different kinds of Web service responses including the HTTP status and the payload.
 */
class ResponseFactory {

	/**
	 * Creates the {@link ResponseEntity} with a status corresponding to the result of the request.
	 *
	 * @param serviceResponse response from the internal service with different result types.
	 * @return The response with appropriate HTTP status and the body made of the internal service
	 * response's payload.
	 */
	static ResponseEntity<?> createResponse(ServiceResponse serviceResponse) {
		Object payload = serviceResponse.getPayload();

		if (isOk(serviceResponse)) {
			return ResponseEntity.ok()
					.body(payload);
		} else if (isValidationError(serviceResponse)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(payload);
		} else if (isNotFoundError(serviceResponse)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(payload);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(payload);
	}

	private static boolean isOk(ServiceResponse serviceResponse) {
		return serviceResponse.getResult() == ServiceResponse.ResultType.OK;
	}

	private static boolean isValidationError(ServiceResponse serviceResponse) {
		return serviceResponse.getResult() == ServiceResponse.ResultType.ERROR;
	}

	private static boolean isNotFoundError(ServiceResponse serviceResponse) {
		return serviceResponse.getResult() == ServiceResponse.ResultType.NOT_FOUND;
	}
}
