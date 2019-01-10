package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.BatchResultErrorEntry
import org.scalacheck._
import Arbitrary.arbitrary

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object BatchResultErrorEntryGen {
  implicit val cogenBatchResultErrorEntry: Cogen[BatchResultErrorEntry] =
    Cogen.tuple4[String, String, String, Boolean].contramap { entry =>
      (entry.code, entry.id, entry.message, entry.senderFault)
    }

  def genBatchResultErrorEntry(guarenteeSenderFault: Option[Boolean] = None) = for {
    code <- arbitrary[String]
    id <- identifier(0, 80, '_', '-')
    message <- arbitrary[String]
    senderFault <- arbitrary[Boolean]
  } yield {
    // for some reason senderFault is Any if we do this inline
    val fault = guarenteeSenderFault.getOrElse(senderFault)
    BatchResultErrorEntry.builder
      .code(code)
      .id(id)
      .message(message)
      .senderFault(fault)
      .build
  }

  implicit val arbitraryBatchResultErrorEntry = Arbitrary(genBatchResultErrorEntry())
}
