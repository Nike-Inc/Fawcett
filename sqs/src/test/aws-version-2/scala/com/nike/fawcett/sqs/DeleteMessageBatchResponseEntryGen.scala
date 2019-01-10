package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchResultEntry
import org.scalacheck._
import Arbitrary.arbitrary

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object DeleteMessageBatchResponseEntryGen {

  implicit val cogenDeleteMessageBatchResponseEntry: Cogen[DeleteMessageBatchResultEntry] =
    Cogen[String].contramap(_.id)

  val genDeleteMessageBatchResultEntry = for {
    id <- arbitrary[String]
  } yield {
    DeleteMessageBatchResultEntry.builder
      .id(id)
      .build
  }

  implicit val arbitraryDeleteMessageBatchResultEntry = Arbitrary(genDeleteMessageBatchResultEntry)
}
