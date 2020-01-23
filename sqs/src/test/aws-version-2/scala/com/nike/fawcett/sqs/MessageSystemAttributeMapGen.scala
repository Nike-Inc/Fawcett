
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

object MessageSystemAttributeMapGen {
  val systemAttributeMapEntryGen: Gen[(MessageSystemAttributeNameForSends, MessageSystemAttributeValue)] = for {
    attribute <- arbitrary[MessageSystemAttributeValue]
    name <- arbitrary[MessageSystemAttributeNameForSends]
  } yield {
    name -> attribute
  }

  implicit val arbitraryMessageSystemAttributeMap = Arbitrary(mapOf(systemAttributeMapEntryGen))
}
