package Camp;

import Camp.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.Argument;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class CountryTest {
Cookies cookies;
    @BeforeClass
    public void loginCampus() {
        baseURI = "https://demo.mersys.io/";

        Map<String, String> credential = new HashMap<>();

//        {"username": "richfield.edu","password": "Richfield2020!","rememberMe": "true"}


        credential.put("username", "richfield.edu");
        credential.put("password", "Richfield2020!");
        credential.put("rememberMe", "true");

            cookies=
        given()
                .contentType(ContentType.JSON)
                .body(credential)

                .when()
                .post("auth/login")

                .then()
                .log().cookies()
                .statusCode(200)
                .extract().response().getDetailedCookies()


        ;


    }

    public String getRandomName()
    {
        return RandomStringUtils.randomAlphabetic(8);
    }
    public String getRandomCode()
    {
        return RandomStringUtils.randomAlphabetic(6).toLowerCase();
    }



    String countryID;
    String countryName;
    String countryCode;


    @Test
    public void createCountry(){

        countryName=getRandomName();
        countryCode=getRandomCode();
        Country country=new Country();
        country.setName(countryName);   // generateCountryName
        country.setCode(countryCode);      // generateCountryCode

        countryID=
        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)




                .when()
                .post("school-service/api/countries")


                .then()
                .statusCode(201)
                .log().body()
                .extract().jsonPath().getString("id")



        ;







    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative(){


        Country country=new Country();
        country.setName(countryName);   // generateCountryName
        country.setCode(countryCode);      // generateCountryCode


                given()

                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(country)




                        .when()
                        .post("school-service/api/countries")


                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message",equalTo("The Country with Name \""+countryName+"\" already exists."))




        ;







    }

    @Test(dependsOnMethods = "createCountry")
    public void createUpdate(){
        countryName=getRandomName();

        Country country=new Country();
        country.setId(countryID);
        country.setName(countryName);   // generateCountryName
        country.setCode(countryCode);      // generateCountryCode


        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)




                .when()
                .put("school-service/api/countries")


                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(countryName))




        ;







    }
    @Test(dependsOnMethods = "createUpdate")
    public void deleteCountryById(){


        given()

                .cookies(cookies)
                .pathParam("countryID",countryID)




                .when()
                .delete("school-service/api/countries/{countryID}")


                .then()
                .log().body()
                .statusCode(200)





        ;







    }

    @Test(dependsOnMethods = "deleteCountryById")
    public void deleteCountryByIdNegative(){


        given()

                .cookies(cookies)
                .pathParam("countryID",countryID)




                .when()
                .delete("school-service/api/countries/{countryID}")


                .then()
                .log().body()
                .statusCode(400)





        ;







    }

    @Test(dependsOnMethods = "deleteCountryById")
    public void createUpdateNegative(){
        countryName=getRandomName();

        Country country=new Country();
        country.setId(countryID);
        country.setName(countryName);
        country.setCode(countryCode);


        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)




                .when()
                .put("school-service/api/countries")


                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Country not found"))





        ;







    }


}
