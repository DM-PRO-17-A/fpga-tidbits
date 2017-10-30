package fpgatidbits.Testbenches

import Chisel._
import fpgatidbits.rosetta._
import fpgatidbits.PlatformWrapper._


class TestFullyConnected(p: PlatformWrapperParams) extends GenericAccelerator(p) {
    val data_width = 12
    val output_width = math.ceil(math.log(4 * math.pow(2, data_width)) / math.log(2)).toInt + 1

    val numMemPorts = 0
    val io = new GenericAcceleratorIF(numMemPorts, p) {
        val input_data = Vec.fill(4){UInt(INPUT, data_width)}

        val output_data = Vec.fill(3){SInt(OUTPUT, data_width)}
    }

    val FC = Module(new FullyConnected(
        3,
        Array(Array(1,0,1,0), Array(1,1,1,1), Array(0,0,0,0)),
        4,
        4,
        3,
        data_width)).io

    //FC.input_data := io.input_data
    FC.input_data := Array(100, 100, 100, 100).map(s => UInt(s))
    io.output_data := FC.output_data.bits
}
