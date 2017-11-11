package fpgatidbits.rosetta
import Chisel._

class ComparatorWrapper(dataWidth: Int, valuesPerIteration: Int, thresholds: Array[Int] ) extends Module {
  val io = new Bundle {
    // values to be compared
    val input = Vec.fill(valuesPerIteration){SInt(INPUT, 21)}
    // values to be output
    val output = Decoupled(Vec.fill(valuesPerIteration){UInt(OUTPUT, 1)})
  }

  // val thresholds = scala.io.Source.fromInputStream(this.getClass.getResourceAsStream("/test_data/thresholds.txt")).getLines.toArray
  // val t = Vec(thresholds(0).split(", ").map(f => SInt(f.toInt, width = 12)))
  val t = Vec(thresholds.map(s => SInt(s, dataWidth)))
  val counter = Reg(init=UInt(width=dataWidth))
  
  // sets all values in output to be 0, NECESSARY to avoid error
  for(i <- 0 to valuesPerIteration) {
    io.output.bits := UInt(0)
  }

  // creates a new comparator for each element in array 
  for(j <- 0 until valuesPerIteration){
      val in = Module(new Comparator(dataWidth)).io
      in.in0 := io.input(UInt(j))
      in.in1 := t(counter + UInt(j))
      io.output.bits(UInt(j)) := in.output
    }

  // sets the counter to be in correct position for next iteration
  when(counter === UInt(thresholds.length)) {
    counter := UInt(0)
  } .otherwise {
    counter := counter + UInt(valuesPerIteration)
  }
}


class ComparatorWrapperTest(c: ComparatorWrapper) extends Tester(c) {
  val test = Array[BigInt](1)
  val test2 = Array[BigInt](2)
  val test3 = Array[BigInt](3)
  val test4 = Array[BigInt](4)
  poke(c.io.input, test)
  peek(c.io.output)
  peek(c.counter)
  expect(c.io.output.bits(0), 0)
  step(1)
  poke(c.io.input, test2)
  peek(c.io.output)
  peek(c.counter)
  expect(c.io.output.bits(0), 1)
  step(1)
  poke(c.io.input, test3)
  peek(c.io.output)
  peek(c.counter)
  expect(c.io.output.bits(0), 0)
  step(1)
  poke(c.io.input, test4)
  peek(c.io.output)
  peek(c.counter)
  expect(c.io.output.bits(0), 0)
}


