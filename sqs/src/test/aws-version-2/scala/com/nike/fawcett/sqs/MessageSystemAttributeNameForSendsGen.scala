package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.{MessageSystemAttributeValue, MessageSystemAttributeNameForSends}
import org.scalacheck._
import Gen._
import Arbitrary.arbitrary
import MessageSystemAttributeValueGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

trait MessageSystemAttributeNameForSendsGen {
  implicit val cogenMessageSystemAttributeNameForSends: Cogen[MessageSystemAttributeNameForSends] = Cogen[String].contramap(_.toString)

  val genMesssageSystemAttributeNameForSends: Gen[MessageSystemAttributeNameForSends] =
    oneOf(MessageSystemAttributeNameForSends.values.filter(_.toString != "null").toList)

  implicit val arbitraryMessageSystemAttributeNameForSends = Arbitrary(genMesssageSystemAttributeNameForSends)

  val genMesssageSystemAttributeNameForSendsMap: Gen[Map[MessageSystemAttributeNameForSends, MessageSystemAttributeValue]] =
    mapOf(genMesssageSystemAttributeNameForSendsPair)

  val genMesssageSystemAttributeNameForSendsPair: Gen[(MessageSystemAttributeNameForSends, MessageSystemAttributeValue)] = for {
    attribute <- arbitrary[MessageSystemAttributeNameForSends]
    value <- arbitrary[MessageSystemAttributeValue]
  } yield {
    (attribute, value)
  }

  implicit val arbitraryMessageSystemAttributeNameForSendsPair = Arbitrary(genMesssageSystemAttributeNameForSendsPair)
}

object MessageSystemAttributeNameForSendsGen extends MessageSystemAttributeNameForSendsGen
