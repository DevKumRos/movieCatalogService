package com.kumar;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.kumar.model.CatalogItem;
import com.kumar.model.Movie;
import com.kumar.model.Rating;
import com.kumar.model.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogRestService {
	
	   @Autowired
	    private RestTemplate restTemplate;
	   
	   @Autowired
	   private MovieCatalogFacade movieCatalogFacade;

	    /*@Autowired
	    WebClient.Builder webClientBuilder;*/

	    @RequestMapping("/{userId}")
	    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) throws InterruptedException {
	    	System.out.println("getCatalog --> "+userId);
	        UserRating userRating = movieCatalogFacade.getUserRating(userId);

	        return userRating.getRatings().stream()
	                .map(rating -> {
	                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
	                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	                })
	                .collect(Collectors.toList());

	    }
	    
	    

}

/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/