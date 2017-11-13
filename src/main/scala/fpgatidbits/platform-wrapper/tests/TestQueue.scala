package fpgatidbits.Testbenches

import Chisel._
import fpgatidbits.PlatformWrapper._
import fpgatidbits.ocm._


class TestQueue(p: PlatformWrapperParams, dataWidth: Int, queueDepth: Int, vec_fill_size: Int) extends GenericAccelerator(p) {
    val numMemPorts = 0
    val io = new GenericAcceleratorIF(numMemPorts, p) {
        val input_data = Vec.fill(vec_fill_size){UInt(INPUT, width=dataWidth)}
        val input_pulse = Bool(INPUT)

        val output_data = Decoupled(Vec.fill(vec_fill_size){UInt(OUTPUT, dataWidth)})
        val full = Bool(OUTPUT)
        val empty = Bool(OUTPUT)
    }

    val pulse_reg = Reg(next=io.input_pulse)
    val queue = Module(new FPGAQueue(Vec.fill(vec_fill_size){UInt(width=dataWidth)}, queueDepth))

    queue.io.enq.valid := !io.input_pulse && pulse_reg
    queue.io.enq.bits := io.input_data
    
    io.full := (queue.io.count === UInt(queueDepth - 1))
    io.empty := (queue.io.count === UInt(0))

    io.output_data <> queue.io.deq

    when(queue.io.enq.valid && queue.io.enq.ready){
        printf("ADD: %d, len: %d\n", queue.io.enq.bits(0), queue.io.count)
    }
    when(queue.io.deq.valid && queue.io.deq.ready){
        printf("POP: %d, len: %d\n", queue.io.deq.bits(0), queue.io.count)
    }
    //printf("EV: %b EB: %d, len: %d\n", queue.io.enq.valid, queue.io.enq.bits(0), queue.io.count)
}