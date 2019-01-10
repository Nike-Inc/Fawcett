package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.DeleteMessageBatchResultEntry
import org.scalacheck._
import Arbitrary.arbitrary

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object DeleteMessageBatchResultEntryGen {

  implicit val cogenDeleteMessageBatchResultEntry: Cogen[DeleteMessageBatchResultEntry] =
    Cogen[String].contramap(_.getId)

  val genDeleteMessageBatchResultEntry = for {
    id <- arbitrary[String]
  } yield {
    new DeleteMessageBatchResultEntry()
      .withId(id)
  }

  implicit val arbitraryDeleteMessageBatchResultEntry = Arbitrary(genDeleteMessageBatchResultEntry)
}
