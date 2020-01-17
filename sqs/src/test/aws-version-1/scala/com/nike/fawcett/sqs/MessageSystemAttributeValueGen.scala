package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.MessageSystemAttributeValue
import org.scalacheck._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary.arbitrary
import java.nio.ByteBuffer

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

trait MessageSystemAttributeValueGen {

  implicit val cogenAttributeTupleForBinary: Cogen[ByteBuffer] = Cogen[Array[Byte]].contramap(_.array)

  implicit val cogenMessageSystemAttributeValueGen: Cogen[MessageSystemAttributeValue] =
    Cogen[(String, Array[Byte])].contramap { value =>
      val dataType = value.getDataType
      val bytes = dataType match {
        case "String" | "Number" => value.getStringValue.getBytes
        case _ => value.getBinaryValue.array
      }
      dataType -> bytes
    }

  val binaryValueTupleGen = arbitrary[Array[Byte]].map(ByteBuffer.wrap(_))

  implicit val arbMessageAttributeValueBinary: Arbitrary[ByteBuffer] = Arbitrary(binaryValueTupleGen)

  implicit val messageSystemAttributeValue = Arbitrary(for {
    dataType <- oneOf(BinaryValue, StringValue, NumberValue)
    attribute = new MessageSystemAttributeValue()
      .withDataType(dataType.key)
    stringValue <- alphaStr
    numberValue <- arbitrary[Double]
    binaryValue <- arbitrary[ByteBuffer]
  } yield {
    dataType match {
      case StringValue => attribute.withStringValue(stringValue)
      case NumberValue => attribute.withStringValue(numberValue.toString)
      case BinaryValue => attribute.withBinaryValue(binaryValue)
    }
  })

 val genMessageSystemAttributePair: Gen[(String, MessageSystemAttributeValue)] =
    for {
      attribute <- arbitrary[MessageSystemAttributeValue]
      name <- arbitrary[String]
    } yield {
      (name, attribute)
    }

  implicit val genMapStringMessageSystemAttribute: Gen[Map[String, MessageSystemAttributeValue]] =
    mapOf(genMessageSystemAttributePair)

}

object MessageSystemAttributeValueGen extends MessageSystemAttributeValueGen
