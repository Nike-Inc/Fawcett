package com.nike.fawcett.sqs

import cats.implicits._
import org.scalatest.{ FunSuite, Matchers }
import monocle.law.discipline.LensTests
import ChangeMessageVisibilityBatchResultEntryLens._
import org.typelevel.discipline.scalatest.Discipline

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class ChangeMessageVisibilityBatchResultEntryLensTest extends FunSuite with Matchers with Discipline {
  import ChangeMessageVisibilityBatchResultEntryGen._

  checkAll("id", LensTests(id))
}
