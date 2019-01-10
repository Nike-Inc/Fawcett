package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.{ FunSuite, Matchers }
import monocle.law.discipline.LensTests
import SendMessageRequestLens._
import org.typelevel.discipline.scalatest.Discipline
import MessageAttributeValueLens._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class SendMessageRequestLensTest extends FunSuite with Matchers with Discipline {
  import SendMessageRequestGen._

  checkAll("queueUrl", LensTests(queueUrl))
  checkAll("delaySeconds", LensTests(delaySeconds))
  checkAll("messageAttributes", LensTests(messageAttributes))
  checkAll("messageBody", LensTests(messageBody))
  checkAll("messageDeduplicationId", LensTests(messageDeduplicationId))
  checkAll("messageGroupId", LensTests(messageGroupId))
}
