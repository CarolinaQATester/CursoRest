package br.com.automacaorest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class OlaMundoTest {

    @Test
    public void testOlaMundo() {
        Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue("O statuscode deveria ser 200", response.statusCode() == 200);
        Assert.assertEquals(200, response.statusCode());

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }

    @Test
    public void devoConhecerOutrasFormaRestAssured() {
        Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        get("https://restapi.wcaquino.me/ola").then().statusCode(200);

        given().//pré-codição
                when(). //metodos get, post,put, delete
                get("https://restapi.wcaquino.me/ola").
                then().//validaçoes
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void devoConhecerMatcherHamcrest() {
        assertThat("Maria", is("Maria"));
        assertThat(128, is(128));
        assertThat(128, isA(Integer.class));
        assertThat(128d, isA(Double.class));
        assertThat(128d, greaterThan(120d));
        assertThat(128d, lessThan(130d));

        List<Integer> impares = Arrays.asList(1, 3,5, 7, 9);
        assertThat(impares, hasSize(5));
        assertThat(impares, contains(1,3,5,7,9));
        assertThat(impares, containsInAnyOrder(1,3,7,5,9));
        assertThat(impares, hasItem(1));
        assertThat(impares, hasItems(1,5));

        assertThat("Maria", is(not("Joao")));
        assertThat("Maria", not("Joao"));
        assertThat("Maria", anyOf(is("Maria"), is("Paula")));
        assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));

    }
    @Test
    public void devoValidarOBody(){

        given().
                when().
                get("https://restapi.wcaquino.me/ola").
                then().
                log().all().
                statusCode(200).
                body(is(notNullValue())).
                body(is("Ola Mundo!")).
                body(containsString("und"));

    }
}
