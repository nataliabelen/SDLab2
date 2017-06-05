README:
Laboratorio 2 de Sistemas Distribuidos 2017-1
Profesor: Daniel Wladdimiro 
Ayudante: Maximiliano P�rez.
Integrante: Natalia P�rez.


Este laboratorio consiste en simular un anillo unilateral en una red peer to peer.
Algunas variables que se pueden modificar para probar esta simulaci�n en config/config-example.cfg, son:
l�nea 4: SIZE que especifica la cantidad de nodos que esta red tendr� (este valor ser� constante a lo largo de la simulaci�n)
l�nea 6: c es el tama�o de la cach� que tendr�n los peer
l�nea 8: b es el tama�o de la base de datos
l�nea 10: d es un n�mero que condiciona el tama�o del dht, 1+2d y con l condici�n de que 2^x<SIZE
l�nea 16: CYCLES es la cantidad de cycle que habr� en la simulaci�n
l�nea 18: CYCLE es la cantidad de tiempo transcurrido que habr� en un ciclo (se muestra por la aparici�n de current time en consola)
l�nea 41: OBSERVER_STEP que es la cantidad de tiempo transcurrido en la cual vuelve a llamar al observer (se muestra la informaci�n de los nodos por pantalla)
l�nea 42: TRAFFIC_STEP que es la cantidad de tiempo transcurrido en la cual vuelve a llamar al trafficGenerator, el cual genera nuevos mensajes en la red (nuevas acciones)

Como consejo es que al ejecutar "Run Configurations" ir a "Common" y clickear en Output file :${workspace_loc:/SDLab2}/output.txt

Con esta informaci�n ya es posible echar a correr el laboratorio.
y as� se guardar� el archivo de salida, lo cual ser� m�s f�cil al momento de evaluar como se comporta la red.

Datos del laboratorio:
El Peer que es el nodo de la red.
Al inicializar la red se configura que sea un anillo unidireccional. luego en trafficGeneration se inicia con s�lo un nodo realizando una consulta.
Luego en el layer est� toda la informaci�n de como se manejan los eventos que en este caso son los mensajes enviados en la red.
el Observer se encarga de cada cierto tiempo ir mostrando esa informaci�n actualizada de los nodos.

Adem�s en la carpeta /doc se encuentra el javadoc generado, para mejor visi�n de las clases, m�todos, etc.