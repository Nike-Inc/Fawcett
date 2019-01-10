package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.MessageAttributeValue
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

object MessageAttributeValueGen {

  implicit val cogenAttributeTupleForBinary: Cogen[ByteBuffer] =
    Cogen[Array[Byte]].contramap(_.array)

  val binaryValueTupleGen = arbitrary[Array[Byte]].map(ByteBuffer.wrap(_))

  implicit val arbMessageAttributeValueBinary: Arbitrary[ByteBuffer] = Arbitrary(binaryValueTupleGen)

  implicit val messageAttributeValue = Arbitrary(for {
    dataType <- oneOf(BinaryValue, StringValue, NumberValue)
    attribute = new MessageAttributeValue()
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

}
