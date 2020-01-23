package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.SendMessageResult
import org.scalacheck._
import Arbitrary.arbitrary

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object SendMessageResultGen {
  implicit val cogenSendMessageResult: Cogen[SendMessageResult] = Cogen.tuple5[String, String, String, String, String]
    .contramap { result => (
      result.getMD5OfMessageAttributes,
      result.getMD5OfMessageSystemAttributes,
      result.getMD5OfMessageBody,
      result.getMessageId,
      result.getSequenceNumber
    )}

  val genSendMessageResult = for {
    md5OfMessageAttributes <- arbitrary[String]
    md5OfMessageSystemAttributes <- arbitrary[String]
    md5OfBody <- arbitrary[String]
    messageId <- arbitrary[String]
    sequenceNumber <- arbitrary[String]
  } yield {
    new SendMessageResult()
      .withMD5OfMessageAttributes(md5OfMessageAttributes)
      .withMD5OfMessageSystemAttributes(md5OfMessageSystemAttributes)
      .withMD5OfMessageBody(md5OfBody)
      .withMessageId(messageId)
      .withSequenceNumber(sequenceNumber)
  }

  implicit val arbitrarySendMessageResult = Arbitrary(genSendMessageResult)
}
