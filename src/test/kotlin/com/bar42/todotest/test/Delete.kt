package com.bar42.todotest.test

import com.bar42.todotest.util.BusinessLogic
import com.bar42.todotest.util.Item
import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class Delete: BusinessLogic() {
  @Test
  public fun `can delete item`(){
    val expected = Item(50, "Delete me", false)
    `should have item`(expected)

    delete(expected.id).statusCode(SUCCESSFUL_DELETION)

    `check that item does not exist`(expected)
  }

  @Test
  public fun `can not delete non-existent item`(){
    val unexpected = Item(51, "Does not exist", false)
    `should not have item`(unexpected)

    delete(unexpected.id).statusCode(NOT_FOUND)

    `check that item does not exist`(unexpected)
  }

  @Test
  public fun `can delete item with id 0`(){
    val expected = Item(0, "Delete me", false)
    `should have item`(expected)

    delete(expected.id).statusCode(SUCCESSFUL_DELETION)

    `check that item does not exist`(expected)
  }

  @Test
  public fun `can delete item with maximum id`(){
    val expected = Item(18446744073709551615UL, "Delete me", false)
    `should have item`(expected)

    delete(expected.id).statusCode(SUCCESSFUL_DELETION)

    `check that item does not exist`(expected)
  }

  @Test
  public fun `not allowed to delete without authorization`(){
    val expected = Item(53, "Not enough rights", false)
    `should have item`(expected)

    RestAssured.given()
      .delete("todos/${expected.id}")
      .then().statusCode(ACCESS_DENIED)

    `check that item exists`(expected)
  }

  @Test
  public fun `can not delete negative id`(){
    RestAssured.given()
      .delete("todos/-1")
      .then().statusCode(INCORRECT_INPUT)
  }

  @Test
  public fun `can not delete more than maximum id`(){
    RestAssured.given()
      .delete("todos/18446744073709551616")
      .then().statusCode(INCORRECT_INPUT)
  }

  @Test
  fun delete() {
    `clear all items`()
  }
}