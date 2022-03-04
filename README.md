# ChatApplication
A Multithreaded Chat Application on localhost

Aplicatie Chat Multithreadding, pe acelasi computer.

Componenta Server are o lista cu clientii care se conecteaza si deschide cate un Thread separat pentru fiecare client.
In momentul in care se conecteaza un client, ii transmite cine mai este conectat in acel moment si, dupa ce primeste numele acestuia, il salveaza in lista.
Cand primeste un mesaj de la un client, analizeaza catre cine se doreste a fi transmis mesajul si actioneaza in consecinta.
In momentul in care un client decide sa se deconecteze, acesta este scos din lista.
Apoi sunt anuntati ceilalti clienti conectati.

Componenta Client are Thread-uri separate pentru primire, respectiv transmitere mesaje.
Dupa conectarea la server, componenta Client primeste o lista cu toti ceilalti clienti conectati, pe care o salveaza in propria lista.
Apoi, transmite catre server numele sau.
Cand vrea sa transmita un mesaj, clientul trebuie sa specifice catre cine doreste sa transmita respectivul mesaj.
Daca un alt client se conecteaza sau se deconecteaza de la Server, acesta este adaugat, respectiv scos din lista de clienti conectati.
