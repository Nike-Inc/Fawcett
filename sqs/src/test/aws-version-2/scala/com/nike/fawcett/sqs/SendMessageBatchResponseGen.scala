package com.nike.fawcett.sqs

import org.scalacheck._
import software.amazon.awssdk.services.sqs.model.{BatchResultErrorEntry, SendMessageBatchResponse, SendMessageBatchResultEntry}
import BatchResultErrorEntryGen._
import SendMessageBatchResultEntryGen._
import scala.collection.JavaConverters._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object SendMessageBatchResponseGen {
  def genSendMessageBatchResponse(failureAndSuccess: Gen[(List[BatchResultErrorEntry], List[SendMessageBatchResultEntry])]) =
    for {
      (failed, successful) <- failureAndSuccess
    } yield {
      SendMessageBatchResponse.builder
        .failed(failed.asJava)
        .successful(successful.asJava)
        .build
    }

  implicit val arbitrarySendMessageBatchResponse = Arbitrary(
    genSendMessageBatchResponse(FailuresAndSuccess.random[BatchResultErrorEntry, SendMessageBatchResultEntry](1, 10))
  )
}
