#include <iostream>
#include <bitset>
using namespace std;

//#include "TestRegOps.hpp"
#include "TestDotProduct.hpp"
#include "platform.h"

bool Run_DotProduct(WrapperRegDriver * platform) {
    TestDotProduct dp(platform);

    //cout << "Signature: " << hex << fc.get_signature() << dec << endl;
    /*
    cout << "Enter four numbers: ";
    int a, b, c, d, e, f, g, h;
    cin >> a >> b >> c >> d;
    cout << "Enter four numbers: ";
    cin >> e >> f >> g >> h;

    dp.set_input_data_0(a);
    dp.set_input_data_1(b);
    dp.set_input_data_2(c);
    dp.set_input_data_3(d);

    dp.set_input_weight_0(e);
    dp.set_input_weight_1(f);
    dp.set_input_weight_2(g);
    dp.set_input_weight_3(h);

    int result;
    result = a*(2*e-1)+b*(2*f-1)+c*(2*g-1)+d*(2*h-1);
    */

    cout << "Result: " << bitset<12>(dp.get_output_data()) << ". Expected: " << endl;// << result << endl;
}

int main()
{
  WrapperRegDriver * platform = initPlatform();

  Run_DotProduct(platform);

  deinitPlatform(platform);

  return 0;
}
