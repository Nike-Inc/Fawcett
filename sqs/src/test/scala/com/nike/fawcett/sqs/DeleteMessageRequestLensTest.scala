package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.LensTests
import DeleteMessageRequestLens._
import org.typelevel.discipline.scalatest.{Discipline, FunSuiteDiscipline}
import org.scalatest.prop.Configuration

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class DeleteMessageRequestLensTest extends AnyFunSuite with FunSuiteDiscipline with Matchers with Discipline
  with Configuration {

  import DeleteMessageRequestGen._

  checkAll("queueUrl", LensTests(queueUrl))
  checkAll("receiptHandle", LensTests(receiptHandle))
}
