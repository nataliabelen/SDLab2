package cl.usach.sd;
import java.util.Arrays;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Linkable;

/**
 * Clase que se encarga de inicializar los nodos en la red.
 * Estos nodos tienen una estructura de anillo, unidireccional.
 * Los datos de inicialización los trae del archivo de configuración.
 * Además imprime por pantalla los datos de inicialización.
 * 
 * @author Natalia
 *
 */
public class Initialization implements Control {
	String prefix;
	int idLayer;
	int idTransport;
	int cache;
	int database; 
	int dht;
	int n;
	int dhtMax; 
	/**
	 * Constructor de Iniatialization, que se encarga de leer desde el archivo de configuración
	 * los datos iniciales, como el tamaño de la base de datos, el d, etc.
	 * Además imprime por pantalla estos datos iniciales.
	 * @param prefix prefijo del cual extrae la información de entrada.
	 */
	public Initialization(String prefix) {
		this.prefix = prefix;
		this.n = Network.size();
		this.idLayer = Configuration.getPid(prefix + ".protocol");
		this.idTransport = Configuration.getPid(prefix + ".transport");
		this.cache = Configuration.getInt(prefix + ".cache");
		this.database = Configuration.getInt(prefix + ".database");
		int d = Configuration.getInt(prefix + ".dht");
		this.dhtMax = 1+2*d;
		System.out.println("Valores iniciales:\nn:	"+this.n+"\t[Tamaño de la red]\nb:	"+this.database+"\t[Tamaño de la Base de datos]\n"+
		"c:	"+this.cache+"\t[Tamaño del caché]\nd:	"+d+"\n---");
		
	}

	/**
	 * Ejecución de la inicialización donde crea la red física, modificando los vecinos para que sea un anillo circular
	 * unidireccionado y luego genera el arreglo de dht que indicará el overlay de la red.
	 */
	@Override
	public boolean execute() {
		/**Es conveniente inicializar los nodos, puesto que los nodos 
		 * son una clase clonable y si asignan valores desde el constructor
		 *  todas tomaran los mismos valores, puesto que tomaran la misma dirección
		 * de memoria*/
		//Se generan los vecinos físicos.
		int i ;
		for (i=0; i<this.n-1; i++){
			((Linkable) Network.get(i).getProtocol(0)).addNeighbor(Network.get(i+1));
		}
		((Linkable) Network.get(Network.size()-1).getProtocol(0)).addNeighbor(Network.get(0));
		
		//Se genera el valor de los dht correspondientos.
		this.dht = (int) (Math.log(this.n)/Math.log(2))-1;
		System.out.print("Calculos iniciales:\nDHT:\t"+this.dhtMax+"\nx:\t");
		int [] distances = new int[this.dht];
		int d; 
		for (i=1; i<=this.dht; i++){
			d = (int) (this.n/Math.pow(2.0,i*1.0));
			distances[i-1]=d;
			System.out.print(i+", ");
		}
		Arrays.sort(distances);
		System.out.print("[Valores de x, 2^x<n]\nDistancias:\t");
		for( i=0; i<this.dht; i++){
			System.out.print(distances[i]+", ");
		}
		
		for(i=0; i<this.n; i++){
			((Peer) Network.get(i)).start(this.dht, this.cache, this.database);
			((Peer) Network.get(i)).createDHT(distances);
			((Peer) Network.get(i)).createDB(i*this.database); //*2
		}
		
		//Imprimiendo el encabezado.
		System.out.print("\n---\nInicializando la red:\nID\t|Vecino\t|DHT");
		for(i=0; i<this.dht; i++){
			System.out.print("\t");
		}
		System.out.print("|BD");
		for(i=0; i<this.database; i++){
			System.out.print("\t");
		}
		System.out.println("|CACHE");
		for (i=0; i<Network.size(); i++){
			System.out.println(((Peer)Network.get(i)).show_all());
		}
		return true;
	}
}
