package fpgatidbits.Testbenches

import Chisel._
import fpgatidbits.PlatformWrapper._
import fpgatidbits.rosetta._
import fpgatidbits.ocm._

class TestOutputQueue (p: PlatformWrapperParams) extends GenericAccelerator(p) {
    val vec_size = 43
    val data_width = 8
    val queue_depth = 384

    val numMemPorts = 0
    val io = new GenericAcceleratorIF(numMemPorts, p){
        val output_data = Vec.fill(vec_size){SInt(OUTPUT, width=data_width)}
        val output_pulse = Bool(INPUT)

        val full = Bool(OUTPUT)
        val empty = Bool(OUTPUT)
    }
    val vec = Array(-12,-22,26,16,-4,-18,-26,-32,-70,2,-22,-2,-48,-6,130,56,-50,48,-22,18,-14,14,50,6,-16,56,-50,2,-18,14,-6,0,-4,22,10,-4,0,12,20,-2,-46,-34,-52)

    val queue = Module(new OutputQueue(data_width, queue_depth, vec_size)).io
    queue.input_data.bits := Vec(vec.map(s=> SInt(s)))   
    when (queue.input_data.ready){
        queue.input_data.valid := Bool(true)
    } .otherwise{
        queue.input_data.valid := Bool(false)
    }


    queue.output_pulse <> io.output_pulse
    queue.empty <> io.empty

    io.output_data <> queue.output_data

    
    //printf("V: %b R: %b D: %d \n", queue.output_data.valid, queue.output_data.ready, queue.output_data.bits(0))
}
