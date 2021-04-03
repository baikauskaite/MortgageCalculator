*The program's interface is in Lithuanian language, however, work is being done to translate it to English.*

This program has been written as a project during the second semester of the BSc Software Engineering degree in Vilnius University.

# Mortgage Calculator

A Java mortgage calculator using the JavaFX library. The fulfilled project requirements include:

	* Used the keyword `this` and `super`
	* Method and field overriding
	* Ability to choose either a linear or annuity method of mortgage calculation
	* Ability to see the calculation of paymens in a graph
	* Ability to see the calculation of paymens in a table
	* Ability to output the calculations to a `txt` file

## How to run the program

Be sure to install [JavaFX SDK version 11] (https://gluonhq.com/products/javafx/) or later. You can run the code on Intellij or use it to create an executable `.jar` file from source files.
Open terminal where the `.jar` file was created and enter:

	java --module-path PATH --add-modules javafx.controls,javafx.fxml -jar Project2.jar
	
Instead of the `PATH` placeholder specify the path to the lib folder in the JavaFX SDK package in your computer, for example:

	/Users/jetbrains/Desktop/javafx-sdk-12/lib


## Screenshots

**The main screen for entering data**

![Main](https://user-images.githubusercontent.com/73688133/113473505-8ac0b600-9472-11eb-9bd4-01e5707a892c.png)


**The calculation of payments depicted in a table**

![Table](https://user-images.githubusercontent.com/73688133/113473463-4d5c2880-9472-11eb-973e-f3116f4c1630.png)


**The calculation of payments depicted in a linear graph**

![Graph](https://user-images.githubusercontent.com/73688133/113473465-4e8d5580-9472-11eb-91e8-e919a1833527.png)
