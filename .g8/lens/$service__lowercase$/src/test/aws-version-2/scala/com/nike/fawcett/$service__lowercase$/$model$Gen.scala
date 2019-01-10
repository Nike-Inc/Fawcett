package com.nike.fawcett.$service;format="lower"$

import $model$Lens._
import org.scalacheck._
import software.amazon.awssdk.services.$service;format="lowerCase"$.model.$model$

object $model$Gen {
  implicit val cogen$model$: Cogen[$model$] = ???

  val gen$model$ = for {
    _ <- ???
  } yield {
    $model$.builder
  }

  implicit val arbitrary$model$ = Arbitrary(gen$model$)
}
