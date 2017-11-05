package fpgatidbits.Testbenches

import Chisel._
import fpgatidbits.PlatformWrapper._
import fpgatidbits.regfile._
import fpgatidbits.ocm._

class TestRFQueue(p: PlatformWrapperParams) extends GenericAccelerator(p) {
    val dataWidth = 32
    val queueDepth = 32
    val vec_fill_size = 4

    val numMemPorts = 0
    val idBits = log2Up(16)
    val dataBits = 32
    val io = new GenericAcceleratorIF(numMemPorts, p) {
        val regFileIF = new RegFileSlaveIF(idBits, dataBits)

        //val queue_input = Flipped(Decoupled(UInt(INPUT, width = dataWidth)))
        val queue_output = (Decoupled(UInt(OUTPUT, width = dataWidth)))       //Valid and bits are outputs.count
        val queue_count = UInt(OUTPUT)

    }

    val testQueue = Module(new FPGAQueue(UInt(width = dataWidth), entries = queueDepth))

    //RegFile(numRegs: Int, idBits: Int, dataBits: Int) - databits = width
    val regFile = Module(new RegFile(2, idBits, dataBits)).io


    io.regFileIF <> regFile.extIF

    testQueue.io.enq.bits := io.regFileIF.readData.bits
    testQueue.io.enq.valid := io.regFileIF.readData.valid


    val toggle_valid = Reg(init=Bool(false))
    val last_valid = Reg(init=Bool(false), next=Mux(toggle_valid === io.regFileIF.cmd.valid, Bool(false), Bool(true)))
    //val current_valid = Reg(init=Bool(false), next=Mux())
    //last_valid := io.regFileIF.cmd.valid
    regFile.extIF.cmd.bits.read := last_valid

    /*
    when (toggle_valid != io.regFileIF.cmd.valid) {
        //toggle_valid := ~toggle_valid
        regFile.extIF.cmd.bits.read := Bool(true)
    }     .otherwise {
        regFile.extIF.cmd.bits.read := Bool(false)
    }
    */

    toggle_valid := io.regFileIF.cmd.valid

    //printf("Her kommer data. Toggle: %b RegFileValid: %b Whatever: %b\n", toggle_valid, io.regFileIF.cmd.valid, testQueue.io.enq.bits)
    printf("Last Valid: %b Read Data Valid: %b Read Data bits: %d TestIODeq: %d, QueueCount: %d  \n", last_valid, io.regFileIF.readData.valid, io.regFileIF.readData.bits, testQueue.io.deq.bits, io.queue_count)
    //printf("Her kommer data. Toggle: %b Valid: %b Whatever: %b\n", io.regFileIF.cmd.valid, testQueue.io.enq.valid, testQueue.io.enq.bits)


    io.queue_output <> testQueue.io.deq
    testQueue.io.count <> io.queue_count



}


class RegFileTests(c: TestRFQueue) extends Tester(c){
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

}
