package com.nike.fawcett.sqs

import org.scalacheck._
import com.amazonaws.services.sqs.model.{BatchResultErrorEntry, DeleteMessageBatchResult, DeleteMessageBatchResultEntry}
import scala.jdk.CollectionConverters._
import BatchResultErrorEntryGen._
import DeleteMessageBatchResultEntryGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object DeleteMessageBatchResultGen {
  def genDeleteMessageBatchResult(
    genFailureAndSuccesses: Gen[(List[BatchResultErrorEntry], List[DeleteMessageBatchResultEntry])]) =
    for {
      (failed, successful) <- genFailureAndSuccesses
    } yield {
      new DeleteMessageBatchResult()
        .withFailed(failed.asJava)
        .withSuccessful(successful.asJava)
    }

  implicit val arbitraryDeleteMessageBatchResult = Arbitrary(
    genDeleteMessageBatchResult(
      FailuresAndSuccess.random[BatchResultErrorEntry, DeleteMessageBatchResultEntry](1, 10)
    ))
}
