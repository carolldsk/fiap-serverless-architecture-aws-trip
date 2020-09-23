package br.com.iwe.handler;

import java.io.IOException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.iwe.dao.TripRepository;
import br.com.iwe.model.HandlerRequest;
import br.com.iwe.model.HandlerResponse;
import br.com.iwe.model.Trip;

public class CreateTripRecord implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final TripRepository repository = new TripRepository();

	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {
		Trip trip = null;

		try {
			trip = new ObjectMapper().readValue(request.getBody(), Trip.class);
		} catch (IOException e) {
			return HandlerResponse.builder().setStatusCode(400)
					.setRawBody("There is a error in your Trip! " + e.getMessage()).build();
		}
		try {
			context.getLogger().log("Creating a new trip record for the city " + trip.getCity());
			final Trip tripRecorded = repository.save(trip);
			return HandlerResponse.builder().setStatusCode(201).setObjectBody(tripRecorded).build();
		} catch (Exception e) {
			return HandlerResponse.builder().setStatusCode(400).setRawBody("There is a error. " + e.getMessage())
					.build();
		}
	}

}