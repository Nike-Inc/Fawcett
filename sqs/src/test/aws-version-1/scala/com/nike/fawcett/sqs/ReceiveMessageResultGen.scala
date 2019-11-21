package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.{Message, ReceiveMessageResult}
import org.scalacheck._
import Arbitrary.arbitrary
import Gen._
import MessageGen._
import scala.jdk.CollectionConverters._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object ReceiveMessageResultGen {
  implicit val cogenReceiveMessageResult: Cogen[ReceiveMessageResult] =
    Cogen[List[Message]].contramap(_.getMessages.asScala.toList)

  val genReceiveMessageResult = for {
    count <- choose(1, 10)
    messages <- listOfN(count, arbitrary[Message])
  } yield {
    new ReceiveMessageResult()
      .withMessages(messages.asJava)
  }

  implicit val arbitraryReceiveMessageResult = Arbitrary(genReceiveMessageResult)
}
