#include <iostream>
#include <fstream>
#include <string>
#include <typeinfo>
using namespace std;

#include "TestOutputQueue.hpp"
#include "platform.h"



bool Run_OutputQueue(WrapperRegDriver * platform) {
    TestOutputQueue q(platform);
    int vec [43];
    int c=0;
    int input[] = {1,2,3,4,5,6,7,8};
    for(int i = 0; i < 8; i++) {
      q.set_input_data_0(input[i]);
      q.set_input_pulse(1);
      q.set_input_pulse(0);
      while(q.get_empty() != 0){}
      q.set_output_pulse(1);
      q.set_output_pulse(0);
      vec[i] = q.get_output_data_0();
      cout << vec[i] << ", ";

    };

}

//Må legge til bilde fra Kamera som et argument i main.
int main()
{
  WrapperRegDriver * platform = initPlatform();

  Run_OutputQueue(platform);
  deinitPlatform(platform);
  return 0;
}

