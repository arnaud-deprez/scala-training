def testCallByValue(x:Int, y:Int) = x * x
def testCallByName(x: => Int, y: => Int) = x * x
//Same perf
testCallByValue(2, 3)
testCallByName(2, 3)

//Call by value better
testCallByValue(3+4,8)

//Call by name better because y is not used
testCallByName(7,2*4)

// Same perf
testCallByValue(3+4,2*4) // (7, 8) = 7 * 7 = 49 (3 steps)
testCallByName(3+4,2*4) // (3+4)*(3+4) = 7 * 7 = 49 (3 steps)