package com.nike.fawcett.sqs

import cats.Eq
import software.amazon.awssdk.services.sqs.model.QueueAttributeName
import org.scalacheck._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

trait QueueAttributeNameGen {
  implicit val cogenQueueAttributeName: Cogen[QueueAttributeName] = Cogen[String].contramap { _.toString }
  implicit val eqQueueAttributeName: Eq[QueueAttributeName] = Eq.fromUniversalEquals
}
object QueueAttributeNameGen extends QueueAttributeNameGen
