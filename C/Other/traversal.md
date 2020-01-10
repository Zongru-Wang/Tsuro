## Kotlin Labyrinth Interface Specification


In order to create a Labyrinth in Kotlin, one should have a few things. Firstly, an interface with at least these few functions... 


* A createLabrynth function that allows the user to create a simple graph with any amount of nodes. 
* An addColoredToken function that takes in an rgb value and a node-id and puts that token on that node. 
* A queryPath function that takes in a colored token and a destination node and returns a boolean that determines whether any colored token of that color can reach that node in the graph. 


You must use Kotlin version 1.3 for this project. It is expected that you may need to use either kotlin or java packages in order to complete the project. However, you must make sure the compiled code runs on the CCIS machines. 


### Data
* Booleans 
* An RGB value