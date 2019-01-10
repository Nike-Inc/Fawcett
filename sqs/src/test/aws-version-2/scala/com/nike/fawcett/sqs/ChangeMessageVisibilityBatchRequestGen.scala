package com.nike.fawcett.sqs

import software.amazon.awssdk.services.sqs.model.{ChangeMessageVisibilityBatchRequestEntry, ChangeMessageVisibilityBatchRequest}
import org.scalacheck._
import Gen._
import Arbitrary.arbitrary
import ChangeMessageVisibilityBatchRequestEntryGen._
import scala.collection.JavaConverters._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

object ChangeMessageVisibilityBatchRequestGen {
  implicit val cogenChangeMessageVisibilityBatchRequest: Cogen[ChangeMessageVisibilityBatchRequest] =
    Cogen.tuple2[List[ChangeMessageVisibilityBatchRequestEntry], String].contramap(request => (
      request.entries.asScala.toList,
      request.queueUrl
    ))


  val genChangeMessageVisibilityBatchRequest = for {
    count <- choose(1, 10)
    entries <- listOfN(count, arbitrary[ChangeMessageVisibilityBatchRequestEntry])
    queueUrl <- arbitrary[String]
  } yield {
    ChangeMessageVisibilityBatchRequest.builder
      .entries(entries.asJava)
      .queueUrl(queueUrl)
      .build
  }

  implicit val arbitraryChangeMessageVisibilityBatchRequest = Arbitrary(genChangeMessageVisibilityBatchRequest)
}
