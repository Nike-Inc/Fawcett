package com.nike.fawcett

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

package object sqs extends TestCommon
  with QueueAttributeNameGen
  with MessageAttributeMapGen
  with MessageSystemAttributeNameGen {
}
