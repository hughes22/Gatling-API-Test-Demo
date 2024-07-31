package api

import io.gatling.core.Predef._
import  io.gatling.http.Predef._

class APIDemo extends Simulation{

  //Create Section for protocol
  val httpProtocol = http.baseUrl("https://reqres.in")

  //Scenerio
val getUser = scenario("Get API demo")
  .exec(
    http("Get Single User")
  .get("/api/users/2")
      .check(status.is(200))
      .check(jsonPath
      ("$.data.first_name").is("Janet"))
      .check(jsonPath("$.support.text").is("To keep ReqRes free, contributions towards server costs are appreciated!"))
).pause(2)

  val createUser = scenario("Post API demo")
    .exec(
      http("Create User")
      .post("/api/users")
      .asJson
        //fetching the user data fro the resource
       // .body(RawFileBody("data/user.json"))
      .body(StringBody(
        """
          |{
          |    "name": "Peter",
          |    "job": "leader"
          |}
          |""".stripMargin))
          .asJson
      .check(status.is(201))
      .check( jsonPath("$.name").is("Peter"))
  ).pause(2)

  val updateUser = scenario("Put API demo").exec(
    http("update User")
      .put("/api/users/2")
      //fetching the user data fro the resource
      .body(RawFileBody("data/user.json")).asJson
      .check(
        status.is(200)

        //jsonPath("$job").is("leader")
      )
      .check(jsonPath("$.name").is("Peter"))
  ).pause(2)

  val deleteUser = scenario("Delete API demo")
    .exec(
      http("deleteUser")
        .delete("/api/users/2")
        .check(status.is(204))

  )
//Setup
setUp(
  getUser.inject(atOnceUsers(12)),
  createUser.inject(atOnceUsers(7)),
  updateUser.inject(atOnceUsers(5)),
  deleteUser.inject(atOnceUsers(12))
).protocols(httpProtocol)
}
