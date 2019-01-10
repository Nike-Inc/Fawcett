package com.nike.fawcett.$service;format="lower"$

import $model$Lens._
import org.scalacheck._
import com.amazonaws.services.$service;format="lowerCase"$.model.$model$

object $model$Gen {
  implicit val cogen$model$: Cogen[$model$] = ???

  val gen$model$ = for {
    _ <- ???
  } yield {
    new $model$()
  }

  implicit val arbitrary$model$ = Arbitrary(gen$model$)
}
