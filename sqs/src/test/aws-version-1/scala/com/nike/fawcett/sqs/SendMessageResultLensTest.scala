package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.LensTests
import SendMessageResultLens._
import org.typelevel.discipline.scalatest.Discipline

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class SendMessageResultLensTest extends AnyFunSuite with Matchers with Discipline {
  import SendMessageResultGen._

  checkAll("md5OfMessageAttributes", LensTests(md5OfMessageAttributes))
  checkAll("md5OfMessageBody", LensTests(md5OfMessageBody))
  checkAll("messageId", LensTests(messageId))
  checkAll("sequenceNumber", LensTests(sequenceNumber))
  checkAll("md5OfMessageSystemAttributes", LensTests(md5OfMessageSystemAttributes))
}
