package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest
import org.scalacheck._
import Arbitrary.arbitrary
import Gen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object ChangeMessageVisibilityRequestGen {
  implicit val cogenChangeMessageVisibilityRequest: Cogen[ChangeMessageVisibilityRequest] =
    Cogen.tuple3[String, String, Int].contramap(request => (
      request.getQueueUrl,
      request.getReceiptHandle,
      request.getVisibilityTimeout
    ))

  val genChangeMessageVisibilityRequest = for {
    queueUrl <- arbitrary[String]
    receiptHandle <- arbitrary[String]
    visibilityTimeout <- choose(0, 100)
  } yield {
    new ChangeMessageVisibilityRequest()
      .withQueueUrl(queueUrl)
      .withReceiptHandle(receiptHandle)
      .withVisibilityTimeout(visibilityTimeout)
  }

  implicit val arbitraryChangeMessageVisibilityRequest = Arbitrary(genChangeMessageVisibilityRequest)
}
