package com.nike.fawcett.sqs

import cats.Eq
import monocle.Prism
import com.amazonaws.services.sqs.model.MessageSystemAttributeValue
import java.nio.ByteBuffer

trait MessageSystemAttributeValueLens {
  implicit val messageSystemAttributeValueBinaryEq = Eq.fromUniversalEquals[ByteBuffer]
  implicit val messageSystemAttributeValueMapEq = Eq.fromUniversalEquals[Map[String, MessageSystemAttributeValue]]

  implicit val messageSystemAttributeValueEq = Eq.instance[MessageSystemAttributeValue] { case (a, b) =>
    a.getDataType == b.getDataType &&
    (a.getDataType match {
      case "Binary" => a.getBinaryValue.asReadOnlyBuffer.equals(b.getBinaryValue.asReadOnlyBuffer)
      case "String" | "Number" => a.getStringValue == b.getStringValue
    })
  }

  val stringValue = Prism[MessageSystemAttributeValue, String]({ attribute =>
    if (attribute.getDataType == StringValue.key) Some(attribute.getStringValue) else None
  }) { value => new MessageSystemAttributeValue().withDataType(StringValue.key).withStringValue(value) }

  val numberValue = Prism[MessageSystemAttributeValue, String]({ attribute =>
    if (attribute.getDataType == NumberValue.key) Some(attribute.getStringValue) else None
  }) { value => new MessageSystemAttributeValue().withDataType(NumberValue.key).withStringValue(value) }

  val binaryValue = Prism[MessageSystemAttributeValue, ByteBuffer]({ attribute =>
    if (attribute.getDataType == BinaryValue.key) Some(attribute.getBinaryValue) else None
  }) { value => new MessageSystemAttributeValue().withDataType(BinaryValue.key).withBinaryValue(value) }
}

object MessageSystemAttributeValueLens extends MessageSystemAttributeValueLens
