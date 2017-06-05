package cl.usach.sd;
import peersim.config.Configuration;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;
/**
 * Clase que se encarga de manejar los eventos (estos serían los mensajes) en la red.
 * @author Natalia
 *
 */
public class Layer implements Cloneable, EDProtocol {
	private static final String PAR_TRANSPORT = "transport";
	private static String prefix = null;
	private int transportId;
	//private int layerId;


	/**
	 * Constructor por defecto de la capa Layer del protocolo construido
	 * @param prefix prefijo que se utiliza para extraer los datos desde el archivo de configuración.
	 */
	public Layer(String prefix) {
		Layer.prefix = prefix;
		transportId = Configuration.getPid(prefix + "." + PAR_TRANSPORT);
		//Siguiente capa del protocolo
		//layerId = transportId + 1;
	}


	/**
	 * Función que se encarga de procesar el evento recibido por el peer. 
	 * El evento va a corresponder a un mensaje que tiene distinto tipo de mensaje
	 * ya sea cuando va en busqueda de nodo o cuando viene con la respuesta.
	 * Además se encarga de imprimir en pantalla lo que está haciendo el nodo especifico.
	 */
	@Override
	public void processEvent(Node myNode, int layerId, Object event) {
		/**Este metodo trabajará sobre el mensaje*/
		Message message = (Message) event;
		
		//Si está buscando la consulta.
		if(message.getType()==0){
			//Si soy el nodo al que debía llegar, osea, tengo la query
			if(myNode.getID()== message.getDestination()){
				System.out.println("\t["+message.getCreator()+","+message.getDestination()+","+message.getQuery()+"] Nodo "
						+myNode.getID()+" tiene respuesta a la consulta "+message.getQuery());
				message.setType(1);
				int idNode = message.removeToPath();			
				sendmessage(myNode, layerId, message, idNode);
				getStats();
				
			//Si no soy el destino, pero tengo en la caché guardada la query	
			}else if(((Peer)myNode).is_in_cache(message.getQuery())){
				System.out.println("\t["+message.getCreator()+","+message.getDestination()+","+message.getQuery()+"] Nodo "
						+myNode.getID()+" si tiene respuesta a la consulta "+message.getQuery()+" en caché (repetido)");
				//Si ya llegó el mensaje al creador.
				if(myNode.getID()== message.getCreator()){
					System.out.println("\t["+message.getCreator()+","+message.getDestination()+","+message.getQuery()+"] Nodo "
							+myNode.getID()+" recibió respuesta.");
				}
				else {
					int idNode = message.removeToPath();	
					message.setType(1);
					sendmessage(myNode, layerId, message, idNode);
					getStats();
					}
				//Si no tengo la query, debo mandarle el mensaje al que tenga menor distancia con el destinatario.
			}else{
				System.out.println("\t["+message.getCreator()+","+message.getDestination()+","+message.getQuery()+"] Nodo "
						+myNode.getID()+" no tiene respuesta a la consulta "+message.getQuery()+" en caché");
				int idNode =((Peer)myNode).calculate_distance(message, (int) ((Linkable)myNode.getProtocol(0)).getNeighbor(0).getID());
				message.addToPath((int)myNode.getID());
				sendmessage(myNode, layerId, message, idNode);
				getStats();
			}

		}
		
		//Si está respondiendo la consulta.
		else if(message.getType()==1){
			//guarda la respuesta
			System.out.println("\t["+message.getCreator()+","+message.getDestination()+","+message.getQuery()+"] Nodo "
					+myNode.getID()+" guarda respuesta a la consulta "+message.getQuery()+" en caché");
			((Peer)myNode).save_answer_FIFO(message.getDestination(), message.getQuery());
			//Si llegó la respuesta al que pedía la pregunta
			if(myNode.getID()== message.getCreator()){
				System.out.println("\t["+message.getCreator()+","+message.getDestination()+","+message.getQuery()+"] Nodo "
						+myNode.getID()+" recibió respuesta a la consulta "+message.getQuery());
			}
			//Si  no llega la consulta, debo mandarle el mensaje al nodo siguiente.
			else{
				int idNode = message.removeToPath();				
				sendmessage(myNode, layerId, message, idNode);
				getStats();
			}
		}
	}

	
	/**
	 * Función que va contando los mensajes en la red.
	 */
	private void getStats() {
		Observer.message.add(1);
	}

	/**
	 * Función que se enviará el mensaje de un nodo a otro vecino dentro de la red.
	 * @param currentNode es el nodo actual
	 * @param layerId capa
	 * @param msg es el mensaje que viene como objeto, por lo cual se debe trabajar sobre él
	 * @param idNode es el identificador del nodo al cual se envía el mensaje
	 */
	public void sendmessage(Node currentNode, int layerId, Object msg, int idNode) {
		Message message = (Message)msg;		
		Node sendNode = Network.get(idNode);
		String accion="";
		if(message.getType()==0)  accion= "consulta";
		else  accion= "respuesta";
		((Transport) currentNode.getProtocol(transportId)).send(currentNode, sendNode, message, layerId);
		System.out.println("\t["+message.getCreator()+","+message.getDestination()+","+message.getQuery()+"] Nodo "
				+currentNode.getID()+" envía "+accion+" a nodo "+sendNode.getID());
	}


	/**
	 * Definir Clone() para la replicacion de protocolo en nodos
	 */
	public Object clone() {
		Layer dolly = new Layer(Layer.prefix);
		return dolly;
	}
}
