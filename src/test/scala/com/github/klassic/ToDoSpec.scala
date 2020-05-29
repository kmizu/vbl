package com.github.pvb

import pvb.runtime.NotImplementedError

class ToDoSpec extends SpecHelper {
  describe("ToDo() function") {
    it("throw NotImplementedException when it is evaluated") {
      intercept[NotImplementedError] {
        E(
          """
            |Function fact(n)
            |  If n < 2 Then
            |    ToDo()
            |  Else
            |    n * fact(n - 1)
            |  End If
            |End Function
            |fact(0)
          """.stripMargin
        )
      }
    }
  }
}
