package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.ChangeMessageVisibilityRequest
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
      request.queueUrl,
      request.receiptHandle,
      request.visibilityTimeout
    ))

  val genChangeMessageVisibilityRequest = for {
    queueUrl <- arbitrary[String]
    receiptHandle <- arbitrary[String]
    visibilityTimeout <- choose(0, 100)
  } yield {
    ChangeMessageVisibilityRequest.builder
      .queueUrl(queueUrl)
      .receiptHandle(receiptHandle)
      .visibilityTimeout(visibilityTimeout)
      .build
  }

  implicit val arbitraryChangeMessageVisibilityRequest = Arbitrary(genChangeMessageVisibilityRequest)
}
