package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.LensTests
import ChangeMessageVisibilityBatchResponseLens._
import ChangeMessageVisibilityBatchResultEntryLens._
import BatchResultErrorEntryLens._
import org.typelevel.discipline.scalatest.Discipline
import software.amazon.awssdk.services.sqs.model.{BatchResultErrorEntry, ChangeMessageVisibilityBatchResultEntry}
import org.scalacheck._
import Gen._
import BatchResultErrorEntryGen._
import ChangeMessageVisibilityBatchResultEntryGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class ChangeMessageVisibilityBatchResponseLensTest extends AnyFunSuite with Matchers with Discipline {
  import ChangeMessageVisibilityBatchResponseGen._

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
