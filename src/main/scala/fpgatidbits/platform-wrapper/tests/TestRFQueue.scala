package fpgatidbits.Testbenches

import Chisel._
import fpgatidbits.PlatformWrapper._
import fpgatidbits.regfile._
import fpgatidbits.ocm._

class TestRFQueue(p: PlatformWrapperParams, dataWidth: Int, queueDepth: Int, vec_fill_size: Int, num_of_regs: Int) extends GenericAccelerator(p) {
    val numMemPorts = 0
    val idBits = log2Up(num_of_regs)
    val io = new GenericAcceleratorIF(numMemPorts, p) {
        val input_data = UInt(INPUT,width=dataWidth)
        val input_pulse = Bool(INPUT)

        //val queue_input = Flipped(Decoupled(UInt(INPUT, width = dataWidth)))
        val queue_output = (Decoupled(UInt(OUTPUT, width = dataWidth)))       //Valid and bits are outputs.count
        val queue_count = UInt(OUTPUT)
        val queue_full = Bool(OUTPUT)
    }

    val testQueue = Module(new FPGAQueue(UInt(width = dataWidth), entries = queueDepth))
    val regFile = Module(new RegFile(2, idBits, dataWidth)).io
    val toggle_pulse = Reg(init=Bool(false))
    val next_read = Reg(init=Bool(false), next=Bool(!(toggle_pulse === io.input_pulse)))

    toggle_pulse := io.input_pulse

    regFile.extIF.cmd.bits.regID := UInt(0)
    regFile.extIF.cmd.bits.writeData := io.input_data
    regFile.extIF.cmd.bits.read := next_read
    regFile.extIF.cmd.bits.write := UInt(1)
    regFile.extIF.cmd.valid := (toggle_pulse === io.input_pulse)

    testQueue.io.enq.bits := regFile.extIF.readData.bits
    testQueue.io.enq.valid := next_read

    printf("Next ready: %b Input pulse: %b Read Data bits: %d  EnQ: %d TestIODeq: %d, QueueCount: %d, QFull: %b  \n", next_read, io.input_pulse, io.input_data, testQueue.io.enq.bits, testQueue.io.deq.bits, io.queue_count, io.queue_full)

    io.queue_output <> testQueue.io.deq
    testQueue.io.count <> io.queue_count
    io.queue_full := !testQueue.io.enq.ready



}


class RegFileTests(c: TestRFQueue) extends Tester(c){
    /*
    val regFile = c.io.regFileIF

    poke(regFile.cmd.bits.regID, 0)
    poke(regFile.cmd.bits.read, 0)
    poke(regFile.cmd.bits.write, 1)
    poke(regFile.cmd.bits.writeData, 5)
    poke(regFile.cmd.valid, 1)
    step(1)
    poke(regFile.cmd.valid, 0)
    peek(regFile)
    step(1) // allow the command to propagate and take effect
    poke(regFile.cmd.bits.read, 1)
    poke(regFile.cmd.bits.write, 0)
    poke(regFile.cmd.valid, 1)
    step(1)
    poke(regFile.cmd.valid, 0)
    step(1)
    //peek(regFile.readData)
    //step(1)
    peek(c.testQueue.io.enq.bits)
    step(1)
    peek(c.io.queue_count)
    step(1)
    //Lese output fra FPGAQUEUE
    poke(c.io.queue_output.ready, 1)
    peek(c.io.queue_output)
    step(1)
    peek(c.io.queue_output)
    */
}
