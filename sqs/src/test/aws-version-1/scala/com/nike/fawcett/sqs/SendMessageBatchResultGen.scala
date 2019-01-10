package com.nike.fawcett.sqs

import org.scalacheck._
import com.amazonaws.services.sqs.model.{BatchResultErrorEntry, SendMessageBatchResult, SendMessageBatchResultEntry}
import BatchResultErrorEntryGen._
import SendMessageBatchResultEntryGen._
import scala.collection.JavaConverters._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object SendMessageBatchResultGen {
  def genSendMessageBatchResult(failureAndSuccess: Gen[(List[BatchResultErrorEntry], List[SendMessageBatchResultEntry])]) =
    for {
      (failed, successful) <- failureAndSuccess
    } yield {
      new SendMessageBatchResult()
        .withFailed(failed.asJava)
        .withSuccessful(successful.asJava)
    }

  implicit val arbitrarySendMessageBatchResult = Arbitrary(
    genSendMessageBatchResult(FailuresAndSuccess.random[BatchResultErrorEntry, SendMessageBatchResultEntry](1, 10))
  )
}
