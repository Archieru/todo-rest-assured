package com.bar42.todotest.framework

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.io.IOException

@JsonSerialize(using = ItemSerializer::class)
@JsonDeserialize(using = ItemDeserializer::class)
class Item {
  var id: ULong = 0U
  var text: String = ""
  var completed: Boolean = false

  constructor(id: ULong, text: String, completed: Boolean) {
    this.id = id
    this.text = text
    this.completed = completed
  }

  constructor(id: Int, text: String, completed: Boolean) {
    this.id = id.toULong()
    this.text = text
    this.completed = completed
  }

  constructor()
}

class ItemSerializer @JvmOverloads constructor(t: Class<Item>? = null) : StdSerializer<Item>(t) {
  @Throws(IOException::class, JsonProcessingException::class)
  override fun serialize(
    value: Item, jgen: JsonGenerator?, provider: SerializerProvider?
  ) {
    jgen!!.writeStartObject()
    jgen.writeNumberField("id", value.id.toString().toBigInteger())
    jgen.writeStringField("text", value.text)
    jgen.writeBooleanField("completed", value.completed)
    jgen.writeEndObject()
  }
}

class ItemDeserializer @JvmOverloads constructor(vc: Class<*>? = null) : StdDeserializer<Item?>(vc) {
  @Throws(IOException::class, JsonProcessingException::class)
  override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): Item {
    val node: JsonNode = jp.getCodec().readTree(jp)
    val id = node["id"].bigIntegerValue().toString().toULong()
    val text = node["text"].asText()
    val completed = node["completed"].booleanValue()
    return Item(id, text, completed)
  }
}