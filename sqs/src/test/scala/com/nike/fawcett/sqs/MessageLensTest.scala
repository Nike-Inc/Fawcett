package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.LensTests
import MessageLens._
import MessageAttributeValueLens._
import org.typelevel.discipline.scalatest.Discipline

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class MessageLensTest extends AnyFunSuite with Matchers with Discipline {
  import MessageGen._

  checkAll("attributes", LensTests(attributes))
  checkAll("body", LensTests(body))
  checkAll("md5OfBody", LensTests(md5OfBody))
  checkAll("md5OfMessageAttributes", LensTests(md5OfMessageAttributes))
  checkAll("messageAttributes", LensTests(messageAttributes))
  checkAll("messageId", LensTests(messageId))
  checkAll("receiptHandle", LensTests(receiptHandle))
}

