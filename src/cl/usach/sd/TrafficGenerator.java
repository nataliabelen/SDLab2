package cl.usach.sd;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.edsim.EDSimulator;
/**
 * Clase que se encarga de generar tráfico en la red.
 * Utilizando un nodo aleatorio dentro de la red, con una query aleatoria también.
 * @author Natalia
 *
 */
public class TrafficGenerator implements Control {
	private final static String PAR_PROT = "protocol";
	private final int layerId;
	
	/**
	 * Constructor predeterminado de TrafficGenerator
	 * @param prefix prefijo del constructor por defecto
	 */
	public TrafficGenerator(String prefix) {
		layerId = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	/**
	 * Función que se ejecuta para generar tráfico en la red. Parte buscando un nodo al azar
	 * y luego genera un mensaje con una query aleatoria. 
	 * Luego el simulador de la red se encarga de enviar ese mensaje al nodo inicial.
	 */
	@Override
	public boolean execute() {
		// Consideraremos cualquier nodo de manera aleatoria de la red para comenzar y finalizar
		Peer initNode = (Peer)Network.get(CommonState.r.nextInt(Network.size())); 	
		System.out.println("Nodo inicial: "+ initNode.getID());
		Message message = initNode.send_message();
		System.out.println("["+message.getCreator()+","+message.getDestination()+","+message.getQuery()+"] Nodo "
				+initNode.getID()+" consultará a nodo "+message.getDestination()+" por "+message.getQuery()+ ":");
		EDSimulator.add(0, message, initNode, layerId);	
		return false;
	}
}
