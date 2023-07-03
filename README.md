# selenium-java-demo
 Java Selenium with Cucumber parallel tests

type = parallelFeature , parallelScenario
gradlew -Dtype=parallelFeature -Dtag=@Prod -Pthreads=3 -Denv=prod -Dremote.driver.url= -Dbrowser.type=chrome clean test