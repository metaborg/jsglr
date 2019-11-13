import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`
import $ivy.`io.circe::circe-generic:0.12.3`
import $ivy.`io.circe::circe-yaml:0.11.0-M1`

import ammonite.ops._
import cats.syntax.either._
import io.circe.generic.auto._
import io.circe.yaml._

import $file.model, model._

val configJson = parser.parse(read! pwd/"config.yml")
val config = configJson.flatMap(_.as[Config]).valueOr(throw _)