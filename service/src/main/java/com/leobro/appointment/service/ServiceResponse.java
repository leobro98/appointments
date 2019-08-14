package com.leobro.appointment.service;

/**
 * Internal response from the service to its client. Contains not only the data requested, but also the result type.
 */
public class ServiceResponse {

	private ResultType result;
	private Object payload;

	/**
	 * Shows, how successful was the processing of the request.
	 * <p>OK - the processing was successful,
	 * <p>ERROR - there was a validation error caused by data in the request,
	 * <p>FATAL - unexpected exception occurred during the processing.
	 */
	public enum ResultType {
		OK,
		ERROR,
		FATAL;
	}

	ServiceResponse(ResultType result, Object payload) {
		this.result = result;
		this.payload = payload;
	}

	/**
	 * Shows, how successful was the processing of the request.
	 *
	 * @return One of the enumeration constants.
	 */
	public ResultType getResult() {
		return result;
	}

	/**
	 * The data to be passed to the client.
	 *
	 * @return Any kind of data the client requested.
	 */
	public Object getPayload() {
		return payload;
	}
}
