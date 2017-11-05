#include <iostream>
using namespace std;

#include "TestRFQueue.hpp"
#include "platform.h"



bool Run_RFQueue(WrapperRegDriver * platform) {
    TestRFQueue rf(platform);

    rf.set_regFileIF_cmd_bits_regID(0);
    rf.set_regFileIF_cmd_bits_write(1);
    for(int i = 0; i < 32; i++) {
        rf.set_regFileIF_cmd_bits_writeData(i);
        rf.set_regFileIF_cmd_valid(i % 2);
    }
    //rf.set_regFileIF_cmd_bits_writeData(5);
    //rf.set_regFileIF_cmd_valid(1);
    //rf.set_regFileIF_cmd_bits_read(1);

    cout << "Elements in queue are: " << rf.get_queue_count() << endl;
    rf.set_queue_output_ready(1);
    for (int i = 0; i <= rf.get_queue_count(); i++) {
        cout << "Printing elements in queue: " << rf.get_queue_output_bits() << endl;

    }

}

int main()
{
  WrapperRegDriver * platform = initPlatform();

  Run_RFQueue(platform);

  deinitPlatform(platform);

  return 0;
}
