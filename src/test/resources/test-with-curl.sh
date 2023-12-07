#./bin/sh
export BASE_URL='localhost:8087'
export UI_TYPES_PATH="$BASE_URL/uiTypes"
export OBJECT_KEYS_PATH="$BASE_URL/objectKeys"
export TYPE_JSON="Content-Type: application/json"
#curl -v -X POST "$UI_TYPES_PATH" -H "$TYPE_JSON" -d '{"name":"hidden","id":-1}'
#curl -v -X POST "$OBJECT_KEYS_PATH" -H "$TYPE_JSON" -d '{"name":"ID","uiTypeId":10, "id":-1}'
curl -v -X GET "$OBJECT_KEYS_PATH/list" -H "$TYPE_JSON" -d '{"number":0,"size":10}'
curl -v -X GET "$UI_TYPES_PATH" -H "$TYPE_JSON" -d '{"number":0,"size":10}'
