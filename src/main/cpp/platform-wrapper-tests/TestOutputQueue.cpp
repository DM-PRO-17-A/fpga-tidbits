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
    for(int i = 2; i < 346; i += 8) {
        vec[c] = q.get_output_data(i);
        q.get_output_pulse(1);
        q.get_output_pulse(0);
        c=c+1;
        }
}

//Må legge til bilde fra Kamera som et argument i main.
int main()
{
  WrapperRegDriver * platform = initPlatform();

  Run_OutputQueue(platform);
  deinitPlatform(platform);
  return 0;
}

