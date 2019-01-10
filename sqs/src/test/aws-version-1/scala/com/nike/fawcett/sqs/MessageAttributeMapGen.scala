package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.MessageAttributeValue
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
  def attributeMapEntryGen: Gen[(String, MessageAttributeValue)] = for {
    name <- arbitrary[String]
    attribute <- arbitrary[MessageAttributeValue]
  } yield {
    name -> attribute
  }

  implicit val arbitraryMessageAttributeMap = Arbitrary(mapOf(attributeMapEntryGen))

  implicit val cogenMapStringMessageAttributeValue: Cogen[Map[String, MessageAttributeValue]] =
    Cogen[List[String]].contramap(_.keys.toList)
}
