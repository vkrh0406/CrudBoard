# CrudBoard
 직접 만들어보는 게시판

익명과 회원 둘다 자유롭게 글을 쓸수있는 게시판입니다.

http://3.37.137.168:8080/board aws ec2 서버를 통해서 게시가 돼있습니다. 

Spring Boot + Jpa + QueryDsl + thymeleaf 를 사용합니다

DB는 Mariadb를 사용합니다.

test는 정말 기본적인 멤버리포지토리 테스트만 구현해 놓았습니다.

따라서 build하실때 db연결이 안되어있으면 테스트에 실패해서 빌드도 실패합니다.

db 설정은 application.yml에서 하실 수 있습니다.
