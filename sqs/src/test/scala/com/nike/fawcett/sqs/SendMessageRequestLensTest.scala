package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.LensTests
import SendMessageRequestLens._
import org.typelevel.discipline.scalatest.{Discipline, FunSuiteDiscipline}
import MessageAttributeValueLens._
import MessageSystemAttributeValueLens._
import org.scalatest.prop.Configuration

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class SendMessageRequestLensTestBase extends AnyFunSuite with FunSuiteDiscipline with Matchers with Discipline
  with Configuration {

  import SendMessageRequestGen._
  import MessageSystemAttributeValueGen._

  checkAll("queueUrl", LensTests(queueUrl))
  checkAll("delaySeconds", LensTests(delaySeconds))
  checkAll("messageAttributes", LensTests(messageAttributes))
  checkAll("messageBody", LensTests(messageBody))
  checkAll("messageDeduplicationId", LensTests(messageDeduplicationId))
  checkAll("messageGroupId", LensTests(messageGroupId))
  checkAll("messageSystemAttributes", LensTests(messageSystemAttributes))
}
