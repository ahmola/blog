### Microservice Practice With Springboot

![img](https://github.com/ahmola/blog/blob/main/blog.png?raw=true)

  - AWS EC2 가상 인스턴스에서 운영하는 Blog Service. 원래는 각 서비스들을 개별 인스턴스에서 운영해야하나 프리티어를 사용하는 관계로 한 인스턴스에서 운영


  - 프론트엔드(Port 3000)

        1. React+Vite로 웹 페이지를 구성
        2. 클라이언트는 Spring Cloud API Gateway와만 연결되어 서비스를 제공받는다.

  - 백엔드(API Gateway : Port 8080, Netflix Eureka Server : Port 8761 Microservice : 8081 ~ 8084)

        1. 클라이언트는 Spring Cloud API Gateway와만 연결되어 서비스를 요청한다.

        2. API Gateway는 Eureka Server와 연결되어 Eureka Server가 제공하는 Service Discovery를 통해 알맞은 서비스를 찾아준다.

        3. API Gateway는 Service Discovery를 통해 사용자에게 서비스를 제공하기 전에 인증을 요구한다. 사용자 자격 증명을 위해 로그인 관련된 주소들은 자격 증명없이 열어둔다.

        4. 사용자 인증은 Json Web Token을 발급하는 방식으로 클라이언트(React 페이지)는 발급받은 토큰을 사용하여 요청한 서비스를 제공받는다.

        5. 각 마이크로서비스는 이미지화하여 Docker Compose를 통해 일괄 컨테이너화하여 EC2 인스턴스에서 운영된다.

  - 데이터베이스(Port 3306)

        1. AWS RDS 가상 데이터베이스에서 MySQL Aurora을 사용하여 각 서비스의 데이터베이스를 구성한다.
    
        2. 원래는 각 서비스에 맞춰 데이터베이스를 구성해야하나 프리티어를 사용하는 관계로 한 데이터베이스 내 여러 테이블로 구성한다.
    
        3. 인바운드 규칙은 MySQL Aurora TCP 방식으로 Port 3306번을 열어두며, 오직 ec2 인스턴스와 연결하여 각 마이크로서비스 알맞는 테이블을 생성.
    
        4. 아웃바운드 규칙 또한 ec2 인스턴스로만 향하도록 IP를 제한한다.
    
        5. VPC 라우팅 테이블을 제한하여 개발자 로컬 컴퓨터를 제외한 인터넷 게이트웨이는 닫아두고, ec2 인스턴스와만 연결되도록 한다.

## 동작 방식

    1. 사용자가 클라이언트에 접속한다.

    2. 클라이언트는 사용자에게 자격 증명(유저이름, 비밀번호 등)을 요구한다.

    3. 사용자는 클라이언트가 제공하는 로그인 페이지에 자격 증명을 작성하여 권한 부여 서비스(JWT Login Service)에 사용자의 자격 증명을 제출한다.

    4. 자격 증명이 유효하다면 권한 부여 서버는 클라이언트에게 토큰을 발급한다.

    5. 토큰을 발급받은 클라이언트는 사용자가 요청하는 리소스에 따라 토큰을 사용하여 각 리소스를 제공받는다.

## 서비스 모델 구성

    Spring Cloud API Gateway
    - Spring Cloud에서 제공하는 API Gateway 서비스로 클라이언트는 API Gateway와 다른 백엔드 서비스의 URI를 알지 않고도 알맞은 서비스를 제공받을 수 있다.
    - API Gateway는 자체 토큰 검증을 하며, 사용자 인증이 되어있을 경우에만 서비스를 제공한다.

    Netflix Eureka Service Discovery
    - API Gateway가 Eureka Server를 통해 클라이언트가 요청한 서비스를 찾아준다.
    - API Gateway를 통해 제공받고자하는 마이크로서비스들은 API Gateway를 포함해서 Eureka Discovery Client로 등록되어야한다.

    JWT Auth Service
    - Spring Securit에서 제공하는 UserDetails를 상속받아 구성된다. ID, 유저 이름, 비밀번호
    - 사용자 인증을 위해 회원등록과 로그인 기능은 API Gateway를 포함하여 열어둔다.

    User Service 
    - 유저의 정보를 제공. ID, 유저 이름(이 둘은 로그인 서비스와 똑같으나 공유하지 않고 개별 테이블(원래는 DB)에서 관리), 이메일, 권한(USER, ADMIN), 게시물, 댓글
    - 유저가 정보 수정/삭제 시, JWT Auth Service에도 요청하여 사용자 정보를 수정/삭제할 것을 요청함

    Post Service
    - 유저의 게시물 정보를 제공. ID, 제목, 내용, 작성 날짜, 댓글
    - 유저와 @ManyToOne 관계이며 댓글과는 @OneToMany 관계이다

    Comment Service
    - 유저의 댓글 정보를 제공. ID, 내용, 작성 날짜, (대댓글은 추후 추가 예정)
    - 유저와 Post 모두에게 @ManyToOne 관계이다

    React+Vite Front End Service
    - 각 서비스에 맞춰 페이지를 제공함
    - 권한에 따라 접속할 수 있는 페이지를 제한
    - 인증이 되지않은 사용자에게는 자격 증명을 요구하는 로그인 페이지를 띄움
    - 오직 API Gateway와만 연결된다.

## 설계 순서

    우선은 각 서비스를 먼저 개발한 후에 RDS와의 연결 테스트 후 도커 이미지로 만들어서 잘 되는지 확인된 뒤에 EC2 인스턴스에 올려서 RDS와 연결하여 사용하도록 한다.

    1. API Gateway

    - 프론트엔드 페이지와 단독으로 연결된다.

    - Eureka Service에 연결되어 사용자 요청을 통해 받은 서비스를 Eureka Server를 통해 찾는다

    - JWT 로그인 서비스를 제외한 나머지 서비스에는 토큰이 없으면 접근 불가 처리. 토큰 유무를 확인한다.

    2. JWT Login Service

    - Json Web Token을 발급하여 사용자를 인증하고 관리한다.

    - 로그인과 회원가입 두 가지 기능으로 구성

    - 로그인 시에는 토큰을 발급하고, 회원가입 시에는 사용자를 DB에 추가하여 관리한다.

    - Spring Security의 UserDetails를 상속받아 유저 객체를 선언한다.

    3. User Service

    - 사용자의 정보를 제공하며, 권한에 따라 사용자의 기능이 제한됨.

    - USER는 기본적인 자신의 정보들의 CRUD만을 할 수 있다.

    - USER에는 USER와 BANNED_USER 두 가지가 존재하며 밴을 당한 유저는 게시물과 댓글 작성이 불가능함.

    - ADMIN은 모든 유저, 게시물, 댓글을 CRUD할 수 있음.

    - ADMIN은 유저를 밴할 수 있으며 밴을 당한 유저는 게시물 및 댓글 작성 기능을 사용할 수 없음. 유저 밴 기간을 설정할 수 있음.

    4. Post Service

    - 유저가 게시물을 CRUD할 수 있음

    - 밴을 당한 경우, 밴 당한 기간만큼 게시물을 올릴 수 없으나 삭제 및 수정은 가능하다.

    - ADMIN은 모든 게시물을 삭제할 수 있음.(수정은 불가능)

    5. Comment Service

    - 유저가 게시물에 댓글을 CRUD할 수 있음

    - 밴을 당한 경우, 밴 당한 기간만큼 댓글을 달 수 없으나, 삭제 및 수정은 가능하다.

    - ADMIN은 모든 댓글을 삭제할 수 있음.(수정은 불가능)

    6. React+Vite Client

    - 사용자는 클라이언트를 통해 서비스의 기능을 GUI 형태로 제공받음

    - 권한에 따라 보여지는 버튼과 기능, 접속할 수 있는 페이지가 달라짐

    - 일반 유저는 게시물을 올리고 댓글을 달고 유저 정보를 삭제하는 등의 기본적인 페이지만 볼 수 있음

    - ADMIN 유저만 유저, 게시물, 댓글을 관리하는 페이지에 접속할 수 있음

    7. Eureka Server

    - Eureka Client로 등록된 서비스를 찾아주는 Service Discovery를 제공

    - API Gateway가 클라이언트 요청을 바탕으로 서비스를 찾아줄 것을 Eureka Server에 요청함
    
    - Eureka Server는 등록된 서비스들을 찾아서 서비스를 제공한다.

## 비즈니스 로직

## UML 다이어그램
