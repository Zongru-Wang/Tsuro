From: SHAW<br>
To: Ferdinand Vesely<br>
Subject: Explanation for using java 1.8 SDK for this homework 

For task 2, the specification from another group asked us to use Kotlin 1.3 to conduct the server, 
we use the Kotlin JMV to write this code. Since we need Junit4 to test our code, and we want to use Stack and 
java style exception for his homework. 

Ideally, Kotlin is similar to java, and since we are not familiar with kotlin, especially how to test, another reason 
is that the CCIS server has java 1.8 SDK in VM, and since the specification we got want our code can run on CCIS
machine, we choose the safe way which is use Java 1.8 SDK

This assignment asked us to put every piece of code together, but when putting them together, and running junit4 test 
cases, it will throw java.lang.NoClassDefFoundError: org/hamcrest/SelfDescribing, I think it is because kotlin can't 
recognize classes when doing junit tests where we put all classes and interface together. 
If you split the classes and interface in different kotlin.class/interfaces, the test will working perfectly. 

But every piece of code is Kotlin code and it would be a few changes(IllegalArgumentException parts in code, maybe more
small changes) to switch to Kotlin SDK 1.3 in the future.  

