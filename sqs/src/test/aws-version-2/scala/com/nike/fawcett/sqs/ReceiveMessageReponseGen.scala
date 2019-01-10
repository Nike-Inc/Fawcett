package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.{Message, ReceiveMessageResponse}
import org.scalacheck._
import Arbitrary.arbitrary
import Gen._
import MessageGen._
import scala.collection.JavaConverters._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object ReceiveMessageResponseGen {
  implicit val cogenReceiveMessageResponse: Cogen[ReceiveMessageResponse] =
    Cogen[List[Message]].contramap(_.messages.asScala.toList)

  val genReceiveMessageResponse = for {
    count <- choose(1, 10)
    messages <- listOfN(count, arbitrary[Message])
  } yield {
    ReceiveMessageResponse.builder
      .messages(messages.asJava)
      .build
  }

  implicit val arbitraryReceiveMessageResponse = Arbitrary(genReceiveMessageResponse)
}
