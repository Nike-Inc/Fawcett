package com.nike.fawcett.sqs

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

sealed trait MessageAttributeValueDataType { val key: String }
object BinaryValue extends MessageAttributeValueDataType { val key = "Binary" }
object StringValue extends MessageAttributeValueDataType { val key = "String" }
object NumberValue extends MessageAttributeValueDataType { val key = "Number" }

