package com.nike.fawcett.sqs

import org.scalacheck._
import software.amazon.awssdk.services.sqs.model.{BatchResultErrorEntry, DeleteMessageBatchResponse, DeleteMessageBatchResultEntry}
import scala.collection.JavaConverters._
import BatchResultErrorEntryGen._
import DeleteMessageBatchResultEntryGen._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object DeleteMessageBatchResponseGen {
  def genDeleteMessageBatchResponse(
    genFailureAndSuccesses: Gen[(List[BatchResultErrorEntry], List[DeleteMessageBatchResultEntry])]) =
    for {
      (failed, successful) <- genFailureAndSuccesses
    } yield {
      DeleteMessageBatchResponse.builder
        .failed(failed.asJava)
        .successful(successful.asJava)
        .build
    }

  implicit val arbitraryDeleteMessageBatchResponse = Arbitrary(
    genDeleteMessageBatchResponse(
      FailuresAndSuccess.random[BatchResultErrorEntry, DeleteMessageBatchResultEntry](1, 10)
    ))
}
