# Labyrinth
A Labyrinth is a simple graph.  Each node in the graph has at most one outgoing node.  The following three operations must be supported.
1. Creation  of  a  plain  labyrinth  with  named  nodes.
2. Addition  of  colored  tokens  to  a  node.
3. Querying  whether  a  colored  token  can  reach  a  named  node.
Labyrinth is to be implemented using Java 8.

### ILabyrinth  Interface
Define a class that implements the `ILabyrinth` interface.

#### If any function took in empty String as an argument, then throw IllegalArgumentException

- `List<String>  getAllNodeNames()`
	- Returns  a  `List`  containing  the  names  of  all  nodes  in  this  labyrinth.

- `List<String>  getConnectedNodeNames(String  name)`
	- Returns  a  `List`  containing  the  names  of  all  nodes  connected  to  the  node  named  `name`.
	- If  this  labyrinth  does  not  contain  a  node  named  `name`,  throw  an  `IllegalArgumentException`.

- `void  addNode(String  name)`
	- Mutates  this  labyrinth  by  adding  a  new  node  named  `name`.
	- If  there  is  already  a  node  in  the  labyrinth  named  `name`,  throw  an  `IllegalArgumentException`.

- `void  connectNode(String  nodeName1,  String  nodeName2)`
	- Mutates  this  labyrinth  by  connecting  the  node  named  `nodeName1`  to  the  node  named  `nodeName2`.
	- If  there  is  no  node  in  the  labyrinth  named  `nodeName1`  or  `nodeName2`,  throw  an  `IllegalArgumentException`.
	- If  there  is  already  a  connection  between  the  nodes  named  `nodeName1`  and  `nodeName2`,  throw  `IllegalArgumentException`.

- `void  addColoredToken(String  name,  String  color)`
	- Mutates  this  labyrinth  by  adding  a  colored  token  specified  by  `color`  to  the  node  named  `name`.
	- If  the  node  already  has  a  token  with  this  `color`,  do  nothing.
    - If  there  is  no  node  in  this  labyrinth  named  `name`,  throw  an  `IllegalArgumentException`.

- `boolean  canColoredTokenReachNamedNode(String  color,  String  name)`
	- Returns  `true`  if  there  is  a  path  from  a  node  of  the  given  `color`  to  the  node  named  `name`.  Otherwise,  returns  `false`.
	- if  there  is  no  node  in  the  labyrinth  named  `name`,  this  method  should  throw  an  `IllegalArgumentException`.
