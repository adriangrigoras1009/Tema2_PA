build: Poduri.java Adrese.java Lego.java Retea.java
	javac Poduri.java Adrese.java Lego.java Retea.java
run-p1: Poduri.class
	java Poduri
run-p2: Adrese.class
	java Adrese
run-p3: Lego.class
	java Lego
run-p4 : Retea.class
	java -Xss3000k Retea
clean: *.class
	rm *.class
