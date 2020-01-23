package com.nike.fawcett.sqs

import com.nike.fawcett.opticsMacro.awsOptics
import monocle.function.all._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

@awsOptics("sqs.model.ReceiveMessageResponse", exclude = List("hasMessages"))
trait ReceiveMessageResponseLens {
  val allBodies = messages composeTraversal each composeLens MessageLens.body
}
