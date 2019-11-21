package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.{LensTests, TraversalTests}
import ReceiveMessageResultLens._
import org.typelevel.discipline.scalatest.Discipline
import MessageGen._
import MessageLens._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class ReceiveMessageResultLensTest extends AnyFunSuite with Matchers with Discipline {
  import ReceiveMessageResultGen._

  checkAll("messages", LensTests(messages))
  checkAll("allBodies", TraversalTests(allBodies))
}
