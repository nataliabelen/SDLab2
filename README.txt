README:
Laboratorio 2 de Sistemas Distribuidos 2017-1
Profesor: Daniel Wladdimiro 
Ayudante: Maximiliano Pérez.
Integrante: Natalia Pérez.


Este laboratorio consiste en simular un anillo unilateral en una red peer to peer.
Algunas variables que se pueden modificar para probar esta simulación en config/config-example.cfg, son:
línea 4: SIZE que especifica la cantidad de nodos que esta red tendrá (este valor será constante a lo largo de la simulación)
línea 6: c es el tamaño de la caché que tendrán los peer
línea 8: b es el tamaño de la base de datos
línea 10: d es un número que condiciona el tamaño del dht, 1+2d y con l condición de que 2^x<SIZE
línea 16: CYCLES es la cantidad de cycle que habrá en la simulación
línea 18: CYCLE es la cantidad de tiempo transcurrido que habrá en un ciclo (se muestra por la aparición de current time en consola)
línea 41: OBSERVER_STEP que es la cantidad de tiempo transcurrido en la cual vuelve a llamar al observer (se muestra la información de los nodos por pantalla)
línea 42: TRAFFIC_STEP que es la cantidad de tiempo transcurrido en la cual vuelve a llamar al trafficGenerator, el cual genera nuevos mensajes en la red (nuevas acciones)

Como consejo es que al ejecutar "Run Configurations" ir a "Common" y clickear en Output file :${workspace_loc:/SDLab2}/output.txt

Con esta información ya es posible echar a correr el laboratorio.
y así se guardará el archivo de salida, lo cual será más fácil al momento de evaluar como se comporta la red.

Datos del laboratorio:
El Peer que es el nodo de la red.
Al inicializar la red se configura que sea un anillo unidireccional. luego en trafficGeneration se inicia con sólo un nodo realizando una consulta.
Luego en el layer está toda la información de como se manejan los eventos que en este caso son los mensajes enviados en la red.
el Observer se encarga de cada cierto tiempo ir mostrando esa información actualizada de los nodos.

Además en la carpeta /doc se encuentra el javadoc generado, para mejor visión de las clases, métodos, etc.