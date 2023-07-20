# selenium-java-demo
 Java Selenium with Cucumber parallel tests

type = parallelFeature , parallelScenario
env , tag = int , prod ,login , hover, dynamic
browser = chrome or firefox
Pthreads = 3 for parallel feature / 9 for parallel Scenarios

gradlew -Dtype=parallelFeature -Dtag=@Prod -Pthreads=3 -Denv=prod -Dremote.driver.url= -Dbrowser.type=chrome clean test

gradlew -Dtype=parallelScenario -Dtag=@Int -Pthreads=9 -Denv=int -Dremote.driver.url= -Dbrowser.type=chrome clean test