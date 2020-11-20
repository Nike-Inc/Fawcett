package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.LensTests
import SendMessageBatchRequestEntryLens._
import org.typelevel.discipline.scalatest.{Discipline, FunSuiteDiscipline}
import org.scalatest.prop.Configuration
import MessageAttributeValueLens._
import MessageSystemAttributeValueLens._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class SendMessageBatchRequestEntryLensTestBase extends AnyFunSuite with FunSuiteDiscipline with Matchers
  with Discipline with Configuration {

  import SendMessageBatchRequestEntryGen._
  import MessageSystemAttributeValueGen._

  checkAll("delaySeconds", LensTests(delaySeconds))
  checkAll("id", LensTests(id))
  checkAll("messageAttributes", LensTests(messageAttributes))
  checkAll("messgeBody", LensTests(messageBody))
  checkAll("messageDeduplicationId", LensTests(messageDeduplicationId))
  checkAll("messageGroupId", LensTests(messageGroupId))
  checkAll("messageSystemAttributes", LensTests(messageSystemAttributes))
}
