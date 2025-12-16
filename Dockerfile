# 베이스 이미지
FROM amazoncorretto:17
# 컨테이너에 볼륨 설정해줘서 영구적으로 데이터 보존
VOLUME /tmp
#컨테이너가 사용할 포트번호
EXPOSE 8082
# 현재 로컬 파일을 도커 컨테이너 안으로 복사하는 기능
COPY build/libs/*.jar /app.jar
# 도커가 실행되고 최초로 실행될 명령어
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
