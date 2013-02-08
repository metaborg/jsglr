package tt;

public class Example {
	private void m(){
		int v = 10;
		if(v > 5){
			while(true){
				System.out.println(v);
				v++;
				v+=5;
				v+= 10;
				v+=20;
			}
		}
		System.out.println(v);
	}
}