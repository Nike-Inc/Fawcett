package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry
import org.scalacheck._
import Arbitrary.arbitrary

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object DeleteMessageBatchRequestEntryGen {
  val genDeleteMessageBatchRequestEntry = for {
    id <- arbitrary[String]
    receiptHandle <- arbitrary[String]
  } yield {
    DeleteMessageBatchRequestEntry.builder
      .id(id)
      .receiptHandle(receiptHandle)
      .build
  }

  implicit val arbitraryDeleteMessageBatchRequestEntry = Arbitrary(genDeleteMessageBatchRequestEntry)
}
