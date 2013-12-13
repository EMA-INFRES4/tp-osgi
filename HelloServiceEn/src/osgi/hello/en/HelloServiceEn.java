package osgi.hello.en;

import osgi.hello.HelloService;

public class HelloServiceEn implements HelloService {

	public String sayHello(String nom) {
		return "Hello " + nom;
	}
}
