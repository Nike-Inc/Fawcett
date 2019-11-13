package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.{Message, MessageAttributeValue, MessageSystemAttributeName}
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
    Cogen.tuple7[String, String, Map[MessageSystemAttributeName, String], Map[String, MessageAttributeValue], String, String, String]
      .contramap(message => (
        message.messageId,
        message.body,
        message.attributes.asScala.toMap,
        message.messageAttributes.asScala.toMap,
        message.receiptHandle,
        message.md5OfBody,
        message.md5OfMessageAttributes
      ))

  val genMesssage = for {
    messageId <- arbitrary[String]
    body <- arbitrary[String]
    attributes <- arbitrary[Map[MessageSystemAttributeName, String]]
    messageAttributes <- arbitrary[Map[String, MessageAttributeValue]]
    receiptHandle <- arbitrary[String]
    md5OfBody <- arbitrary[String]
    md5OfMessageAttributes <- arbitrary[String]
  } yield {
    Message.builder
      .messageId(messageId)
      .body(body)
      .attributes(attributes.asJava)
      .messageAttributes(messageAttributes.asJava)
      .receiptHandle(receiptHandle)
      .md5OfBody(md5OfBody)
      .md5OfMessageAttributes(md5OfMessageAttributes)
      .build
  }

  implicit val arbitraryMessage = Arbitrary(genMesssage)

}
