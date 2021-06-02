module elib/elib-utils/math

  function iff(a: Bool, b: Bool): Bool {
  	return (a && b) || (!a && !b);
  }
  
  function implies(a: Bool, b: Bool): Bool {
  	return !a || b;
  }

  function abs(i : Int) : Int {
    return if(i < 0) (0 - i) else i;
    //if(i < 0) { return 0 - i; } else { return i; }
  }
  
  function max(i : Int, j : Int) : Int {
    return if(i > j) i else j;
    //if(i > j) { return i; } else { return j; }
  }

  function max(i : Float, j : Float) : Float {
    return if(i > j) i else j;
    //if(i > j) { return i; } else { return j; }
  }
  
  function max(i : Double, j : Double) : Double {
    return if(i > j) i else j;
    //if(i > j) { return i; } else { return j; }
  }
  
  function min(i : Int, j : Int) : Int {
    return if(i > j) j else i;
    //if(i > j) { return j; } else { return i; }
  }

  function min(i : Float, j : Float) : Float {
    return if(i > j) j else i;
    //if(i > j) { return j; } else { return i; }
  }
  
  function min(i : Double, j : Double) : Double {
    return if(i > j) j else i;
    //if(i > j) { return j; } else { return i; }
  }
    
  function mod(i : Int, j : Int) : Int {
  	validate(j != 0, "modulo zero undefined");
    return i - (j * (i / j));
  }
  
  function inc(i: Int, b: Bool): Int {
    return if(b) i + 1 else i;
  	//if(b) { return i + 1; } else { return i; }
  }
  
  function percentage(part: Int, total: Int): Int {
    return if(total == 0) 0 else ((part.floatValue() * 100.0) / total.floatValue()).round();
  	// if(total == 0) { return 0; } else {
  	// 	return (part * 100) / total ;
  	// }
  }
  
  function round1(f: Float): Float{
  	return (10.0 * f).round().floatValue() / 10.0;
  }
  
  native class org.webdsl.tools.MedianList as MedianList {
    insert(Float)
    median():Float
    constructor()
  }
  
  native class org.webdsl.tools.LongToFloat as LongToFloat{
    static longToFloat(Long): Float
  }
  
  function sum(fs: List<Float>): Float {
  	var s := 0.0;
  	for(f: Float in fs) { s := s + f; }
  	return s;
  }
  
  function sum(ds: List<Double>): Double {
  	var s := Double(0.0);
  	for(d: Double in ds) { s := s + d; }
  	return s;
  }
  
  function and(bs: List<Bool>): Bool {
  	var s := true;
  	for(b: Bool in bs) { s := s && b; }
  	return s;
  }