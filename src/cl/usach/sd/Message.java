package cl.usach.sd;
import java.util.Stack;

/**
 * 
 * Clase la cual vamos a utilizar para enviar datos de un Peer a otro
 * contiene los atributos :
 * query: valor del mensaje, que indica la consulta que se está haciendo
 * destination: id del nodo de destino
 * type: el tipo de mensaje, 
 * 		0: es un mensaje que está consultando la query
 * 		1: es un mensaje que está respondiendo la query
 * creator: id del nodo que crea el mensaje
 * path: es el camino que ha recorrido el mensaje, el cual sirve para el retorno una vez que se
 * obtiene la query.
 * Las demás funciones son getters y setters que no se explicarán por simplicidad.
 * @author Natalia
 */

public class Message {
	private int query;
	private int destination;
	private int type; //0 ida, 1 vuelta.
	private int creator;
	private Stack<Integer> path;  


	/**
	 * Constructor del mensaje, por defecto iniciará con type 0
	 * @param query value será el valor que tendrá el mensaje
	 * @param destination el id del nodo del destino
	 * @param creator id del nodo creador.
	 */
	public Message(int query,  int destination, int creator) {

		this.setQuery(query);
		this.setDestination(destination);
		this.setCreator(creator);
		this.setType(0);
		Stack <Integer> path = new Stack <Integer>();
		this.setPath(path);
	}
	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}
	public int getQuery() {
		return query;
	}

	public void setQuery(int query) {
		this.query = query;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Stack<Integer> getPath() {
		return path;
	}

	public void setPath(Stack<Integer> path) {
		this.path = path;
	}
	/**
	 * Agrega un id del nodo al camino del mensaje.
	 * @param node, entero que indica el id del node que se agrega al camino
	 */
	public void addToPath(int node){
		this.path.push(node);

	}
	
	/**
	 * Función que elimina el último nodo del camino, en forma lifo, 
	 * ya que va retrocediendo en el camino.
	 * @return retorna el valor del último nodo del camino.
	 */
	public int removeToPath(){
		return this.path.pop();
	}
	/**
	 * Función que sirve para mostrar el camino y ver que se está actualizando bien.
	 */
	public void show(){
		System.out.print("Camino actualizado: ");
		for (int number : path) {
			System.out.print(number+" ");
		}
	}
}
