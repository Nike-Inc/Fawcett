package com.nike.fawcett.sqs

import com.amazonaws.services.sqs.model.{ChangeMessageVisibilityBatchRequestEntry, ChangeMessageVisibilityBatchRequest}
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
      request.getEntries.asScala.toList,
      request.getQueueUrl
    ))


  val genChangeMessageVisibilityBatchRequest = for {
    count <- choose(1, 10)
    entries <- listOfN(count, arbitrary[ChangeMessageVisibilityBatchRequestEntry])
    queueUrl <- arbitrary[String]
  } yield {
    new ChangeMessageVisibilityBatchRequest()
      .withEntries(entries.asJava)
      .withQueueUrl(queueUrl)
  }

  implicit val arbitraryChangeMessageVisibilityBatchRequest = Arbitrary(genChangeMessageVisibilityBatchRequest)
}
