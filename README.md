# OICD 공모전 - 회식모아(Dining-together) -Backend
<img width="264" alt="image" src="https://user-images.githubusercontent.com/62784314/129598072-57ae7ac1-2192-42da-a3fb-bba8199f56d5.png">

## 프로젝트 소개

경매방식과 위치정보를 활용한 회식장소 선정 및 입찰경매 시스템 개발

## 🛠️ Backend 기술스택 🛠️

- Spring boot
- Build tool : Maven
- DB : mariaDB, Redis
- Log : Elasticsearch, Logstash, kibana
- Infra : MSA (Eureka + Spring Cloud Gateway)
- message queue : kafka,RabbitMq ,Spring Bus
- search: elastic search
- CI/CD : Jenkins, Docker
- Test : Junit
- API 문서 : Swagger UI
- etc : Lombok, Spring Data JPA

![image](https://user-images.githubusercontent.com/62784314/129598118-1e59bc66-6f9f-4e3f-b1dd-a86b3c37ca7a.png)

## :point_down:프로젝트의 자세한 설계과정을 보고싶다면 click :point_down:


<a href="https://github.com/Dining-together/Backend/wiki"><img width="240" alt="image" src="https://user-images.githubusercontent.com/62784314/129601422-07d63d69-2778-4c35-a1dc-5c02a30cceaf.png"></a>

## 디렉터리별 설명

```bash
├── Backend
│   ├── config # Spring Cloud Config Server
│   ├── eureka
│   ├── gateway
│   ├── member # 인증 서버 & 업체 사용자 관리
│   ├── auction # 경매 등록과 낙찰 & 리뷰
│   └── search # 검색 서비스
```

## Get started

---

네이버 클라우드 플랫폼 기반으로 테스트 환경을 구성하여 진행

jdk 11, Maven 3.6 환경

### elk, kafka, zookeeper, redis, mariadb, rabbitmq 구성

```docker
docker network create Dining-together

# elk 환경 구성
git clone https://github.com/TaeBeomShin/docker-elk-1
cd docker-elk-1
docker-compose up

# kafka zookeeper
git clone https://github.com/conduktor/kafka-stack-docker-compose
cd kafka-stack-docker-compose
docker-compose -f zk-multiple-kafka-multiple.yml up

```

```docker
# redis, mariadb, rabbitmq
git clone https://github.com/Dining-together/Backend
docker-compose up
```

### config server

```docker
cd config

mvn -Dmaven.test.failure.ignore clean compile package

docker build -t config_image .

docker run -d -p 8888:8888 --network Dining-together\
 -e "spring.rabbitmq.host=rabbitmq" \
  --name config config_image
```

### eureka server

```docker
cd eureka

mvn -Dmaven.test.failure.ignore clean compile package

docker build -t eureka_image .

docker run -d -p 8761:8761 --network Dining-together \
-e "spring.cloud.config.uri=http://config:8888" \
--name eureka eureka_image
```

### gateway server

```docker
cd gateway

mvn -Dmaven.test.failure.ignore clean compile package

docker build -t gateway_image .

docker run -d -p 8000:8000 -p 587:587/tcp --network Dining-together\
--name gateway -e "eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/" \
-e "spring.cloud.config.uri=http://config:8888" \
-e "spring.rabbitmq.host=rabbitmq" \
gateway_image
```

### member server

```docker
cd member

mvn -Dmaven.test.failure.ignore clean compile package

docker build -t member_image .

docker run -d --network Dining-together \
--name member \
-e "spring.cloud.config.uri=http://config:8888" \
-e "spring.rabbitmq.host=rabbitmq" \
-e "spring.redis.host=redis" \
-e "spring.datasource.url=jdbc:mariadb://mariadb:3306/member" \
-e "kafka.bootstrapAddress: kafka-stack-docker-compose_kafka1_1:9092" \
-e "eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/" \
member_image
```

### auction server

```docker
cd auction

mvn -Dmaven.test.failure.ignore clean compile package

docker build -t auction_image .

docker run -d --network Dining-together \
--name auction \
-e "spring.cloud.config.uri=http://config:8888" \
-e "spring.rabbitmq.host=rabbitmq" \
-e "spring.datasource.url=jdbc:mariadb://mariadb:3306/auction" \
-e "kafka.bootstrapAddress: kafka-stack-docker-compose_kafka1_1:9092" \
-e "eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/" \
auction_image
```

### search server

```docker
cd search

mvn -Dmaven.test.failure.ignore clean compile package

docker build -t search_image .

docker run -d --network Dining-together \
--name search \
-e "spring.elasticsearch.rest.uris=http://docker-elk-1_elasticsearch_1:9200" \
-e "kafka.bootstrapAddress: kafka-stack-docker-compose_kafka1_1:9092" \
-e "eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/" \
search_image
```
