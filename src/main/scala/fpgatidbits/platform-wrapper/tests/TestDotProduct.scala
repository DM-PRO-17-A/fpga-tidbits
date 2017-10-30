package fpgatidbits.Testbenches

import Chisel._
import fpgatidbits.rosetta._
import fpgatidbits.PlatformWrapper._

class TestDotProduct(p: PlatformWrapperParams) extends GenericAccelerator(p) {
    val data_width = 12
    val numMemPorts = 0
    val io = new GenericAcceleratorIF(numMemPorts, p) {
        val input_data = Vec.fill(4){UInt(INPUT, data_width)}
        val input_weight = Vec.fill(4){UInt(INPUT, 1)}
        val output_data = SInt(OUTPUT, 12)
    }

    val DP = Module(new DotProduct(
        4,
        data_width
    )).io


    //DP.vec_1 := io.input_data
    DP.vec_1 := Vec(Array(100,100,100,100).map(s => UInt(s, width=8)))
    //DP.vec_2 := io.input_weight
    DP.vec_2 := Vec(Array(0,0,0,0).map(s => UInt(s, width=1)))
    io.output_data := DP.data_out
}
