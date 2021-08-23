package com.bar42.todotest.util

import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse

open class Requests {
  val SUCCESSFUL_REQUEST = 200
  val SUCCESSFUL_CREATION = 201
  val SUCCESSFUL_DELETION = 204
  val INCORRECT_INPUT=400
  val ACCESS_DENIED=401
  val NOT_FOUND=404

  fun get(offset: ULong? = null, limit: ULong? = null): ValidatableResponse {
    val req = given()
    if (offset != null) {req.queryParam("offset", offset.toString()) }
    if (limit != null) {req.queryParam("limit", limit.toString()) }
    val ret = req
      .get("todos")
      .then()
    return ret
  }

  public fun create(item: Item): ValidatableResponse {
    val ret = given()
      .contentType(ContentType.JSON)
      .body(item)
      .post("todos")
      .then()
    return ret
  }

  public fun create(body: String): ValidatableResponse {
    val ret = given()
      .contentType(ContentType.JSON)
      .body(body)
      .post("todos")
      .then()
    return ret
  }

  public fun update(newItem: Item): ValidatableResponse {
    val ret = given()
      .auth().preemptive().basic("admin", "admin")
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(newItem)
      .post("todos/${newItem.id}")
      .then()
    return ret
  }

  public fun update(body: String, id: ULong): ValidatableResponse {
    val ret = given()
      .contentType(ContentType.JSON)
      .body(body)
      .post("todos/$id")
      .then()
    return ret
  }

  public fun delete(id: ULong): ValidatableResponse {
    val ret = given()
      .auth().preemptive().basic("admin", "admin")
      .delete("todos/$id")
      .then()
    return ret
  }
}