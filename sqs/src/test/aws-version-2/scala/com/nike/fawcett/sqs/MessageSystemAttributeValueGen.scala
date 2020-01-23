package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeValue
import software.amazon.awssdk.core.SdkBytes
import org.scalacheck._
import org.scalacheck.Gen._
import Arbitrary.arbitrary

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object MessageSystemAttributeValueGen {

  implicit val cogenMessageSystemAttributeValueGen: Cogen[MessageSystemAttributeValue] =
    Cogen[(String, Array[Byte])].contramap { value =>
      val dataType = value.dataType
      val bytes = dataType match {
        case "String" | "Number" => value.stringValue.getBytes
        case _ => value.binaryValue.asByteArray
      }
      dataType -> bytes
    }

  implicit val messageSystemAttributeValue = Arbitrary(for {
    dataType <- oneOf(BinaryValue, StringValue, NumberValue)
    attribute = MessageSystemAttributeValue.builder
      .dataType(dataType.key)
    stringValue <- arbitrary[String]
    numberValue <- arbitrary[Double]
    binaryValue <- arbitrary[SdkBytes]
  } yield {
    dataType match {
      case StringValue => attribute.stringValue(stringValue).build
      case NumberValue => attribute.stringValue(numberValue.toString).build
      case BinaryValue => attribute.binaryValue(binaryValue).build
    }
  })
}
