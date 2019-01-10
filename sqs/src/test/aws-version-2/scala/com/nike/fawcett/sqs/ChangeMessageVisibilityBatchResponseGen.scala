package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.{BatchResultErrorEntry, ChangeMessageVisibilityBatchResponse,
  ChangeMessageVisibilityBatchResultEntry}
import org.scalacheck._
import BatchResultErrorEntryGen._
import ChangeMessageVisibilityBatchResultEntryGen._
import scala.collection.JavaConverters._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object ChangeMessageVisibilityBatchResponseGen {
  implicit val cogenChangeMessageVisibilityBatchResult: Cogen[ChangeMessageVisibilityBatchResponse] =
    Cogen.tuple2[List[BatchResultErrorEntry], List[ChangeMessageVisibilityBatchResultEntry]].contramap(result => (
      result.failed.asScala.toList,
      result.successful.asScala.toList
    ))


  def genChangeMessageVisibilityBatchResult(
    failureAndSuccess: Gen[(List[BatchResultErrorEntry], List[ChangeMessageVisibilityBatchResultEntry])]) =
    for {
      (failed, success) <- failureAndSuccess
    } yield {
      ChangeMessageVisibilityBatchResponse.builder
        .failed(failed.asJava)
        .successful(success.asJava)
        .build
    }

  implicit val arbitraryChangeMessageVisibilityBatchResult = Arbitrary(
    genChangeMessageVisibilityBatchResult(
      FailuresAndSuccess.random[BatchResultErrorEntry, ChangeMessageVisibilityBatchResultEntry](1, 10)
    ))
}
