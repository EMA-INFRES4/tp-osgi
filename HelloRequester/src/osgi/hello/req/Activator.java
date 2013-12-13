/*
 * Apache Felix OSGi tutorial.
 **/

package osgi.hello.req;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import osgi.hello.HelloService;

/**
 * This class implements a simple bundle that utilizes the OSGi framework's
 * event mechanism to listen for service events. Upon receiving a service event,
 * it prints out the event's details.
 **/
public class Activator implements BundleActivator, ServiceListener, Runnable {

	private boolean end;
	public static BundleContext ctx;
	Thread th;
	/**
	 * Implements BundleActivator.start(). Prints a message and adds itself to
	 * the bundle context as a service listener.
	 * 
	 * @param context
	 *            the framework context for the bundle.
	 **/
	public void start(BundleContext context) {
		ctx = context;
		end = false;
		th = new Thread(this);
		th.start();
	}

	/**
	 * Implements BundleActivator.stop(). Prints a message and removes itself
	 * from the bundle context as a service listener.
	 * 
	 * @param context
	 *            the framework context for the bundle.
	 **/
	public void stop(BundleContext context) {
		ctx.removeServiceListener(this);
		System.out.println("Stopped listening for service events.");
		end = true;
		th.stop();
		// Note: It is not required that we remove the listener here,
		// since the framework will do it automatically anyway.
	}

	/**
	 * Implements ServiceListener.serviceChanged(). Prints the details of any
	 * service event from the framework.
	 * 
	 * @param event
	 *            the fired service event.
	 **/
	public void serviceChanged(ServiceEvent event) {
		String[] objectClass = (String[]) event.getServiceReference()
				.getProperty("objectClass");

		if (event.getType() == ServiceEvent.REGISTERED) {
			System.out.println("Ex1: Service of type " + objectClass[0]
					+ " registered.");
		} else if (event.getType() == ServiceEvent.UNREGISTERING) {
			System.out.println("Ex1: Service of type " + objectClass[0]
					+ " unregistered.");
		} else if (event.getType() == ServiceEvent.MODIFIED) {
			System.out.println("Ex1: Service of type " + objectClass[0]
					+ " modified.");
		}
	}

	@Override
	public void run(){
		ServiceReference refs[] = null;
		if(ctx == null){
			System.out.println("Context is not defined");
			return;
		}
		while(!end){
			try {
				refs = ctx.getServiceReferences(HelloService.class.getName(), "(objectClass="
						+ HelloService.class.getName() + ")");
			} catch (InvalidSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//, "(objectClass="+
					//HelloService.class.getName() + ")");
			if(refs != null && refs.length != 0){
				for(ServiceReference sRef : refs){
					HelloService service = (HelloService) ctx.getService(sRef);
					System.out.println(service.sayHello("Xavier"));
				}
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}