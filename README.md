## Instructions
* Install jdk and maven
  * apt-get install default-jdk maven -y
* Clone the code
  * git clone https://github.com/thechetantalwar/azure-tables-java
  * cd azure-tables-java
* Update your connection string in src/main/resources/config.properties
* Compile it
  * mvn install
* Execute it
  * java -jar target/storage-java-table-0.0.1-SNAPSHOT-jar-with-dependencies.jar
