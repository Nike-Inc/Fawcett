package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.{QueueAttributeName, ReceiveMessageRequest}
import org.scalacheck._
import Arbitrary.arbitrary
import Gen._
import scala.collection.JavaConverters._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object ReceiveMessageRequestGen {

  implicit val cogenReceiveMessageRequest: Cogen[ReceiveMessageRequest] =
    Cogen.tuple7[List[QueueAttributeName], Int, List[String], String, String, Int, Int].contramap {
      request => (
        request.attributeNames.asScala.toList,
        request.maxNumberOfMessages,
        request.messageAttributeNames.asScala.toList,
        request.queueUrl,
        request.receiveRequestAttemptId,
        request.visibilityTimeout,
        request.waitTimeSeconds
      )}

  val genReceiveMessageRequest = for {
    attributeNames <- arbitrary[List[QueueAttributeName]]
    maxNumberOfMessages <- choose(1, 10)
    messageAttributeNames <- arbitrary[List[String]]
    queueUrl <- arbitrary[String]
    receiveRequestAttemptId <- arbitrary[String]
    visibilityTimeout <- choose(0, 100)
    waitTimeSeconds <- choose(0, 100)
  } yield {
    ReceiveMessageRequest.builder
      .attributeNames(attributeNames.asJava)
      .maxNumberOfMessages(maxNumberOfMessages)
      .messageAttributeNames(messageAttributeNames.asJava)
      .queueUrl(queueUrl)
      .receiveRequestAttemptId(receiveRequestAttemptId)
      .visibilityTimeout(visibilityTimeout)
      .waitTimeSeconds(waitTimeSeconds)
      .build
  }

  implicit val arbitraryReceiveMessageRequest = Arbitrary(genReceiveMessageRequest)
}
