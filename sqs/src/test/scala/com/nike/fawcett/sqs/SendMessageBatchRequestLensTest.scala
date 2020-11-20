package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.{LensTests, TraversalTests}
import SendMessageBatchRequestLens._
import SendMessageBatchRequestEntryLens._
import org.typelevel.discipline.scalatest.{Discipline, FunSuiteDiscipline}
import org.scalatest.prop.Configuration
import SendMessageBatchRequestEntryGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class SendMessageBatchRequestLensTest extends AnyFunSuite with FunSuiteDiscipline with Matchers with Discipline
  with Configuration {

  import SendMessageBatchRequestGen._

  checkAll("queueUrl", LensTests(queueUrl))
  checkAll("entries", LensTests(entries))

  checkAll("allMessageDeduplicationIds", TraversalTests(allMessageDeduplicationIds))
  checkAll("allMessageGroupIds", TraversalTests(allMessageGroupIds))
  checkAll("allMessageBodies", TraversalTests(allMessageBodies))
  checkAll("allIds", TraversalTests(allIds))
}
