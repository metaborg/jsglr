package tt;

public class Example {
	private void m(){
		int i;
		while(i > 5){
			print(i);
			if(i > 10){
				i = i + 5;
				break;
			}
			i++;
		}
	}
}