package com.kumar;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.kumar.model.Rating;
import com.kumar.model.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class MovieCatalogFacade {

	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "getDefaultUserRating")
	public UserRating getUserRating(String userId) throws InterruptedException {
		//TO get fallback working remove comment on line 22 
		//Thread.sleep(10000);
		System.out.println("getUserRating --> "+userId);

		return restTemplate.getForObject("http://movie-rating-service/ratingsdata/user/" + userId, UserRating.class);
	}

	public UserRating  getDefaultUserRating(String userId) {
		System.out.println("fallbackMethod getUserRating --> "+userId);
		UserRating rating = new UserRating();
		rating.setUserId(userId);
		rating.setRatings(Arrays.asList(
				new Rating("100", 1),
				new Rating("200", 1)
				));
		return rating;
	}


}
