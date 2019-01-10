package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.ReceiveMessageRequest
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
    Cogen.tuple7[List[String], Int, List[String], String, String, Int, Int].contramap {
      request => (
        request.getAttributeNames.asScala.toList,
        request.getMaxNumberOfMessages,
        request.getMessageAttributeNames.asScala.toList,
        request.getQueueUrl,
        request.getReceiveRequestAttemptId,
        request.getVisibilityTimeout,
        request.getWaitTimeSeconds
      )}

  val genReceiveMessageRequest = for {
    attributeNames <- arbitrary[List[String]]
    maxNumberOfMessages <- choose(1, 10)
    messageAttributeNames <- arbitrary[List[String]]
    queueUrl <- arbitrary[String]
    receiveRequestAttemptId <- arbitrary[String]
    visibilityTimeout <- choose(0, 100)
    waitTimeSeconds <- choose(0, 100)
  } yield {
    new ReceiveMessageRequest()
      .withAttributeNames(attributeNames.asJava)
      .withMaxNumberOfMessages(maxNumberOfMessages)
      .withMessageAttributeNames(messageAttributeNames.asJava)
      .withQueueUrl(queueUrl)
      .withReceiveRequestAttemptId(receiveRequestAttemptId)
      .withVisibilityTimeout(visibilityTimeout)
      .withWaitTimeSeconds(waitTimeSeconds)
  }

  implicit val arbitraryReceiveMessageRequest = Arbitrary(genReceiveMessageRequest)
}
