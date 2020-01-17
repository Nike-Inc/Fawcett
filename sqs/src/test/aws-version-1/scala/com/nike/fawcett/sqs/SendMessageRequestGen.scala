package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.{MessageSystemAttributeValue, SendMessageRequest}
import org.scalacheck._
import Gen._
import Arbitrary.arbitrary
import scala.jdk.CollectionConverters._
import scala.concurrent.duration._
import MessageSystemAttributeMapGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object SendMessageRequestGen {
  implicit val arbitrarySendMessageRequest = Arbitrary {
    for {
      queueUrl <- arbitrary[String]
      delaySeconds <- choose(0, 15.minutes.toSeconds.toInt)
      id <- identifier(0, 80, '_', '-')
      attributes <- mapOf(attributeMapEntryGen)
      messageSystemAttributes <- arbitrary[Map[String, MessageSystemAttributeValue]]
      body <- arbitrary[String]
      deduplicationId <- identifier(0, 128, allValidSymbols :_*)
      groupId <- identifier(0, 128, allValidSymbols :_*)
    } yield {
      new SendMessageRequest()
        .withQueueUrl(queueUrl)
        .withDelaySeconds(delaySeconds)
        .withMessageAttributes(attributes.asJava)
        .withMessageSystemAttributes(messageSystemAttributes.asJava)
        .withMessageBody(body)
        .withMessageDeduplicationId(deduplicationId)
        .withMessageGroupId(groupId)
    }
  }
}
