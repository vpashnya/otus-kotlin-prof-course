```bash
curl -X POST -H "Content-Type: application/json" 'localhost:8080/v1/ip/stream/create' -d '{"requestType":"create","requestType":null,"debug":{"mode":"stub","stub":"success"},"stream":{"classShortName":"CLIENT","methodShortName":"EXPORT2FNS","transportParams":"some transport","description":"Отправка информации в ФНС"}}' -v
```
```bash
curl -X POST -H "Content-Type: application/json" 'localhost:8080/v1/ip/stream/update' -d '{"requestType":"update","requestType":null,"debug":null,"stream":{"classShortName":"CLIENT","methodShortName":"EXPORT2FNS","transportParams":"some transport","description":"Отправка информации в ФНС","id":"12345"}}' -v
```
```bash
curl -X POST -H "Content-Type: application/json" 'localhost:8080/v1/ip/stream/delete' -d '{"requestType":"delete","requestType":null,"debug":null,"streamId":"12345"}' -v
```
```bash
curl -X POST -H "Content-Type: application/json" 'localhost:8080/v1/ip/stream/read' -d '{"requestType":"read","requestType":null,"debug":null,"streamId":"12345"}' -v
```
```bash
curl -X POST -H "Content-Type: application/json" 'localhost:8080/v1/ip/stream/enable' -d '{"requestType":"enable","requestType":null,"debug":null,"streamId":"12345"}' -v
```
```bash
curl -X POST -H "Content-Type: application/json" 'localhost:8080/v1/ip/stream/disable' -d '{"requestType":"disable","requestType":null,"debug":null,"streamId":"12345"}' -v
```
```bash
curl -X POST -H "Content-Type: application/json" 'localhost:8080/v1/ip/stream/search' -d '{"requestType":"search","requestType":null,"debug":null,"streamFilter":{"searchString":"classShorName = ''KREDS%'' and methodShortName = ''%''  ","classShortName":"DEPOSIT","methodShortName":"OEPN","active":false}}' -v
```
```bash
curl -X POST -H "Content-Type: application/json" 'localhost:8080/v1/ip/stream/accessible' -d '{"requestType":"accessible","requestType":null,"debug":null,"externalSystemId":null}' -v
```