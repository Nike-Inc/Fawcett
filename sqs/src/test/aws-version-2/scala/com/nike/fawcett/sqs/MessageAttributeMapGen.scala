package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.MessageAttributeValue
import org.scalacheck._
import Gen._
import Arbitrary.arbitrary
import MessageAttributeValueGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

trait MessageAttributeMapGen {
  val attributeMapEntryGen: Gen[(String, MessageAttributeValue)] = for {
    attribute <- arbitrary[MessageAttributeValue]
    name <- identifier(1, 100, '-', '_', '.')
  } yield {
    name -> attribute
  }

  implicit val arbitraryMessageAttributeMap = Arbitrary(mapOf(attributeMapEntryGen))

  implicit val cogenMessageAttributeValue: Cogen[MessageAttributeValue] = Cogen[String].contramap(_.toString)
}
