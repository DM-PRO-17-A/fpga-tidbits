#include <iostream>
#include <fstream>
#include <string>
#include <typeinfo>
using namespace std;

#include "TestQueue.hpp"
#include "platform.h"



bool Run_Queue(WrapperRegDriver * platform, int imageList[3072]) {
    TestQueue q(platform);

    unsigned int pixel_adder[8];
    for(int j = 0; j < 2; j++){
    for(int i = 0; i < 3072; i += 8) {

        while (q.get_full()) {
            //Waits for space in the Queue.
            goto slutt;
        }
        for(int p = 0; p < 8; p++) {
            pixel_adder[p] = imageList[i+p];
        }
        q.set_input_data_0(pixel_adder[0]);
        q.set_input_data_1(pixel_adder[1]);
        q.set_input_data_2(pixel_adder[2]);
        q.set_input_data_3(pixel_adder[3]);
        q.set_input_data_4(pixel_adder[4]);
        q.set_input_data_5(pixel_adder[5]);
        q.set_input_data_6(pixel_adder[6]);
        q.set_input_data_7(pixel_adder[7]);
        q.set_input_pulse(1);
        q.set_input_pulse(0);
    }
    slutt: ;
    q.set_output_data_ready(1);
    while(q.get_empty() != 1) {
    }
    cout << "Done.";
    q.set_output_data_ready(0);
    }
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

  Run_Queue(platform, imageList);

  deinitPlatform(platform);

  return 0;
}
