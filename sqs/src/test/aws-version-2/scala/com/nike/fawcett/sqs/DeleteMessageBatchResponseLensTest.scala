package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.LensTests
import org.typelevel.discipline.Laws
import BatchResultErrorEntryLens._
import DeleteMessageBatchResponseLens._
import DeleteMessageBatchResultEntryLens._
import org.typelevel.discipline.scalatest.Discipline
import software.amazon.awssdk.services.sqs.model.{BatchResultErrorEntry, DeleteMessageBatchResultEntry}
import org.scalacheck._
import Gen._
import BatchResultErrorEntryGen._
import DeleteMessageBatchResultEntryGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class DeleteMessageBatchResponseLensTest extends AnyFunSuite with Matchers with Discipline with Laws {
  import DeleteMessageBatchResponseGen._
  checkAll("failed", LensTests(failed))
  checkAll("sucessful", LensTests(successful))

  checkAll("anyFailedBySender", existsTraversal(
    anyFailedBySender,
    containsCondition = genDeleteMessageBatchResponse(FailuresAndSuccess.includesSpecific[BatchResultErrorEntry, DeleteMessageBatchResultEntry](
      specific = genBatchResultErrorEntry(Some(true)),
      nonSpecific = genBatchResultErrorEntry(),
      min = 1,
      max = 10
    )),
    doesntContainCondition = genDeleteMessageBatchResponse(FailuresAndSuccess.includesSpecific[BatchResultErrorEntry, DeleteMessageBatchResultEntry](
      specific = genBatchResultErrorEntry(Some(false)),
      nonSpecific = genBatchResultErrorEntry(Some(false)),
      min = 1,
      max = 10
    ))))
}
