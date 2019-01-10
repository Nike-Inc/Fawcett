package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
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
      request.queueUrl,
      request.receiptHandle
    )}

  val genDeleteMessageRequest = for {
    queueUrl <- arbitrary[String]
    receiptHandle <- arbitrary[String]
  } yield {
    DeleteMessageRequest.builder
      .queueUrl(queueUrl)
      .receiptHandle(receiptHandle)
      .build
  }

  implicit val arbitraryDeleteMessageRequest = Arbitrary(genDeleteMessageRequest)
}
