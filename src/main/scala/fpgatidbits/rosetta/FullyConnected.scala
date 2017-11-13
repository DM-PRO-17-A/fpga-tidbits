package fpgatidbits.rosetta

import Chisel._

// These param names are really bad
class FullyConnected(kernels: Int, weights: Array[Array[Int]], input_size: Int, weight_size: Int, output_size: Int, input_width: Int) extends Module {
    //val output_width = math.ceil(math.log(input_size * 2 ^ input_width * weight_size) / math.log(2)).toInt + 1
    val output_width = 12
    val io = new Bundle {
        val input_data = Vec.fill(input_size){UInt(INPUT, input_width)}

        val output_data = Decoupled(Vec.fill(output_size){SInt(OUTPUT)})
    }
    io.output_data.valid := Bool(false)

    val w = Vec(weights.map(s => Vec(s.map(t => UInt(t, 1)))))

    val acc = Vec.fill(output_size){Reg(init=SInt(0))}

    val weight_counter = Reg(init=UInt(width=log2Up(output_width)))
    val output_counter = Reg(init=UInt(width=log2Up(output_width)))

    for(k <- 0 until kernels){
        val dotprod = Module(new DotProduct(input_size, output_width)).io
        dotprod.vec_1 := io.input_data

        val w_slice = Vec.fill(input_size){UInt(width=1)}
        for(i <- 0 until input_size) {
            w_slice(i) := w(k)(weight_counter+UInt(i))
        }
        dotprod.vec_2 := w_slice

        when(weight_counter === UInt(0)){
            acc(UInt(k)) := dotprod.data_out
        } .otherwise {
            acc(output_counter + UInt(k)) := acc(output_counter + UInt(k)) + dotprod.data_out
        }

        output_counter := output_counter + UInt(kernels)
        when(output_counter === UInt(output_size) - UInt(kernels)) {
            output_counter := UInt(0)

            when(weight_counter === UInt(weight_size) - UInt(input_size)) {
                weight_counter := UInt(0)
                io.output_data.valid := Bool(true)
            } .otherwise {
                weight_counter := weight_counter + UInt(input_size)
            }
        }

    }

    for(i <- 0 until output_size) {
        io.output_data.bits(i) := acc(i)
    }
}

class FullyConnectedTests(c: FullyConnected) extends Tester(c) {
    val test_array = Array[BigInt](20, 10, 2, 45)
    val test_array_2 = Array[BigInt](1,2,3,4)
    val step_size = 2

    // This test is really ugly
    poke(c.io.input_data, test_array)
    peek(c.acc)
    step(1)
//    poke(c.io.input_data, Array[BigInt](2, 45))
//    peek(c.acc)
//    step(1)
    peek(c.io.output_data)
    expect(c.io.output_data.bits(0), -33)
    expect(c.io.output_data.bits(1), 77)
    expect(c.io.output_data.bits(2), -77)
    poke(c.io.input_data, test_array_2)
    step(2)
    peek(c.acc)
    peek(c.io.output_data)
    expect(c.io.output_data.bits(0), -2)
    expect(c.io.output_data.bits(1), 10)
    expect(c.io.output_data.bits(2), -10)

}