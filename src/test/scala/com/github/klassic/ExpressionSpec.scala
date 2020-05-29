package com.github.pvb

/**
  * Created by Mizushima on 2016/05/30.
  */
class ExpressionSpec extends SpecHelper {
  describe("assignment") {
    it("is evaluated correctly") {
      assertResult(
        E(
          """
            |Dim a=1
            |a
          """.stripMargin))(BoxedInt(1))
      assertResult(
        E(
          """
            |Dim a=1
            |a = a + 1
            |a
          """.stripMargin))(BoxedInt(2))
      assertResult(
        E(
          """
            |Dim a=1
            |a += 1
            |a
          """.stripMargin))(BoxedInt(2))
      assertResult(
        E(
          """
            |Dim a=1
            |a -= 1
            |a
          """.stripMargin))(BoxedInt(0))
      assertResult(
        E(
          """
            |Dim a=3
            |a *= 2
            |a
          """.stripMargin))(BoxedInt(6))
      assertResult(
        E(
          """
            |Dim a=5
            |a /= 2
            |a
          """.stripMargin))(BoxedInt(2))
    }
  }

  describe("while expression") {
    it("is evaluated correctly") {
      assertResult(
        E(
          """
            |Dim i = 1
            |While i < 10
            |  i = i + 1
            |End While
            |i
          """.stripMargin))(BoxedInt(10))
      assertResult(
        E(
          """
            |Dim i = 10
            |While i >= 0
            |  i = i - 1
            |End While
            |i
          """.stripMargin))(BoxedInt(-1))
      assertResult(
        E(
          s"""
             |Dim buf = new java.lang.StringBuffer
             |Dim i = 0
             |While i <= 5
             |  buf->append("#{i}")
             |  i = i + 1
             |End While
             |buf->toString()
      """.stripMargin))(ObjectValue("012345"))
    }
  }

  describe("anonymous function") {
    it("is evaluated correctly") {
      assertResult(
        E("""
            |Dim add = (x, y) => x + y
            |add(3, 3)
          """.stripMargin))(BoxedInt(6))
    }
  }

  describe("logical expression") {
    it("is evaluated correctly"){
      assertResult(
        E(
          """
            |Dim i = 1
            |0 <= i && i <= 10
          """.stripMargin))(BoxedBoolean(true))
      assertResult(
        E(
          """
            |Dim i = -1
            |0 <= i && i <= 10
          """.stripMargin))(BoxedBoolean(false))
      var input =
        """
            |Dim i = -1
            |i < 0 || i > 10
        """.stripMargin
      assertResult(
        E(input)
      )(
        BoxedBoolean(true)
      )
      input =
        """
          |Dim i = 1
          |i < 0 || i > 10
        """.stripMargin
      assertResult(
        E(input)
      )(BoxedBoolean(false))
    }

    describe("foreach expression") {
      it("is evaluated correctly") {
        assertResult(
          E(
            """
              |Dim newList = new java.util.ArrayList
              |Foreach a in [1, 2, 3, 4, 5]
              |  newList->add((a As Int) * 2)
              |End Foreach
              |newList
            """.stripMargin))(ObjectValue(listOf(2, 4, 6, 8, 10)))
      }
    }

    describe("if expression") {
      it("is evaluated correctly") {
        assertResult(
          E(
            """
              |If true Then 1.0 Else 2.0 End If
            """.stripMargin))(BoxedDouble(1.0))
        assertResult(
          E(
            """
              |If false Then 1.0 Else 2.0 End If
            """.stripMargin))(BoxedDouble(2.0))
      }
    }

    describe("function definition") {
      it("is evaluated correctly") {
        assertResult(
          E(
            """
              |Function add(x, y) x + y End Function
              |add(2, 3)
            """.stripMargin))(BoxedInt(5))
        assertResult(
          E(
            """
              |Function fact(n) If n < 2 Then 1 Else (n * fact(n - 1)) End If End Function
              |fact(4)
            """.stripMargin))(BoxedInt(24))
        assertResult(
          E(
            """
              |Function none()
              |  24
              |End Function
              |none()
            """.stripMargin))(BoxedInt(24))
        assertResult(
          E(
            """
              |Function hello()
              |  "Hello"
              |  0
              |End Function
              |hello()
            """.stripMargin))(BoxedInt(0))
      }
    }
  }
}
