package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.typelevel.discipline.scalatest.FunSuiteDiscipline
import monocle.law.discipline.LensTests
import ChangeMessageVisibilityBatchResultLens._
import ChangeMessageVisibilityBatchResultEntryLens._
import BatchResultErrorEntryLens._
import org.typelevel.discipline.scalatest.Discipline
import org.scalatest.prop.Configuration
import com.amazonaws.services.sqs.model.{BatchResultErrorEntry, ChangeMessageVisibilityBatchResultEntry}
import BatchResultErrorEntryGen._
import ChangeMessageVisibilityBatchResultEntryGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class ChangeMessageVisibilityBatchResultLensTest extends AnyFunSuite with FunSuiteDiscipline with Matchers
  with Discipline with Configuration {

  import ChangeMessageVisibilityBatchResultGen._

  checkAll("failed", LensTests(failed))
  checkAll("successful", LensTests(successful))

  checkAll("anyFailedBySender", existsTraversal(
    anyFailedBySender,
    containsCondition = genChangeMessageVisibilityBatchResult(FailuresAndSuccess.includesSpecific[BatchResultErrorEntry, ChangeMessageVisibilityBatchResultEntry](
      specific = genBatchResultErrorEntry(Some(true)),
      nonSpecific = genBatchResultErrorEntry(),
      min = 1,
      max = 10
    )),
    doesntContainCondition = genChangeMessageVisibilityBatchResult(FailuresAndSuccess.includesSpecific[BatchResultErrorEntry, ChangeMessageVisibilityBatchResultEntry](
      specific = genBatchResultErrorEntry(Some(false)),
      nonSpecific = genBatchResultErrorEntry(Some(false)),
      min = 1,
      max = 10
    ))))
}
