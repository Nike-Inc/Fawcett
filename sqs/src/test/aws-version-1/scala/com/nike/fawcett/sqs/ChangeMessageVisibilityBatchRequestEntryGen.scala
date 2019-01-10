package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchRequestEntry
import org.scalacheck._
import Arbitrary.arbitrary
import Gen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object ChangeMessageVisibilityBatchRequestEntryGen {
  implicit val cogenChangeMessageVisibilityBatchRequestEntry: Cogen[ChangeMessageVisibilityBatchRequestEntry] =
    Cogen.tuple3[String, String, Int].contramap(request => (
      request.getId,
      request.getReceiptHandle,
      request.getVisibilityTimeout
    ))

  val genChangeMessageVisibilityBatchRequestEntry = for {
    id <- arbitrary[String]
    receiptHandle <- arbitrary[String]
    visibilityTimeout <- choose(1, 100)
  } yield {
    new ChangeMessageVisibilityBatchRequestEntry()
      .withId(id)
      .withReceiptHandle(receiptHandle)
      .withVisibilityTimeout(visibilityTimeout)
  }

  implicit val arbitraryChangeMessageVisibilityBatchRequestEntry = Arbitrary(genChangeMessageVisibilityBatchRequestEntry)
}
