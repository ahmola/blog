# Netflix Eureka API Gateway

- ssh로 ec2에 접속

      ssh -i /path/to/your-key.pem ec2-user@<EC2-PUBLIC-IP>

### dockerfile로 이미지 빌드

      docker build -t 이미지이름 -f dockerfile.식별가능한이름 .

### docker 컨테이너 실행
      
      docker run -d --name 컨테이너이름 -p 포트번호:컨테이너포트번호 이미지이름

### EC2 인스턴스에 dockerfile 전송

- dockerfile 전송
   
      scp -i /path/to/your-key.pem Dockerfile ec2-user@<EC2-PUBLIC-IP>:디렉터리 위치/ec2-user/