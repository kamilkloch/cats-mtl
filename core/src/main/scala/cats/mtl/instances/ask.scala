package cats
package mtl
package instances

import cats.mtl.lifting.ApplicativeLayer

trait AskInstances {
  implicit final def askLayerInd[M[_], Inner[_], E](
      implicit
      lift: ApplicativeLayer[M, Inner],
      under: ApplicativeAsk[Inner, E]): ApplicativeAsk[M, E] = {
    instanceOf(new ApplicativeAskClass[M, E] {
      val applicative = lift.outerInstance

      def ask: M[E] = lift.layer(under.ask)

      def reader[A](f: E => A): M[A] = lift.layer(under.reader(f))
    })
  }

}

private[instances] trait AskInstancesLowPriority1 {}

object ask extends AskInstances
