package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.LensTests
import org.typelevel.discipline.Laws
import org.typelevel.discipline.scalatest.FunSuiteDiscipline
import BatchResultErrorEntryLens._
import DeleteMessageBatchResultLens._
import DeleteMessageBatchResultEntryLens._
import org.typelevel.discipline.scalatest.Discipline
import org.scalatest.prop.Configuration
import com.amazonaws.services.sqs.model.{BatchResultErrorEntry,  DeleteMessageBatchResultEntry}
import BatchResultErrorEntryGen._
import DeleteMessageBatchResultEntryGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class DeleteMessageBatchResultLensTest extends AnyFunSuite with FunSuiteDiscipline with Matchers with Discipline
  with Configuration with Laws {

  import DeleteMessageBatchResultGen._
  checkAll("failed", LensTests(failed))
  checkAll("sucessful", LensTests(successful))

  checkAll("anyFailedBySender", existsTraversal(
    anyFailedBySender,
    containsCondition = genDeleteMessageBatchResult(FailuresAndSuccess.includesSpecific[BatchResultErrorEntry, DeleteMessageBatchResultEntry](
      specific = genBatchResultErrorEntry(Some(true)),
      nonSpecific = genBatchResultErrorEntry(),
      min = 1,
      max = 10
    )),
    doesntContainCondition = genDeleteMessageBatchResult(FailuresAndSuccess.includesSpecific[BatchResultErrorEntry, DeleteMessageBatchResultEntry](
      specific = genBatchResultErrorEntry(Some(false)),
      nonSpecific = genBatchResultErrorEntry(Some(false)),
      min = 1,
      max = 10
    ))))
}
