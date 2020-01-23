package com.nike.fawcett

import org.scalacheck._
import org.scalacheck.Gen._
import Arbitrary.arbitrary
import software.amazon.awssdk.core.SdkBytes

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

package object sqs extends TestCommon
  with QueueAttributeNameGen
  with MessageAttributeMapGen
  with MessageSystemAttributeNameForSendsGen
  with MessageSystemAttributeNameGen {

  implicit val cogenSdkBytes: Cogen[SdkBytes] = Cogen[Array[Byte]].contramap(_.asByteArray)

  implicit val arbitrarySdkBytes: Arbitrary[SdkBytes] =
    Arbitrary(nonEmptyContainerOf[Array, Byte](arbitrary[Byte]).map(SdkBytes.fromByteArray))

}
