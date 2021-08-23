package com.bar42.todotest.test

import com.bar42.todotest.framework.BusinessLogic
import com.bar42.todotest.framework.Item
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class Read: BusinessLogic() {
  private val item1 = Item(41, "Existing item", false)
  private val item2 = Item(42, "Existing item", false)
  private val item3 = Item(43, "Existing item", false)
  private val item4 = Item(44, "Existing item", false)

  @BeforeEach
  fun `get predictable data`() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    `clear all items`()
    `should have item`(item1)
    `should have item`(item2)
    `should have item`(item3)
    `should have item`(item4)
  }

  @Test
  fun `returns added items`() {
    val list = listAllItems()
    assertEquals(4, list.size)
    `check that item is correct`(item1)
    `check that item is correct`(item2)
    `check that item is correct`(item3)
    `check that item is correct`(item4)
  }

  @Test
  fun `Check limit`() {
    val list = listAllItems(limit = 2UL)

    assertEquals(2, list.size)
    assertTrue(list.any { it.id == item1.id })
    assertTrue(list.any { it.id == item2.id })
    assertFalse(list.any { it.id == item3.id })
    assertFalse(list.any { it.id == item4.id })
  }

  @Test
  fun `Check offset`() {
    val list = listAllItems(offset = 2UL)

    assertEquals(2, list.size)
    assertFalse(list.any { it.id == item1.id })
    assertFalse(list.any { it.id == item2.id })
    assertTrue(list.any { it.id == item3.id })
    assertTrue(list.any { it.id == item4.id })
  }

  @Test
  fun `Check limit and offset`() {
    val list = listAllItems(limit = 2UL, offset = 1UL)

    assertEquals(2, list.size)
    assertFalse(list.any { it.id == item1.id })
    assertTrue(list.any { it.id == item2.id })
    assertTrue(list.any { it.id == item3.id })
    assertFalse(list.any { it.id == item4.id })
  }

  @Test
  fun `Empty list with limit 0`() {
    val body = get(limit = 0UL).statusCode(SUCCESSFUL_REQUEST).extract().body().asString()

    assertEquals("[]", body)
  }

  @Test
  fun `Empty list with offset more than count`() {
    val body = get(offset = 4UL).statusCode(SUCCESSFUL_REQUEST).extract().body().asString()

    assertEquals("[]", body)
  }

  @Test
  fun `Empty list with no results`() {
    `clear all items`()

    val body = get().statusCode(SUCCESSFUL_REQUEST).extract().body().asString()

    assertEquals("[]", body)
  }

  @Test
  fun `all items with offset 0`() {
    val list = listAllItems(offset = 0UL)
    assertEquals(4, list.size)
    `check that item is correct`(item1)
    `check that item is correct`(item2)
    `check that item is correct`(item3)
    `check that item is correct`(item4)
  }

  @Test
  fun `Empty list with maximum offset`() {
    val body = get(offset = 18446744073709551615UL).statusCode(SUCCESSFUL_REQUEST).extract().body().asString()

    assertEquals("[]", body)
  }

  @Test
  fun `all items with maximum limit`() {
    val list = listAllItems(limit = 18446744073709551615UL)
    assertEquals(4, list.size)
    `check that item is correct`(item1)
    `check that item is correct`(item2)
    `check that item is correct`(item3)
    `check that item is correct`(item4)
  }

  @Test
  fun `Empty list with maximum offset and limit`() {
    val body = get(offset = 18446744073709551615UL, limit = 18446744073709551615UL)
      .statusCode(SUCCESSFUL_REQUEST).extract().body().asString()

    assertEquals("[]", body)
  }

  @Test
  fun `Error on negative limit`() {
    given()
      .queryParam("limit", "-1").get("todos")
      .then().statusCode(INCORRECT_INPUT)
  }

  @Test
  fun `Error on negative offset`() {
    given()
      .queryParam("offset", "-1").get("todos")
      .then().statusCode(INCORRECT_INPUT)
  }

  @Test
  fun `Error on more than maximum limit`() {
    given()
      .queryParam("limit", "18446744073709551616").get("todos")
      .then().statusCode(INCORRECT_INPUT)
  }

  @Test
  fun `Error on more than maximum offset`() {
    given()
      .queryParam("offset", "18446744073709551616").get("todos")
      .then().statusCode(INCORRECT_INPUT)
  }
}