package br.com.iwe.handler;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.iwe.dao.TripRepository;
import br.com.iwe.model.HandlerRequest;
import br.com.iwe.model.HandlerResponse;
import br.com.iwe.model.Trip;

public class GetTripsByCountry implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final TripRepository repository = new TripRepository();

	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {

		final String country = request.getPathParameters().get("country");

		context.getLogger().log("Searching for registered trips for " + country);
		
		try {
			final List<Trip> trips = this.repository.findByCountry(country);
			if (trips == null || trips.isEmpty()) {
				return HandlerResponse.builder().setStatusCode(404).build();
			}
			return HandlerResponse.builder().setStatusCode(200).setObjectBody(trips).build();
		} catch (Exception e) {
			return HandlerResponse.builder().setStatusCode(400).setRawBody("There is a error. " + e.getMessage())
					.build();
		}
	}

}
