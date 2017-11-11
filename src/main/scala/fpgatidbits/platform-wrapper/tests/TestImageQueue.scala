package fpgatidbits.Testbenches

import Chisel._
import fpgatidbits.PlatformWrapper._
import fpgatidbits.rosetta._
import fpgatidbits.ocm._

class TestImageQueue (p: PlatformWrapperParams) extends GenericAccelerator(p) {
    val vec_size = 8
    val data_width = 8
    val queue_depth = 384

    val numMemPorts = 0
    val io = new GenericAcceleratorIF(numMemPorts, p){
        val input_data = Vec.fill(vec_size){UInt(INPUT, width=data_width)}
        val input_pulse = Bool(INPUT)

        val full = Bool(OUTPUT)
    }

    val queue = Module(new ImageQueue(data_width, queue_depth, vec_size)).io

    queue.input_data <> io.input_data
    queue.input_pulse <> io.input_pulse
    queue.full <> io.full
    
    val counter = Reg(init=UInt(width=8))
    counter := counter + UInt(1)
    when(counter > UInt(queue_depth)){
        queue.output_data.ready := Bool(true)
        when(queue.output_data.valid){
            printf("Output: %d\n", queue.output_data.bits(0))
        }
    }.otherwise{
        queue.output_data.ready := Bool(false)
    }
}
