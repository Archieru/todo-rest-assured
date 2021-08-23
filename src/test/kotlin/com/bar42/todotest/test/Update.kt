package com.bar42.todotest.test

import com.bar42.todotest.framework.BusinessLogic
import com.bar42.todotest.framework.Item
import com.bar42.todotest.framework.NegativeItem
import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class Update: BusinessLogic() {
  @Test
  fun `can complete the item`() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    val initial = Item(21, "Buy milk", false)
    `should have item`(initial)

    val expected = Item(21, "Buy milk", true)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can uncomplete the item`() {
    val initial = Item(22, "Buy bread", true)
    `should have item`(initial)

    val expected = Item(22, "Buy bread", false)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can change the uncompleted topic`() {
    val initial = Item(23, "Buy sugar", false)
    `should have item`(initial)

    val expected = Item(23, "Buy salt", false)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can change the completed topic`() {
    val initial = Item(24, "Buy pepper", true)
    `should have item`(initial)

    val expected = Item(24, "Buy spices", false)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can change plans for item`() {
    val initial = Item(25, "Buy ginger", false)
    `should have item`(initial)

    val expected = Item(25, "Buy garlic", true)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can create a new item instead of completed one`() {
    val initial = Item(26, "Buy cinnamon", true)
    `should have item`(initial)

    val expected = Item(26, "Buy cumin", false)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can complete an item without text`() {
    val initial = Item(27, "", false)
    `should have item`(initial)

    val expected = Item(27, "", true)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can update an item text from empty`() {
    val initial = Item(28, "", false)
    `should have item`(initial)

    val expected = Item(28, "Now we have text", false)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can remove item text`() {
    val initial = Item(29, "We had text before", false)
    `should have item`(initial)

    val expected = Item(29, "", false)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can update an item with id 0`() {
    val initial = Item(0, "Zero ID", false)
    `should have item`(initial)

    val expected = Item(0, "Zero ID", false)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can add an item with maximum id`() {
    val initial = Item(18446744073709551615UL, "Buy glove", false)
    `should have item`(initial)

    val expected = Item(18446744073709551615UL, "Buy glove", false)
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)
  }

  @Test
  fun `can edit item id`() {
    val initial = Item(91, "Change ID", false)
    `should have item`(initial)

    val expected = NegativeItem("92", "Change ID", "false")
    update(expected.build(), initial.id).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected.toItem())
  }

  @Test
  fun `can not create not existing item`() {
    val notExisting = Item(96, "Not existing item", true)
    `should not have item`(notExisting)

    update(notExisting).statusCode(INCORRECT_INPUT)

    `check that item does not exist`(notExisting)
  }

  @Test
  fun `can update an item with a name more than 1000`() {
    val initial = Item(id = 90, completed = false,
      text = "Lorem ipsum, 1914 translation by H. Rackham: On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains.")
    `should have item`(initial)

    val expected = Item(id = 90, completed = false,
      text = "Changed item, 1914 translation by H. Rackham: On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains.")
    update(expected).statusCode(SUCCESSFUL_CREATION)

    `check that item is correct`(expected)

    delete(expected.id)
  }

  @Test
  fun `can not change an item without id parameter`() {
    val toEdit = NegativeItem(null, "Without id", "false")

    update(toEdit.build(), 93UL).statusCode(INCORRECT_INPUT)

    `check that item does not exist`(toEdit)
  }

  @Test
  fun `can not change an item without text parameter`() {
    val initial = Item(97, "Has some text", false)
    val unexpected = NegativeItem("97", null, "false")
    `should have item`(initial)

    update(unexpected.build(), initial.id).statusCode(INCORRECT_INPUT)

    `check that item is correct`(initial)
  }

  @Test
  fun `can not change an item without completed parameter`() {
    val initial = Item(98, "Text befor change", false)
    val unexpected = NegativeItem("97", "Text after change without completion status", null)
    `should have item`(initial)

    update(unexpected.build(), initial.id).statusCode(INCORRECT_INPUT)

    `check that item is correct`(initial)
    `check that item does not exist`(unexpected)
  }

  @Test
  fun `can not overwrite existing item id`() {
    val initial = Item(34, "Change ID", false)
    val existing = Item(35, "Existing item", true)
    `should have item`(initial)
    `should have item`(existing)

    val unexpected = NegativeItem("92", "Change ID", "false")
    update(unexpected.build(), existing.id).statusCode(INCORRECT_INPUT)

    `check that item is correct`(initial)
    `check that item is correct`(existing)
  }

  @Test
  fun `can not set negative ID to item`() {
    val initial = Item(36, "Change ID", false)
    `should have item`(initial)

    val unexpected = NegativeItem("-1", "Item with negative ID", "false")
    update(unexpected.build(), initial.id).statusCode(INCORRECT_INPUT)

    `check that item is correct`(initial)
    `check that item does not exist`(unexpected)
  }

  @Test
  fun `can not set ID more than maximum to item`() {
    val initial = Item(37, "Change ID", false)
    `should have item`(initial)

    val unexpected = NegativeItem("18446744073709551616", "Item with ID  more than maximum", "false")
    update(unexpected.build(), initial.id).statusCode(INCORRECT_INPUT)

    `check that item is correct`(initial)
    `check that item does not exist`(unexpected)
  }

  @Test
  fun `Receive an error updating with empty body`() {
    val initial = Item(38, "Change ID", false)
    `should have item`(initial)
    update("", initial.id).statusCode(INCORRECT_INPUT)

    `check that item is correct`(initial)
  }
}