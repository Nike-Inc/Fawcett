package com.nike.fawcett

import org.scalacheck._
import Gen._
import Arbitrary.arbitrary
import org.typelevel.discipline.Laws
import scala.util.Random
import org.scalacheck.Gen._
import org.scalacheck.Prop._

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

trait TestCommon extends Laws {

  val allValidSymbols = List(
    '!','"','#','$','%','&','\'','(',
    ')','*','+',',','-','.','/',':',
    ';','<','=','>','?','@','[','\\',']',
    '^','_','`','{','|','}','~')

  def identifier(min: Int, max: Int, symbols: Char*): Gen[String] =
    for {
      length <- choose(min, max)
      id <- listOfN(length, frequency((1,numChar), (8,alphaChar), (1, oneOf(symbols))))
    } yield id.mkString

  object FailuresAndSuccess {
    def includesSpecific[Failure, Success: Arbitrary](
      specific: Gen[Failure],
      nonSpecific: Gen[Failure],
      min: Int,
      max: Int): Gen[(List[Failure], List[Success])] =
      for {
        specificCount <- choose(min, max)
        nonSpecificCount <- choose(0, max - specificCount)
        successCount <- choose(0, max - specificCount - nonSpecificCount)
        specifics <- listOfN[Failure](specificCount, specific)
        nonSpecifics <- listOfN[Failure](nonSpecificCount, nonSpecific)
        successes <- listOfN(successCount, arbitrary[Success])
      } yield (Random.shuffle(specifics ++ nonSpecifics), successes)

    def includesFailures[Failure: Arbitrary, Success: Arbitrary](min: Int, max: Int): Gen[(List[Failure], List[Success])] =
      for {
        failureCount <- choose(min, max)
        failures <- listOfN(failureCount, arbitrary[Failure])
        successCount <- choose(0, max - failureCount)
        successes <- listOfN(successCount, arbitrary[Success])
      } yield (failures, successes)

    def random[Failure: Arbitrary, Success: Arbitrary](min: Int, max: Int): Gen[(List[Failure], List[Success])] =
      for {
        failureCount <- choose(0, max)
        failures <- listOfN(failureCount, arbitrary[Failure])
        successCount <- choose(0, max - failureCount)
        successes <- listOfN(successCount, arbitrary[Success])
      } yield (failures, successes)


    def onlySuccess[Failure, Success: Arbitrary](min: Int, max: Int): Gen[(List[Failure], List[Success])] =
      for {
        successCount <- choose(min, max)
        successes <- listOfN(successCount, arbitrary[Success])
      } yield (Nil, successes)
  }

  def existsTraversal[Source](getLens: Source => Boolean, containsCondition: Gen[Source], doesntContainCondition: Gen[Source]) =
    new SimpleRuleSet("exists in traversal",
      "any matching condition results in true" -> forAll(containsCondition)(getLens(_) == true),
      "no matching condition results in false" -> forAll(doesntContainCondition)(getLens(_) == false)
    )
}
