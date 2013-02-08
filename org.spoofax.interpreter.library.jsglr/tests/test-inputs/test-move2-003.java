package tt;

public class Example {
	
	private void m(){
		extracted(v);
	}

	private void extracted(int v){
		if(v > 5){
			v++;
		}
		v+= 10;
		System.out.println(v);
	}
}