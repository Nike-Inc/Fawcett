package com.nike.fawcett.sqs

import cats.Eq
import monocle.Prism
import software.amazon.awssdk.services.sqs.model.{MessageSystemAttributeValue, MessageSystemAttributeNameForSends}
import software.amazon.awssdk.core.SdkBytes

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

trait MessageSystemAttributeValueLens {

  implicit val messageSystemAttributeNameForSendsMapEq = Eq.fromUniversalEquals[Map[MessageSystemAttributeNameForSends, MessageSystemAttributeValue]]

  implicit val messageSystemAttributeValueBinaryTupleEq = Eq.fromUniversalEquals[SdkBytes]

  implicit val messageSystemAttributeValueEq = Eq.instance[MessageSystemAttributeValue] { case (a, b) =>
    a.dataType == b.dataType &&
    (a.dataType match {
      case "Binary" => a.binaryValue.asByteBuffer.asReadOnlyBuffer.equals(b.binaryValue.asByteBuffer.asReadOnlyBuffer)
      case "String" | "Number" => a.stringValue == b.stringValue
    })
  }

  val stringValue = Prism[MessageSystemAttributeValue, String]({ attribute =>
    if (attribute.dataType == StringValue.key) Some(attribute.stringValue) else None
  }) { value => MessageSystemAttributeValue.builder.dataType(StringValue.key).stringValue(value).build }

  val numberValue = Prism[MessageSystemAttributeValue, String]({ attribute =>
    if (attribute.dataType == NumberValue.key) Some(attribute.stringValue) else None
  }) { value => MessageSystemAttributeValue.builder.dataType(NumberValue.key).stringValue(value).build }

  val binaryValue = Prism[MessageSystemAttributeValue, SdkBytes]({ attribute =>
    if (attribute.dataType == BinaryValue.key) Some(attribute.binaryValue) else None
  }) { value => MessageSystemAttributeValue.builder.dataType(BinaryValue.key).binaryValue(value).build }
}

object MessageSystemAttributeValueLens extends MessageSystemAttributeValueLens
