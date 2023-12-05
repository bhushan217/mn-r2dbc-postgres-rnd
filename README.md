## Micronaut - R2DBC
Learning micronaut with r2dbc protocol for reactive data storage

### Object Keys
API with CRUD operation on ObjectKeys(keyName, uiType) 

### Test with curl command (on Git Bash)

#### GET - List All
curl http://localhost:8087/objectKeys/list

#### POST - save
curl -d '{"keyName":"FIRST_NAME", "uiType":"text"}' -H "Content-Type: application/json" -X POST http://localhost:8087/objectKeys

    {"id":3,"keyName":"FIRST_NAME","uiType":"text"}

#### PUT - update
curl -d '{"id":3, "keyName":"FIRST_NAME", "uiType":"text"}' -H "Content-Type: application/json" -X PUT http://localhost:8087/objectKeys


#### DELETE - update
curl -X DELETE http://localhost:8087/objectKeys/3


