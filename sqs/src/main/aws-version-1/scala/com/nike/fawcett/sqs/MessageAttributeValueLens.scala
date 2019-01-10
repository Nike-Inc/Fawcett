package com.nike.fawcett.sqs

import cats.Eq
import monocle.Prism
import com.amazonaws.services.sqs.model.MessageAttributeValue
import java.nio.ByteBuffer

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

trait MessageAttributeValueLens {

  implicit val messageAttributeValueBinaryTupleEq = Eq.fromUniversalEquals[ByteBuffer]

  implicit val messageAttributeValueEq = Eq.instance[MessageAttributeValue] { case (a, b) =>
    a.getDataType == b.getDataType &&
    (a.getDataType match {
      case "Binary" => a.getBinaryValue.asReadOnlyBuffer.equals(b.getBinaryValue.asReadOnlyBuffer)
      case "String" | "Number" => a.getStringValue == b.getStringValue
    })
  }

  val stringValue = Prism[MessageAttributeValue, String]({ attribute =>
    if (attribute.getDataType == StringValue.key) Some(attribute.getStringValue) else None
  }) { value => new MessageAttributeValue().withDataType(StringValue.key).withStringValue(value) }

  val numberValue = Prism[MessageAttributeValue, String]({ attribute =>
    if (attribute.getDataType == NumberValue.key) Some(attribute.getStringValue) else None
  }) { value => new MessageAttributeValue().withDataType(NumberValue.key).withStringValue(value) }

  val binaryValue = Prism[MessageAttributeValue, ByteBuffer]({ attribute =>
    if (attribute.getDataType == BinaryValue.key) Some(attribute.getBinaryValue) else None
  }) { value => new MessageAttributeValue().withDataType(BinaryValue.key).withBinaryValue(value) }
}

object MessageAttributeValueLens extends MessageAttributeValueLens
