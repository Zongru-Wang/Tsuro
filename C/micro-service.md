# lab.com Micro Service

**lab.com** is a micro service that allows clients to

 - Specify a Labyrinth with named nodes
 - Add colored tokens to existing named nodes
 - Ask whether or not a node with a given token reaches a node with a given name.

This document defines the communication protocol between a client of lab.com and the lab.com server.

## Communication Protocol

Within this protocol:
 - Json as input or as output will be shown as `code`.
 - Logic will be shown in *italics*.
 - Output will be shown in **bold**.

### 1) Connect to lab.com
#### Request
N/A
#### Response
**Welcome to lab.com!  
Please enter your Labyrinth using Json in the form `["lab", { "from": <name:string>, "to": <name:string> }, ...]`**

### 2) Input Labyrinth
#### Request
`["lab", { "from": <name:string>, "to": <name:string> }, ...]`
#### Response
*if the request is correctly formed*:  
&nbsp;&nbsp;&nbsp;&nbsp; **Successfully constructed Labyrinth** *echo inputted Json*  
&nbsp;&nbsp;&nbsp;&nbsp; **To add color to existing node, type `["add", <color:color-token>, <name:string>]`  
&nbsp;&nbsp;&nbsp;&nbsp; To ask if a node with a given `color` is connected to the node with the given `name`, type `["move", <color:color-token>, <name:string>]`  
&nbsp;&nbsp;&nbsp;&nbsp; To disconnect from lab.com, type `disconnect`**  
*else*:  
&nbsp;&nbsp;&nbsp;&nbsp; **Labyrinth construction was unsuccessful**  
&nbsp;&nbsp;&nbsp;&nbsp; *echo why it was unsuccessful*  
&nbsp;&nbsp;&nbsp;&nbsp; **Please enter your Labyrinth using Json in the form `["lab", { "from": <name:string>, "to": <name:string> }, ...]`**  
&nbsp;&nbsp;&nbsp;&nbsp; *goto (2)*

### 3) `add`/`move`/`disconnect` Loop
#### Request
`["add", <color:color-token>, <name:string>]`  
*OR* `["move", <color:color-token>, <name:string>]`  
*OR* `disconnect`
#### Response
*if the request is* `disconnect`:  
&nbsp;&nbsp;&nbsp;&nbsp; **Bye!**  
&nbsp;&nbsp;&nbsp;&nbsp; *Disconnect*  
*else if request is* `["add", <color:color-token>, <name:string>]`:  
&nbsp;&nbsp;&nbsp;&nbsp; **Successfully added token `<color>` to node named `<name>`**  
*else if request is* `["move", <color:color-token>, <name:string>]`:  
&nbsp;&nbsp;&nbsp;&nbsp; *if node with token of `<color>`* can reach the node named `<name>`:  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **Found path from a node with token of `<color>` to the node `<name>`**  
&nbsp;&nbsp;&nbsp;&nbsp; *else*:  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **Unable to reach the node `<name>` from a node with `<color>` token** 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  *goto (3)*  
*else*:  
&nbsp;&nbsp;&nbsp;&nbsp; **Invalid command** *echo input*  
&nbsp;&nbsp;&nbsp;&nbsp; *goto (3)*