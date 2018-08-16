package cats
package mtl
package tests

import cats._
import cats.arrow.FunctionK
import cats.data._
import cats.instances.all._
import cats.laws.discipline.SerializableTests
import cats.mtl.instances.listen._
import cats.laws.discipline.arbitrary._
import cats.mtl.laws.discipline._
import cats.mtl.lifting.{ApplicativeLayerFunctor, FunctorLayerFunctor, MonadLayerControl}
import org.scalacheck._

class ValidatedTests extends BaseSuite {

  import cats.mtl.instances.handle._

  checkAll("Validated[String, ?]", ApplicativeHandleTests[Validated[String, ?], String].applicativeHandle[Int])

}
