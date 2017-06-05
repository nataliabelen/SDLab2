package cl.usach.sd;

import java.util.Random;
import peersim.core.GeneralNode;
import peersim.core.Linkable;
import peersim.core.Network;
/**
 * Peer se utiliza como el nodo base en esta red simulada de peer to peer.
 * Tiene los atributos:
 * dht: que corresponde al arreglo de vecinos en el overlay.
 * database: que corresponde al arreglo de la base de datos, con las query que cada nodo tiene.
 * cache: corresponde al arreglo de cach� que tiene el nodo.
 * Los getters y setters no se especificar�n por simplicidad.
 * @author Natalia
 *
 */
public class Peer extends GeneralNode {
	private int[] dht; 
	private int[] database;
	private int[][] cache; 


/**
 * 	Constructor de Peer por defecto
 * @param prefix prefijo del constructor por defecto
 */
	public Peer(String prefix) {
		super(prefix);
	}
	
	public int[] getDht() {
		return dht;
	}
	public void setDht(int[] dht) {
		this.dht = dht;
	}
	public int[] getDatabase() {
		return database;
	}
	public void setDatabase(int[] database) {
		this.database = database;
	}
	public int[][] getCache() {
		return cache;
	}
	public void setCache(int[][] cache) {
		this.cache = cache;
	}

	/**
	 * Funci�n que inicializa los arreglos de dht, database y cache
	 * de acuerdo a los parametros de tama�o ingresados.
	 * @param dhtSize Tama�o del arreglo de dht
	 * @param cacheSize Tama�o de la cach�
	 * @param databaseSize Tama�o de la base de datos.
	 */
	public void start(int dhtSize, int cacheSize, int databaseSize ){
		dht = new int[dhtSize];
		database = new int[databaseSize];
		cache = new int[cacheSize][3];
		int i;
		for (i = 0 ; i<cache.length; i++){
			cache[i][0]= -1; // Node
			cache[i][1]= -1; //Query
			cache[i][2]= 0; //FIFO 
		}

	}
	
	public void createDHT(int [] distances){
		int i;
		for (i = 0 ; i<dht.length; i++){
			dht[i]= (int) (this.getID() + distances[i])%Network.size();
		}
	}
	/**
	 * Funci�n que almacena los datos en la base de datos.
	 * Desde un n�mero inicial, hasta completar la base de datos.
	 * @param start entero que contiene la primera query que tiene el peer.
	 */
	public void createDB(int start){
		int i;
		for (i = 0 ; i<database.length; i++){
			database[i]= start+i; //*2
		}
	}
	/**
	 * Funci�n que se encarga de guardar la query en la cach�.
	 * Sigue la politica de reemplazo de FIFO
	 * @param node id del nodo que tiene la query originalmente
	 * @param query consulta que se quiere guardar en cach�
	 */
	public void save_answer_FIFO(int node, int query){
		int min= cache[0][2];
		int max = cache[0][2];
		for(int i=1; i<this.cache.length; i++){
			if(min>cache[i][2]) min  = cache[i][2];
			if(max<cache[i][2]) max = cache[i][2];
		}
		for(int i=0; i<this.cache.length; i++){
			if(min==cache[i][2]) {
				cache[i][0]=node;
				cache[i][1]=query;
				cache[i][2]= max+1;
				return;
			}
		}
	}

	/**
	 * Funci�n que crea un mensaje aleatorio para que sea enviado por la red.
	 * @return Mensaje que se enviar� en la red.
	 */
	public Message send_message(){
		int total, query, node;
		total= Network.size()*database.length;
		Random random = new Random();
		boolean igual=true;
		do{
			query =random.nextInt(total);
			node = (int) (query/database.length);
			if(node !=this.getID()) igual=false;
		}while (igual);
		//System.out.println("Query: "+query+", Node: "+node);
		Message message = new Message(query,node,(int)this.getID());
		
		return message;
		
		
	}
	/**
	 * Funci�n que evalua si una consulta se encuentra almacenada en cach�
	 * @param query consulta que se evaluar� si est� en la cach� del nodo o no
	 * @return Boolean true si est� en cach� y false si no est� en la cach�.
	 */
	public boolean is_in_cache(int query){
		for(int i=0; i<this.cache.length; i++){
			if(cache[i][1]==query){
				return true;
			}
		}
		return false;
	}

	/**
	 * Funci�n que retorna el id del nodo que tiene la distancia minima para llegar a la consulta
	 * Adem�s muestra la informaci�n.
	 * @param message Mensaje que contiene la informaci�n de la query que se busca.
	 * @param vecino id del vecino que tiene el nodo.
	 * @return int que indica el id del nodo m�s cercano.
	 */
	public int calculate_distance(Message message, int vecino){
		String distancias="";
		int i, dmin, d, nmin;
		nmin=vecino;
		dmin= (int) (message.getDestination()-this.getID());
		if(dmin<0) dmin=dmin+Network.size();
		for(i=0; i<dht.length; i++){
			distancias= distancias +dht[i]+", ";
			d=(int) (message.getDestination()-dht[i]);
			if(d<0) d=d+Network.size();
			if(dmin>d) {
				dmin=d;
				nmin=dht[i];
			}
		}
		distancias=distancias+vecino+" ";
		//Mostrando la informaci�n.
		System.out.println("\t["+message.getCreator()+","+message.getDestination()+","+message.getQuery()+"] Nodo "
				+this.getID()+" calcula distancias con "+ distancias);
		System.out.println("\t["+message.getCreator()+","+message.getDestination()+","+message.getQuery()+"] Nodo "
				+this.getID()+" tiene minima distancia con nodo "+ nmin+" , con distancia "+ dmin);
		return nmin;
	}
	
	/**
	 * Funci�n asociada al nodo
	 * Captura la informaci�n asociada al nodo, como el vecino, los nodos en dht , las consultas en la base de datos y en la cach�
	 * @return Retorna un String que contiene toda la informaci�n del nodo.
	 */
	public String show_all(){
		int i; 
		String text=this.getID() + "\t|";
		for(i = 0; i < ((Linkable) this.getProtocol(0)).degree(); i++) {
			text=text+ ((Linkable) this.getProtocol(0)).getNeighbor(i).getID()+"\t";
		}
		text= text +"\t|";
		for (i=0; i< dht.length-1; i++){
			text = text +dht[i]+", \t ";
		}
		text = text +dht[dht.length-1]+"\t|";
		for (i=0; i< database.length-1; i++){
			text = text +" "+database[i]+", \t ";
		}
		text = text +database[database.length-1]+" \t|";
		for (i=0; i< cache.length; i++){
			String cachecito ="";
			if (cache[i][0]==-1) cachecito ="[]";
			else cachecito = "["+cache[i][0]+","+cache[i][1]+"] ("+cache[i][2]+")";
			text = text +cachecito+"\t";
		}
		return text;
	}



}