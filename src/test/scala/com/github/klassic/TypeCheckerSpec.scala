package com.github.pvb

/**
  * Created by Mizushima on 2016/05/30.
  */
class TypeCheckerSpec extends SpecHelper {
  describe("assignment") {
    val expectations: List[(String, Value)] = List(
      """
        |Dim a=1
        |a
      """.stripMargin -> BoxedInt(1),
      """
        |Dim a=1
        |a = a + 1
        |a
      """.stripMargin -> BoxedInt(2),
      """
        |Dim s="FOO"
        |s=s+s
        |s
      """.stripMargin -> ObjectValue("FOOFOO")
    )

    expectations.zipWithIndex.foreach { case ((in, expected), i) =>
      it(s"${in} evaluates to ${expected}") {
        assert(expected == E(in))
      }
    }
  }

  describe("valid function type") {
    val expectations: List[(String, Value)] = List(
      """
        |Function add(x As Int, y As Int) As Int
        |  x + y
        |End Function
        |Dim f As (Int, Int) => Int = add
        |f(2, 3)
      """.stripMargin -> BoxedInt(5))

    expectations.zipWithIndex.foreach { case ((in, expected), i) =>
      it(s"${in} evaluates to ${expected}") {
        assert(expected == E(in))
      }
    }
  }

  describe("arithmetic operation between incompatible type cannot be done") {
    val inputs = List(
      """
        |Dim a = 1
        |Dim b = 2L
        |1 + 2L
      """.stripMargin,
      """
        |Dim a = 1
        |Dim b As Long = a
        |b
      """.stripMargin
    )
    inputs.zipWithIndex.foreach{ case (in, i) =>
      it(s"expectation  ${i}") {
        val e = intercept[TyperException] {
          E(in)
        }
      }
    }
  }

  describe("valid foreach expression") {
    val expectations: List[(String, Value)] = List(
      """
        |Dim a = 1
        |a = 2
        |Foreach b in [1, 2, 3]
        |  (b As Int) + 3
        |End Foreach
      """.stripMargin -> UnitValue
    )

    expectations.zipWithIndex.foreach { case ((in, expected), i) =>
      it(s"expectation  ${i}") {
        assert(expected == E(in))
      }
    }
  }

  describe("invalid foreach expression") {
    val illTypedPrograms: List[String] = List(
      """
        |Dim a = 1
        |Foreach a in [1, 2, 3]
        |  b + 3
        |End Foreach
      """.stripMargin
    )
    illTypedPrograms.zipWithIndex.foreach { case (in, i) =>
      it(s"expectation  ${i}") {
        val e = intercept[TyperException] {
          E(in)
        }
      }
    }
  }

  describe("function type doesn't match ") {
    val illTypedPrograms: List[String] = List(
      """
        |Function f(x, y) x + y End Function
        |f(10)
      """.stripMargin
    )
    illTypedPrograms.zipWithIndex.foreach { case (in, i) =>
      it(s"expectation  ${i}") {
        val e = intercept[TyperException] {
          E(in)
        }
      }
    }
  }

}

