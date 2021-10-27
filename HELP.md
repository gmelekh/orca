# Getting Started

### Guides

Clone project to your local machine

This application could be tested by several options:
first option:
1. you should have java IDE (intellij,eclipse...) with installed java virtual machine and configured maven (for mac users could be installed using brew)
     open project in IDE (make sure it is recognized as maven application) and just execute application.
2. (several user profiles could be used to inject provided input files) `prod`,`dev`,`stage`
new api was created for self testing http://localhost/v1/api/self.

second option to execute a program is using docker.
run fallowing commands 
 1. `docker build --no-cache -t orca-test .`
 2. `docker run -p 80:80 orca-test &`
 3.  when docker is in RUNNING state, you can start sending curls
 

