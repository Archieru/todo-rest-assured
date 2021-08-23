package com.bar42.todotest.test

import com.bar42.todotest.util.BusinessLogic
import com.bar42.todotest.util.Item
import com.bar42.todotest.util.NegativeItem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Create: BusinessLogic() {
  @Test
  fun `can add a non completed item`() {
    val toAdd = Item(11, "Buy milk", false)
    `should not have item`(toAdd)

    create(toAdd).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(toAdd)
  }

  @Test
  fun `can add a completed item`() {
    val toAdd = Item(12, "Buy bread", true)
    `should not have item`(toAdd)

    create(toAdd).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(toAdd)
  }

  @Test
  fun `can add an item without text`() {
    val toAdd = Item(13, "", false)
    `should not have item`(toAdd)

    create(toAdd).statusCode(SUCCESSFUL_CREATION)

    `check that item exists`(toAdd)
    assertEquals("", `get item`(toAdd).text, listAllItems().toString())
    `check that item is correct`(toAdd)
  }

  @Test
  fun `can add an item with id 0`() {
    val toAdd = Item(0, "Zero ID", true)
    `should not have item`(toAdd)

    create(toAdd).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(toAdd)
  }

  @Test
  fun `can add an item with maximum id`() {
    val toAdd = Item(18446744073709551615UL, "Buy glove", false)
    `should not have item`(toAdd)

    create(toAdd).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(toAdd)
  }

  @Test
  fun `can add an item with a name more than 1000`() {
    val toAdd = Item(
      id = 16, completed = false,
      text = "Lorem ipsum, 1914 translation by H. Rackham: On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains.")
    `should not have item`(toAdd)

    create(toAdd).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(toAdd)

    delete(toAdd.id)
  }

  @Test
  fun `can not add an item without id parameter`() {
    val toAdd = NegativeItem(null, "Without id", "false")
    `should not have item`(toAdd.texts!!)

    create(toAdd.build()).statusCode(INCORRECT_INPUT)

    `check that item does not exist`(toAdd)
  }

  @Test
  fun `can not add an item without text parameter`() {
    val toAdd = NegativeItem("17", null, "false")
    val expected = Item(17, "", false)
    `should not have item`(expected)

    create(toAdd.build()).statusCode(INCORRECT_INPUT)

    `check that item does not exist`(expected)
  }

  @Test
  fun `can not add an item without completed parameter`() {
    val toAdd = NegativeItem("14", "Without completed", null)
    val expected = Item(14, "Without completed", false)
    `should not have item`(toAdd.texts!!)

    create(toAdd.build()).statusCode(INCORRECT_INPUT)

    `check that item does not exist`(toAdd)
    `check that item does not exist`(expected)
  }

  @Test
  fun `can not add an item with the same ID`() {
    val expected = Item(15, "Initial ID", false)
    `should have item`(expected)

    val toAdd = Item(15, "Same ID", true)
    create(toAdd).statusCode(INCORRECT_INPUT)

    `check that item is correct`(expected)
  }

  @Test
  fun `can not add an item with negative ID`() {
    val toAdd = NegativeItem("-1", "Negative ID", "false")
    `should not have item`(toAdd.texts!!)

    create(toAdd.build()).statusCode(INCORRECT_INPUT)

    `check that item does not exist`(toAdd)
  }

  @Test
  fun `can not add an item with ID more than maximum`() {
    val toAdd = NegativeItem("18446744073709551616", "More than maximum ID", "false")
    `should not have item`(toAdd.texts!!)

    create(toAdd.build()).statusCode(INCORRECT_INPUT)

    `check that item does not exist`(toAdd)
  }

  @Test
  fun `Receive an error with empty body`() {
    create("").statusCode(INCORRECT_INPUT)
  }
}