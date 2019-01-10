package com.nike.fawcett.$service;format="lower"$

import cats.implicits._
import org.scalatest.{ FunSuite, Matchers }
import monocle.law.discipline.LensTests
import $model$Lens._
import org.typelevel.discipline.scalatest.Discipline


class $model$LensTest extends FunSuite with Matchers with Discipline {
  import $model$LensTest.Gen._
}
