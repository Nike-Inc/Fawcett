package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.MessageAttributeValue
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

object MessageAttributeValueGen {

  implicit val cogenSdkBytes: Cogen[SdkBytes] = Cogen[Array[Byte]].contramap(_.asByteArray)

  implicit val arbitrarySdkBytes: Arbitrary[SdkBytes] =
    Arbitrary(nonEmptyContainerOf[Array, Byte](arbitrary[Byte]).map(SdkBytes.fromByteArray))

  implicit val messageAttributeValue = Arbitrary(for {
    dataType <- oneOf(BinaryValue, StringValue, NumberValue)
    attribute = MessageAttributeValue.builder
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
