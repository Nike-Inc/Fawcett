package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.{BatchResultErrorEntry, ChangeMessageVisibilityBatchResult,
  ChangeMessageVisibilityBatchResultEntry}
import org.scalacheck._
import BatchResultErrorEntryGen._
import ChangeMessageVisibilityBatchResultEntryGen._
import scala.jdk.CollectionConverters._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object ChangeMessageVisibilityBatchResultGen {
  implicit val cogenChangeMessageVisibilityBatchResult: Cogen[ChangeMessageVisibilityBatchResult] =
    Cogen.tuple2[List[BatchResultErrorEntry], List[ChangeMessageVisibilityBatchResultEntry]].contramap(result => (
      result.getFailed.asScala.toList,
      result.getSuccessful.asScala.toList
    ))


  def genChangeMessageVisibilityBatchResult(
    failureAndSuccess: Gen[(List[BatchResultErrorEntry], List[ChangeMessageVisibilityBatchResultEntry])]) =
    for {
      (failed, success) <- failureAndSuccess
    } yield {
      new ChangeMessageVisibilityBatchResult()
        .withFailed(failed.asJava)
        .withSuccessful(success.asJava)
    }

  implicit val arbitraryChangeMessageVisibilityBatchResult = Arbitrary(
    genChangeMessageVisibilityBatchResult(
      FailuresAndSuccess.random[BatchResultErrorEntry, ChangeMessageVisibilityBatchResultEntry](1, 10)
    ))
}
