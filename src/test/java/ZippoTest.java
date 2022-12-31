import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test() {

        given()
                // hazırlık islemleri (token, send body, parameters)

                .when()
                // link and method verme

                .then()
        // asserton ve verileri ele alma extract
        ;

    }

    @Test
    public void statusCodeTest() {

        given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   //  log.All() all responsu göster
                .statusCode(200);  // status kontrolü
    }

    @Test
    public void contentTypeTest() {

        given()
                .when().get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   //  log.All() all responsu göster
                .statusCode(200)  // status kontrolü
                .contentType(ContentType.JSON); // hatali durum kontrolü yapalım
    }

    @Test
    public void checkStateInResponseBody() {

        given()
                .when().get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country", equalTo("United States")) // body.country == United States ?
                .statusCode(200)
        ;
    }


    @Test
    public void bodyJsonPathtest2() {

        given()
                .when().get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state", equalTo("California")) // body.country == United States ?
                .statusCode(200)
        ;
    }



    @Test
    public void bodyJsonPathtest3() {

        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .body("places.'place name'", hasItem("Çaputçu Köyü")) // bir index verilmezse dizinin bütün elemanları
                .statusCode(200)
        ;
    }




    @Test
    public void bodyArrayHasSizeTest() {

        given()
                .when().get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1))
                .statusCode(200)
        ;
    }






    @Test
    public void combiningTest() {

        given()
                .when().get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1))
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .statusCode(200)
        ;
    }


    @Test
    public void pathParamTest() {

        given()
                .pathParam("Country","us")
                .pathParam("ZipKod",90210)
                .log().uri()

                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipKod}")

                .then()
                .log().body()

                .statusCode(200)
        ;
    }



    @Test
    public void pathParamTest2() {

        for (int i = 90210; i <= 90213; i++) {
            given()
                    .pathParam("Country", "us")
                    .pathParam("ZipKod", 90210)
                    .log().uri()

                    .when()
                    .get("http://api.zippopotam.us/{Country}/{ZipKod}")

                    .then()
                    .log().body()

                    .statusCode(200)
            ;
        }
    }



    @Test
    public void queryParamTest() {


        for (int i = 1; i < 11; i++) {
            given()
                    .param("page", i)

                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))

                    .statusCode(200)
            ;
        }
    }


    @Test
    public void queryParamTest2() {


        for (int i = 1; i < 11; i++) {
            given()
                    .param("page", i)

                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))

                    .statusCode(200)
            ;
        }
    }


    RequestSpecification requestSpecs;
    ResponseSpecification responseSpecs;

    @BeforeClass
    void Setup(){

        baseURI="https://gorest.co.in/public/v1";

        requestSpecs=new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

        responseSpecs=new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();


    }



@Test
public void requestResponseSpecification() {



        given()
                .param("page", 1)

                .spec(requestSpecs)

                .when()
                .get("/users")

                .then()

                .body("meta.pagination.page", equalTo(1))
                .spec(responseSpecs)
        ;

}


    @Test
    public void extractingJsonPath() {


        String placeName=

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                .extract().path("places[0].'place name'") // body.country == United States ?

        ;

        System.out.println("placeName = " + placeName);
    }





    @Test
    public void extractingJsonPathInt() {


int limit=

                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                       // .log().body()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit");


                ;
        System.out.println("limit = " + limit);
        Assert.assertEquals(limit,10,"test sonucu");

    }







    @Test
    public void extractingJsonPathInt2() {


        int id=

                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        // .log().body()
                        .statusCode(200)
                        .extract().path("data[2].id");


        ;
        System.out.println("id = " + id);

    }


    @Test
    public void extractingJsonPathIntList() {


        List<Integer> idler=

                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        // .log().body()
                        .statusCode(200)
                        .extract().path("data.id");  // data daki bütün idleri bir liste şeklinde verir


        ;
        System.out.println("idler = " + idler);
        Assert.assertTrue(idler.contains(3045));

    }



    @Test
    public void extractingJsonPathIntList2() {


        List<String> isimler=

                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        // .log().body()
                        .statusCode(200)
                        .extract().path("data.name");  // data daki bütün idleri bir liste şeklinde verir


        ;
        System.out.println("isimler = " + isimler);
        Assert.assertTrue(isimler.contains("Datta Achari"));

    }

    @Test
    public void extractingJsonPathIntList3() {


        Response response=

                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        // .log().body()
                        .statusCode(200)
                        .extract().response();  // bütün body alındı


        ;
       List<Integer>idler=response.path("data.id");
        List<String>isimler=response.path("data.name");
        int limit =response.path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        System.out.println("isimler = " + isimler);
        System.out.println("idler = " + idler);
    }


    @Test
    public void extractingJsonPOJO() {


        Location yer=


                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().as(Location.class)   // Location şablonu
                              ;
        System.out.println("yer = " + yer);
        System.out.println("yer = " + yer.getCountry());
        System.out.println("yer.getPlaces().get(0).getPlacename() = " + yer.getPlaces().get(0).getPlacename());

    }



































}