package com.nike.fawcett.sqs

import cats.Eq
import monocle.Prism
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue
import software.amazon.awssdk.core.SdkBytes

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

trait MessageAttributeValueLens {

  implicit val messageAttributeValueBinaryTupleEq = Eq.fromUniversalEquals[SdkBytes]

  implicit val messageAttributeValueEq = Eq.instance[MessageAttributeValue] { case (a, b) =>
    a.dataType == b.dataType &&
    (a.dataType match {
      case "Binary" => a.binaryValue.asByteBuffer.asReadOnlyBuffer.equals(b.binaryValue.asByteBuffer.asReadOnlyBuffer)
      case "String" | "Number" => a.stringValue == b.stringValue
    })
  }

  val stringValue = Prism[MessageAttributeValue, String]({ attribute =>
    if (attribute.dataType == StringValue.key) Some(attribute.stringValue) else None
  }) { value => MessageAttributeValue.builder.dataType(StringValue.key).stringValue(value).build }

  val numberValue = Prism[MessageAttributeValue, String]({ attribute =>
    if (attribute.dataType == NumberValue.key) Some(attribute.stringValue) else None
  }) { value => MessageAttributeValue.builder.dataType(NumberValue.key).stringValue(value).build }

  val binaryValue = Prism[MessageAttributeValue, SdkBytes]({ attribute =>
    if (attribute.dataType == BinaryValue.key) Some(attribute.binaryValue) else None
  }) { value => MessageAttributeValue.builder.dataType(BinaryValue.key).binaryValue(value).build }
}

object MessageAttributeValueLens extends MessageAttributeValueLens
