# fabbrica
Industrial IOT Platform

#### Requirements
- EMQX - MQTT Broker
- InfluxDB - Timeseries Data
- Telegraf - Metric Collection
- MongoDB - User & Device Data
- Vue - Front End
- Spring Boot - Rest API
- Nginx - Reverse Proxy
- Chronograf - Testing InfluxDB & Telegraf **(Optional)**

#### Installation
1. Start containers
`docker-compose up -d`
2. Updating fabbrica-server
```
cd fabbrica-server
./gradlew build -x test 
cd .. && docker-compose restart fabbrica-server
```
