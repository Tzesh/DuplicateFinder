# DuplicateFinder
Kind of lab session of 4th week of BIM208 course.

Simple Java practicement that leads us to learn how to use java.NIO package and also the basic idea that lies behind the streams and how to use them.

DuplicateFinder just simply looks for duplicates located in your 'user.home' directory, which is 'C:\Users\Tzesh' in Windows or '/home/Tzesh' for Linux. Maps them according to their MD5, and controls if there is a collusion or not.

## Usage
Just simple clone the repository then build the maven project by typing ```mvn clean package``` then go to the directory of project and run the compiled .jar file by typing ```java -jar target\DuplicateFinder-1.0-SNAPSHOT.jar```
![Usage](https://i.imgur.com/3myfing.png)
![Usage2](https://imgur.com/r7eERm8.png)
