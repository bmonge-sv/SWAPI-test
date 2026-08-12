package com.swapi.tests;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.swapi.framework.core.BaseEndpoint;
import com.swapi.framework.core.HttpStatus;
import com.swapi.framework.endpoints.FilmEndpoint;
import com.swapi.framework.models.Film;
import com.swapi.framework.reporting.Story;

import io.restassured.response.Response;

@Story("Films")
public class FilmTest extends AbstractResourceTest {

    private final FilmEndpoint films = new FilmEndpoint();
    private Film film;
    private Response response;

    @Override protected BaseEndpoint endpoint()     { return films; }
    @Override protected int          validId()      { return 1; }
    @Override protected String       resourceName() { return "films"; }

    @BeforeClass(groups = "films")
    public void fetchFilm() {
        response = films.getById(validId());
        assertEquals(response.getStatusCode(), HttpStatus.OK.code(),
                "films/" + validId() + " must return 200 before field assertions can run");
        film = response.as(Film.class);
    }

    @Story("Returning JSON format")
    @Test(groups = "films", description = "GET by id deserializes into the Film model")
    public void getById_deserializesToFilm() {
        assertNotNull(film, "Film model must not be null");
    }

    @Story("Validation of Title")
    @Test(groups = "films, happy", description = "title matches swapi.properties")
    public void verifyTitle() {
        assertEquals(film.getTitle(), SwapiProperties.filmsTitle());
    }

    @Story("Validation of Episode id matches with Film")
    @Test(groups = "films, happy, performance", description = "episode_id matches swapi.properties")
    public void verifyEpisodeId() {
        assertEquals(film.getEpisodeId(), SwapiProperties.filmsEpisodeId());
    }

    @Story("Validation of the Film Director")
    @Test(groups = "films", description = "director matches swapi.properties")
    public void verifyDirector() {
        assertEquals(film.getDirector(), SwapiProperties.filmsDirector());
    }

    @Story("Validation of the Film producer")
    @Test(groups = "films", description = "producer matches swapi.properties")
    public void verifyProducer() {
        assertEquals(film.getProducer(), SwapiProperties.filmsProducer());
    }

    @Story("Validation of the Film release date")
    @Test(groups = "films", description = "release_date matches swapi.properties")
    public void verifyReleaseDate() {
        assertEquals(film.getReleaseDate(), SwapiProperties.filmsReleaseDate());
    }

    @Story("Validation of the Film All fields")
    @Test(groups = "films", description = "response body fields validated with inline Hamcrest matchers")
    public void verifyResponseBody() {
        response.then()
                .statusCode(HttpStatus.OK.code())
                .body("title",        equalTo(SwapiProperties.filmsTitle()))
                .body("episode_id",   equalTo(SwapiProperties.filmsEpisodeId()))
                .body("director",     equalTo(SwapiProperties.filmsDirector()))
                .body("producer",     equalTo(SwapiProperties.filmsProducer()))
                .body("release_date", equalTo(SwapiProperties.filmsReleaseDate()))
                .body("characters",   not(empty()))
                .body("planets",      not(empty()))
                .body("opening_crawl", notNullValue());
    }
}
