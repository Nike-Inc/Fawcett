package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.{Message, MessageAttributeValue}
import org.scalacheck._
import Arbitrary.arbitrary
import scala.jdk.CollectionConverters._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object MessageGen {

  implicit val cogenMessage: Cogen[Message] =
    Cogen.tuple7[String, String, Map[String, String], Map[String, MessageAttributeValue], String, String, String]
      .contramap(message => (
        message.getMessageId,
        message.getBody,
        message.getAttributes.asScala.toMap,
        message.getMessageAttributes.asScala.toMap,
        message.getReceiptHandle,
        message.getMD5OfBody,
        message.getMD5OfMessageAttributes
      ))

  val genMesssage = for {
    messageId <- arbitrary[String]
    body <- arbitrary[String]
    attributes <- arbitrary[Map[String, String]]
    messageAttributes <- arbitrary[Map[String, MessageAttributeValue]]
    receiptHandle <- arbitrary[String]
    md5OfBody <- arbitrary[String]
    md5OfMessageAttributes <- arbitrary[String]
  } yield {
    new Message()
      .withMessageId(messageId)
      .withBody(body)
      .withAttributes(attributes.asJava)
      .withMessageAttributes(messageAttributes.asJava)
      .withReceiptHandle(receiptHandle)
      .withMD5OfBody(md5OfBody)
      .withMD5OfMessageAttributes(md5OfMessageAttributes)
  }

  implicit val arbitraryMessage = Arbitrary(genMesssage)

}
