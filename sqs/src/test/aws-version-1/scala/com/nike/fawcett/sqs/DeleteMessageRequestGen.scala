package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.DeleteMessageRequest
import org.scalacheck._
import Arbitrary.arbitrary

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object DeleteMessageRequestGen {
  implicit val cogenDeleteMessageRequest: Cogen[DeleteMessageRequest] = Cogen.tuple2[String, String]
    .contramap { request => (
      request.getQueueUrl,
      request.getReceiptHandle
    )}

  val genDeleteMessageRequest = for {
    queueUrl <- arbitrary[String]
    receiptHandle <- arbitrary[String]
  } yield {
    new DeleteMessageRequest()
      .withQueueUrl(queueUrl)
      .withReceiptHandle(receiptHandle)
  }

  implicit val arbitraryDeleteMessageRequest = Arbitrary(genDeleteMessageRequest)
}
