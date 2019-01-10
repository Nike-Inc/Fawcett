package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.SendMessageBatchResultEntry
import org.scalacheck._
import Gen._
import Arbitrary.arbitrary

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object SendMessageBatchResultEntryGen {
  implicit val cogenSendMessageBatchResultEntry: Cogen[SendMessageBatchResultEntry] =
    Cogen.tuple5[String, String, String, String, String].contramap { entry =>
      (entry.id,
       entry.md5OfMessageAttributes,
       entry.md5OfMessageBody,
       entry.messageId,
       entry.sequenceNumber)
    }

  val genSendMessageBatchResultEntry =
    for {
      id <- identifier(0, 80, '_', '-')
      md5MessageAttributes <- arbitrary[String]
      md5MessageBody <- arbitrary[String]
      messageId <- identifier(0, 80, '_', '-')
      sequenceNumber <- numStr
    } yield {
      SendMessageBatchResultEntry.builder
        .id(id)
        .md5OfMessageAttributes(md5MessageAttributes)
        .md5OfMessageBody(md5MessageBody)
        .messageId(messageId)
        .sequenceNumber(sequenceNumber)
        .build
    }

  implicit val arbitrarySendMessageBatchResultEntryLens = Arbitrary(genSendMessageBatchResultEntry)
}
