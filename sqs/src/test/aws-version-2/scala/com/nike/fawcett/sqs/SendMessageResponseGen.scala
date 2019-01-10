package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.SendMessageResponse
import org.scalacheck._
import Arbitrary.arbitrary

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object SendMessageResponseGen {
  implicit val cogenSendMessageResponse: Cogen[SendMessageResponse] = Cogen.tuple4[String, String, String, String]
    .contramap { result => (
      result.md5OfMessageAttributes,
      result.md5OfMessageBody,
      result.messageId,
      result.sequenceNumber
    )}

  val genSendMessageResponse = for {
    md5OfMessageAttributes <- arbitrary[String]
    md5OfBody <- arbitrary[String]
    messageId <- arbitrary[String]
    sequenceNumber <- arbitrary[String]
  } yield {
    SendMessageResponse.builder
      .md5OfMessageAttributes(md5OfMessageAttributes)
      .md5OfMessageBody(md5OfBody)
      .messageId(messageId)
      .sequenceNumber(sequenceNumber)
      .build
  }

  implicit val arbitrarySendMessageResponse = Arbitrary(genSendMessageResponse)
}
