package es.udc.apm.museos.rest;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import es.udc.apm.museos.model.Token;
import es.udc.apm.museos.model.User;

@Rest(rootUrl = "https://aqueous-atoll-60097.herokuapp.com/api",
      converters = { MappingJackson2HttpMessageConverter.class })
public interface RestClient {
    @Post("/users")
    User userFromGoogleToken(@Body Token token);
}
