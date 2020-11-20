package com.nike.fawcett.sqs

import MessageSystemAttributeValueLens._
import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatest.OptionValues
import org.scalatest.funsuite.AnyFunSuite
import monocle.law.discipline.PrismTests
import org.typelevel.discipline.scalatest.{Discipline, FunSuiteDiscipline}
import org.scalatest.prop.Configuration

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

class MessageSystemAttributeValueLensTest extends AnyFunSuite with FunSuiteDiscipline with Matchers with Discipline
  with Configuration with OptionValues {

  import MessageSystemAttributeValueGen._

  checkAll("stringValue", PrismTests(MessageSystemAttributeValueLens.stringValue))
  checkAll("numberValue", PrismTests(MessageSystemAttributeValueLens.numberValue))
  checkAll("binaryValue", PrismTests(MessageSystemAttributeValueLens.binaryValue))
}
