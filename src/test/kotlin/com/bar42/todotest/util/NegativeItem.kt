package com.bar42.todotest.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

data class NegativeItem(var ids: String?, var texts: String?, var completeds: String?) {
  fun build(): String {
    var ret = "{"
    if (ids!=null) { ret+= """"id":$ids,"""}
    if (texts!=null) { ret+= """"text":"$texts","""}
    if (completeds!=null) { ret+= """"completed":$completeds,"""}
    if (ret[ret.length - 1] == ',') ret = ret.substring(0, ret.length - 1)
    ret += "}"
    return ret
  }

  fun toItem(newId: Int? = null, newText: String? = null, newCompleted: Boolean? = null): Item {
    val toId = newId ?: (ids?.toInt() ?: 0)
    val toText = newText ?: texts ?: ""
    val toCompleted = newCompleted ?: (completeds == "true")
    return Item(toId, toText, toCompleted)
  }

  class NegativeItemTest {
    @Test
    fun empty() {
      Assertions.assertEquals(NegativeItem(null, null, null).build(), "{}")
    }

    @Test
    fun id() {
      Assertions.assertEquals(NegativeItem("42", null, null).build(), "{\"id\":42}")
    }

    @Test
    fun text() {
      Assertions.assertEquals(NegativeItem(null, "some text", null).build(), "{\"text\":\"some text\"}")
    }

    @Test
    fun completed() {
      Assertions.assertEquals(NegativeItem(null, null, "true").build(), "{\"completed\":true}")
    }

    @Test
    fun positive() {
      Assertions.assertEquals(
        NegativeItem("11", "other text", "false").build(),
        "{\"id\":11,\"text\":\"other text\",\"completed\":false}"
      )
    }
  }}