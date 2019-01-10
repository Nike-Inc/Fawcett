package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName
import org.scalacheck._
import Gen._
import scala.collection.JavaConverters._
import Arbitrary.arbitrary

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

trait MessageSystemAttributeNameGen {
  implicit val cogenMessageSystemAttributeName: Cogen[MessageSystemAttributeName] = Cogen[String].contramap(_.toString)

  val genMesssageSystemAttributeName: Gen[MessageSystemAttributeName] =
    oneOf(MessageSystemAttributeName.knownValues.asScala.toList)

  implicit val arbitraryMessageSystemAttributeName = Arbitrary(genMesssageSystemAttributeName)

  val genMesssageSystemAttributeNameMap: Gen[Map[MessageSystemAttributeName, String]] =
    mapOf(genMesssageSystemAttributeNamePair)

  val genMesssageSystemAttributeNamePair: Gen[(MessageSystemAttributeName, String)] = for {
    attribute <- oneOf(MessageSystemAttributeName.knownValues.asScala.toList)
    value <- arbitrary[String]
  } yield {
    (attribute, value)
  }

  implicit val arbitraryMessageSystemAttributeNamePair = Arbitrary(genMesssageSystemAttributeNamePair)
}
