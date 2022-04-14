# Securing an event streaming platform for IoT - Apache Kafka

Apache Kafka est une dérivation de l'architecture publish/subscribe, servant notamment à la communication entre objets connectés. Un producteur va publier une donnée sous la forme (topic,message) où le "topic" est l'identifiant du canal. Cette architecture permet d'analyser un flux de données en streaming et d'agir en fonction des valeurs reçues au niveau des serveurs d'interconnexion pour les acheminer vers des consommateurs intéressés par les flux.
 
Le projet consiste à étudier le fonctionnement ainsi que la sécurité d'Apache Kafka, puis d'adapter un schéma d'encrypted matching afin de masquer les "topics" au serveur pour renforcer la confidentialité des échanges et ce, tout en permettant aux utilisateurs autorisés de s'abonner à ses flux. Une preuve de concept devra être développée pour évaluer la solution retenue.

Un groupe composé d'étudiants des parcours informatique et mathématique est possible.

https://blog.zenika.com/2017/09/14/mais-cest-quoi-apache-kafka/

https://kafka.apache.org/documentation/streams/

https://medium.com/@stephane.maarek/introduction-to-apache-kafka-security-c8951d410adf

Encadrants: Mathieu Klingler et Emmanuel Conchon
