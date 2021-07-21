package lombok

import org.scalatest._

class DataSuite extends FunSuite {
  test("generate setter and getter") {
    @data
    class A {
      var x: Int = _
      var y: String = _
    }

    val a = new A
    a.setX(12)
    assert(a.getX === 12)
    a.setY("Hello")
    assert(a.getY === "Hello")
  }

  test("handle operator in the name") {
    @data
    class B {
      var op_+ : Int = _
    }

    val b = new B
    b.setOp_+(42)
    assert(b.getOp_+ === 42)
  }
}
