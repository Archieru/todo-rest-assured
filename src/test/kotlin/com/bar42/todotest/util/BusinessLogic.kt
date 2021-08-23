package com.bar42.todotest.util

import org.junit.jupiter.api.Assertions.*

open class BusinessLogic: Requests() {
  fun `should have item`(item: Item) {
    if (!`item is correct`(item)) {
      if (`item exists`(item)) { delete(item.id) }
      create(item).statusCode(SUCCESSFUL_CREATION)
    }
  }

  fun `should not have item`(item: Item) {
    if (`item exists`(item)) {
      delete(item.id).statusCode(SUCCESSFUL_DELETION)
    }
  }

  fun `should not have item`(text: String) {
    val foundItem = `find item by text`(text)
    if (foundItem != null) {
      delete(foundItem.id).statusCode(SUCCESSFUL_DELETION)
    }
  }

  fun `clear all items`() {
    for (item in listAllItems()) {
      delete(item.id)
    }
  }

  fun `find item by text`(text: String): Item? {
    return listAllItems().filter { it.text == text }.getOrNull(0)
  }

  fun `get item`(item: Item) : Item {
    return tryGetItem(item)!!
  }

  fun `item exists`(item: Item) : Boolean {
    return tryGetItem(item) != null
  }

  fun `item is correct`(item: Item) : Boolean {
    val actual = tryGetItem(item) ?: return false
    return item == actual
  }

  fun `check that item exists`(item: Item) {
    assertTrue(
      `item exists`(item),
      "Item <$item> should be in ${listAllItems()}")
  }

  fun `check that item does not exist`(item: Item) {
    assertFalse(
      `item exists`(item),
      "Item <$item> should not be in ${listAllItems()}")
  }

  fun `check that item does not exist`(text: String) {
    assertNull(
      `find item by text`(text),
      "Item <$text> should not be in ${listAllItems()}")
  }

  fun `check that item does not exist`(negativeItem: NegativeItem) {
    `check that item does not exist`(negativeItem.texts!!)
  }

  fun `check that item is correct`(item: Item) {
    val actual = tryGetItem(item)
    assertNotNull(actual, "Item <$item> should be in ${listAllItems()}")
    assertEquals(item.id, actual!!.id)
    assertEquals(item.text, actual.text)
    assertEquals(item.completed, actual.completed)
  }

  fun listAllItems(offset: ULong? = null, limit: ULong? = null): MutableList<Item> {
    return get(offset, limit).statusCode(SUCCESSFUL_REQUEST).extract().body().jsonPath()
      .getList(".", Item().javaClass)!!
  }

  private fun tryGetItem(item: Item) : Item? {
    return listAllItems().filter { it.id == item.id }.getOrNull(0)
  }
}
