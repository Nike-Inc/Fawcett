package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.{SendMessageBatchRequest, SendMessageBatchRequestEntry}
import org.scalacheck._
import org.scalacheck.Gen._
import Arbitrary.arbitrary
import scala.collection.JavaConverters._
import SendMessageBatchRequestEntryGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object SendMessageBatchRequestGen {
  implicit val arbitrarySendMessageBatchRequest = Arbitrary {
    for {
      queueUrl <- arbitrary[String]
      entryCount <- choose(1, 10)
      entries <- listOfN(entryCount, arbitrary[SendMessageBatchRequestEntry])
    } yield {
      new SendMessageBatchRequest()
        .withQueueUrl(queueUrl)
        .withEntries(entries.asJava)
    }
  }
}
