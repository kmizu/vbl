package com.github.pvb

/**
  * Created by Mizushima on 2016/05/30.
  */
class RecordSpec extends SpecHelper {

  describe("new record") {
    val expectations: List[(String, Value)] = List(
      """
        |Record Person
        |  name As *
        |  age As Int
        |End Record
        |#Person("Hoge", 7)
      """.stripMargin -> RecordValue("Person", List("name" -> ObjectValue("Hoge"), "age" -> BoxedInt(7))),
      """
        |Record Tuple<'a, 'b>
        |  _1 As 'a
        |  _2 As 'b
        |End Record
        |#Tuple(1, 2)
      """.stripMargin -> RecordValue("Tuple", List("_1" -> BoxedInt(1), "_2" -> BoxedInt(2)))
    )

    expectations.foreach{ case (in, expected) =>
      it(s"${in} evaluates to ${expected}") {
        assert(expected == E.evaluateString(in))
      }
    }
  }

  describe("access record") {
    val expectations: List[(String, Value)] = List(
      """
        |Record Person
        |  name As *
        |  age As Int
        |End Record
        |Dim p = #Person("Hoge", 7)
        |p.name
      """.stripMargin -> ObjectValue("Hoge"),
      """
        |Record Tuple<'a, 'b>
        |  _1 As 'a
        |  _2 As 'b
        |End Record
        |Dim t = #Tuple(1, 2)
        |t._1
      """.stripMargin -> BoxedInt(1)
    )

    expectations.foreach{ case (in, expected) =>
      it(s"${in} evaluates to ${expected}") {
        assert(expected == E(in))
      }
    }

    intercept[TyperException] {
      E {
        """
          | Record Person
          |   name As *
          |   age As Int
          | End Record
          | Dim p = #Person("Hoge", 1.0)
        """.stripMargin
      }
    }
    intercept[TyperException] {
      E {
        """
          | Record Tuple<'a, 'b>
          |   _1 As 'a
          |   _2 As 'b
          | End Record
          | Dim t = #Tuple(1, 2)
          | Dim k As Double = t._1
        """.stripMargin
      }
    }
  }
}
