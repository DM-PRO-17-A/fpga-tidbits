#include <iostream>
#include <fstream>
#include <string>
#include <typeinfo>
using namespace std;

#include "TestImageQueue.hpp"
#include "platform.h"



bool Run_ImageQueue(WrapperRegDriver * platform, int imageList[3072]) {
    TestImageQueue q(platform);

    int num_its = 1;
    unsigned int pixel_adder[8];
    for(int j = 0; j < num_its; j++){
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

  Run_ImageQueue(platform, imageList);

  deinitPlatform(platform);

  return 0;
}

