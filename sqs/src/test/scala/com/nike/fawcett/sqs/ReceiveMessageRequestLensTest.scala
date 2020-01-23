package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.LensTests
import ReceiveMessageRequestLens._
import org.typelevel.discipline.scalatest.Discipline

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class ReceiveMessageRequestLensTest extends AnyFunSuite with Matchers with Discipline {
  import ReceiveMessageRequestGen._

  checkAll("attributeNames", LensTests(attributeNames))
  checkAll("maxNumberOfMessages", LensTests(maxNumberOfMessages))
  checkAll("messageAttributeNames", LensTests(messageAttributeNames))
  checkAll("queueUrl", LensTests(queueUrl))
  checkAll("receiveRequestAttemptId", LensTests(receiveRequestAttemptId))
  checkAll("visibilityTimeout", LensTests(visibilityTimeout))
  checkAll("waitTimeSeconds", LensTests(waitTimeSeconds))
}
