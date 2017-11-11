#include <iostream>
using namespace std;

//#include "TestRegOps.hpp"
#include "TestBipolar.hpp"
#include "platform.h"

/*
bool Run_TestRegOps(WrapperRegDriver * platform) {
  TestRegOps t(platform);

  cout << "Signature: " << hex << t.get_signature() << dec << endl;
  cout << "Enter two operands to sum: ";
  unsigned int a, b;
  cin >> a >> b;

  t.set_op_0(a);
  t.set_op_1(b);

  cout << "Result: " << t.get_sum() << " expected: " << a+b << endl;

  return (a+b) == t.get_sum();
}
*/

bool Run_Bipolar(WrapperRegDriver * platform) {
    TestBipolar bp(platform);

    //cout << "Signature: " << hex << fc.get_signature() << dec << endl;
    /*
    cout << "Enter four numbers: ";
    int a, b, c, d;
    cin >> a >> b >> c >> d;

    int result0, result1, result2;
    result0 = a - b + c - d;
    result1 = a + b + c + d;
    result2 = - a - b - c - d;

    fc.set_input_data_0(a);
    fc.set_input_data_1(b);
    fc.set_input_data_2(c);
    fc.set_input_data_3(d);
    */

    cout << "Result 1: " << int(bp.get_output_0()) << endl; //". Expected: " << result0 << endl;
    cout << "Result 2: " << int(bp.get_output_1()) << endl; //". Expected: " << result0 << endl;
    cout << "Result 3: " << int(bp.get_output_2()) << endl; //". Expected: " << result0 << endl;
    cout << "Result 64: " << int(bp.get_output_63()) << endl; //". Expected: " << result0 << endl;

     //". Expected: " << result2 << endl;
}

int main()
{
  WrapperRegDriver * platform = initPlatform();

  //Run_TestRegOps(platform);
  Run_Bipolar(platform);

  deinitPlatform(platform);

  return 0;
}
