# Microservice - Json Web Token Authentication Login Service

### RDS 접속 방법
- 접속하기 전에 vpc 인바운드 규칙에 추가로 모든 ipv4 또는 특정 로컬 컴퓨터에 대한 접근을 허용해야함
- MySQL의 경우 2025.2.12일 기준 8.4까지만 지원하므로 로컬 컴퓨터의 mysql버전도 RDS의 DB버전과 맞춰야함

        mysql -h rds엔포인트 -P 포트번호(기본 3306) -u db사용자 -p