package tt;

public class Example {
	private void m(){
		int v = 10;
		extracted(v);
		v+= 10;
		v += 11;
		System.out.println(v);
	}
	private void extracted(int v){
		if(v > 5){
			v++;
		}				
	}
}