package cats
package mtl
package instances

import cats.data.WriterT
import cats.syntax.all._

trait ListenInstances {
  implicit final def listenWriter[M[_], L]
  (implicit M: Monad[M], L: Monoid[L]
  ): ApplicativeListen[WriterTC[M, L]#l, L] = {
    new ApplicativeListen[WriterTC[M, L]#l, L] {
      val monad = WriterT.catsDataMonadWriterForWriterT(M, L)
      val tell = instances.tell.tellWriter[M, L]

      def listen[A](fa: WriterT[M, L, A]): WriterT[M, L, (A, L)] = {
        WriterT[M, L, (A, L)](fa.run.map { case (l, a) => (l, (a, l)) })
      }

      def pass[A](fa: WriterT[M, L, (A, L => L)]): WriterT[M, L, A] = {
        WriterT[M, L, A](fa.run.map { case (l, (a, f)) => (f(l), a) })
      }

      def listens[A, B](fa: WriterT[M, L, A])(f: (L) => B): WriterT[M, L, (B, A)] = {
        WriterT[M, L, (B, A)](fa.run.map { case (l, a) => (l, (f(l), a)) })
      }

      def censor[A](fa: WriterT[M, L, A])(f: (L) => L): WriterT[M, L, A] = {
        WriterT[M, L, A](fa.run.map { case (l, a) => (f(l), a) })
      }
    }
  }

  implicit final def listenTuple[L]
  (implicit L: Monoid[L]
  ): ApplicativeListen[TupleC[L]#l, L] = {
    new ApplicativeListen[TupleC[L]#l, L] {
      val monad = cats.instances.tuple.catsStdMonadForTuple2(L)
      val tell = instances.tell.tellTuple[L]

      def listen[A](fa: (L, A)): (L, (A, L)) = {
        val (l, a) = fa
        (l, (a, l))
      }

      def pass[A](fa: (L, (A, L => L))): (L, A) = {
        val (l, t) = fa
        (t._2(l), t._1)
      }

      def listens[A, B](fa: (L, A))(f: (L) => B): (L, (B, A)) = {
        val (l, a) = fa
        (l, (f(l), a))
      }

      def censor[A](fa: (L, A))(f: (L) => L): (L, A) = (f(fa._1), fa._2)
    }
  }
}

object listen extends ListenInstances