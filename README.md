## PSO by java

### precondition

1. maven
2. java8 - 17

### how to build

- cd root dir
- mvn clean install

### how to execute test

- cd root dir
- maven clean test

### How to use pso

1. Create java class extends AbstractPSO
2. Implements `initXs`, `score`
3. Implements `condition` (optional, if you have constraint condition)
4. Set `compareType`(default = MAX), `particleNum`(default = 50), `iteration`(default = 500)
5. Then run `runAndGet` method, and print result (gBestScore)

