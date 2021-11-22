class Signal(initVal: Int) {
  var curVal = initVal
  def apply(): Int = curVal
  protected def update(x: Int): Unit = curVal = x
}

//// Companion objects to enable instance creation without 'new' keyword
object Signal { def apply(initVal: Int) = new Signal(initVal) }
//object Var { def apply(initVal: Int) = new Var(initVal) }

abstract class Var {
  var curVal: Int
  def apply(initVal:Int) = curVal = initVal
}
//object Var {
//  def apply(initVal:Int) = new Var(initVal)
//}
class Bandwidth extends Var {

  override var curVal: Int = 0

  def Usage(x: Int): Unit = {
    val newCurrentVal = curVal + x
    this.apply(newCurrentVal)
  }

}
def consolidated(usage: List[Bandwidth]): Signal = Signal(usage.map(_.curVal).sum)

val a = new Bandwidth()
val b = new Bandwidth()

println("Consolidated: " + consolidated(List(a, b)).curVal)

a Usage 9
println (a.curVal)

a Usage 1
println (a.curVal)

println("Consolidated: " + consolidated(List(a, b)).curVal)

b Usage 3
println (b.curVal)

b Usage 9
println (b.curVal)

println("Consolidated: " + consolidated(List(a, b)).curVal)