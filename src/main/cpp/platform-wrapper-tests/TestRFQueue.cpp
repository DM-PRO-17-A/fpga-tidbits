#include <iostream>
#include <fstream>
#include <string>
#include <typeinfo>
using namespace std;

#include "TestRFQueue.hpp"
#include "platform.h"



bool Run_RFQueue(WrapperRegDriver * platform, int imageList[3072]) {
    TestRFQueue rf(platform);

    unsigned int pixel_adder[8];
    for(int i = 0; i < 3072; i += 8) {

        while (rf.get_queue_full()) {
            //Waits for space in the Queue.
            goto slutt;
        }
        for(int p = 0; p < 8; p++) {
            pixel_adder[p] = imageList[i+p];
        }
        rf.set_input_data_0(pixel_adder[0]);
        rf.set_input_data_1(pixel_adder[1]);
        rf.set_input_data_2(pixel_adder[2]);
        rf.set_input_data_3(pixel_adder[3]);
        rf.set_input_data_4(pixel_adder[4]);
        rf.set_input_data_5(pixel_adder[5]);
        rf.set_input_data_6(pixel_adder[6]);
        rf.set_input_data_7(pixel_adder[7]);
        rf.set_input_pulse(1);
        rf.set_input_pulse(0);
    }
    slutt: ;
    rf.set_queue_output_ready(1);
    while(rf.get_queue_count() > 0) {
    }
    cout << "Done.";
}


int main()
{
  int x;
  int imageList[3072];
  int index = 0;

  ifstream testFile;
  testFile.open("/home/christoffer/Documents/vector.txt");

  if (!testFile) {
      cout << "Unable to open file";
      exit(1); // terminate with error
  }

  while (testFile >> x) {
      imageList[index] = x;
      index ++;
  }

  WrapperRegDriver * platform = initPlatform();

  Run_RFQueue(platform, imageList);

  deinitPlatform(platform);

  return 0;
}