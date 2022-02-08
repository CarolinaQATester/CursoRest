package br.com.automacaorest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {

    @Test
    public void deveVerificarAListaDeUsuarios() {
        given().when().
                get("https://restapi.wcaquino.me/users").
                then().
                log().all().
                statusCode(200);
    }
    @Test
    public void deveVerificarListaNaRaiz() {
        given().when().
                get("https://restapi.wcaquino.me/users").
                then().
                log().all().
                statusCode(200).body("", hasSize(3)).
                body("name", hasItems("João da Silva","Maria Joaquina", "Ana Júlia")).
                body("age[1]", is(25)).
                body("filhos.name", hasItems(Arrays.asList("Zezinho", "Luizinho")));
    }

    @Test
    public void deveVerificarOPrimeiroNivel() {
        given().when().
                get("https://restapi.wcaquino.me/users/1").
                then().
                log().all().
                statusCode(200).
                body(is(notNullValue())).
                body("id", is(1)).
                body("name", containsString("ilv")).
                body("age", greaterThan(18));
    }

    @Test
    public void deveVerificarOPrimeiroNivelOutrasFormas() {
        Response response = RestAssured.request(Method.GET,"https://restapi.wcaquino.me/users/1");
        //path
        Assert.assertEquals(new Integer(1), response.path("id"));
        Assert.assertEquals(new Integer(1), response.path("%s","id"));
        //jsonpath
        JsonPath jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(1, jsonPath.getInt("id"));
        //from
        int id = JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1, id);
    }
    @Test
    public void deveVerificarOSegundoNivel() {
        given().when().
                get("https://restapi.wcaquino.me/users/2").
                then().
                log().all().
                statusCode(200).
                body(is(notNullValue())).
                body("id", is(2)).
                body("name", containsString("Maria Joaquina")).
                body("age", greaterThan(18)).
                body("endereco.rua", is("Rua dos bobos")).
                body("endereco.numero", is(0));
    }
    @Test
    public void deveVerificarUmaLista() {
        given().when().
                get("https://restapi.wcaquino.me/users/3").
                then().
                log().all().
                statusCode(200).
                body(is(notNullValue())).
                body("id", is(3)).
                body("name", containsString("Ana Júlia")).
                body("age", greaterThan(18)).
                body("filhos", hasSize(2)).
                body("filhos[0].name", is("Zezinho")).
                body("filhos[1].name", is("Luizinho")).
                body("filhos.name", hasItems("Zezinho", "Luizinho"));
    }
    @Test
    public void deveRetornarErroDeUsuarioInexistente() {
        given().when().
                get("https://restapi.wcaquino.me/users/4").
                then().
                log().all().
                statusCode(404).body("error", is("Usuário inexistente"));
    }


}
