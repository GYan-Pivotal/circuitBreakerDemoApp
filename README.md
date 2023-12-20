# GemFire Circuit Breaker

main idea is from https://github.com/ggreen/spring-geode-showcase/tree/master/applications/gemfire-circuitbreaker-demo

## Description

switch connection between 2 clusters

## Getting Started

### Dependencies

* Spring Boot
* GemFire

### endpoints
1. get value from server cluster 
   - GET http://localhost:8080/key/test
2. switch to Primary cluster
   - GET http://localhost:8080/pool/PRIMARY
3. switch to Secondary cluster
   - GET http://localhost:8080/pool/SECONDARYPOOL
4. get the current connected cluster name
   - GET http://localhost:8080/pool/current

### some GFSH commands
for cluster 1
1. connect --locator=locator1[30001]
2. list members
3. list regions
4. create region --name=/test --type=PARTITION
5. put --region=/test --key="test" --value="locator1"
6. query --query="select * from /test"

for cluter 2
1. connect --locator=locator2[40001]
2. list members
3. list regions
4. create region --name=/test --type=PARTITION
5. put --region=/test --key="test" --value="locator2"
6. query --query="select * from /test"
