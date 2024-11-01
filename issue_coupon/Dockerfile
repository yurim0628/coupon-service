# Step 1: 빌더 단계
FROM openjdk:17-slim as builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 및 설정 파일 복사
COPY gradlew /app/
COPY gradle /app/gradle/
COPY build.gradle /app/
COPY settings.gradle /app/

# common 모듈의 Gradle 파일 및 src 디렉토리 복사
COPY common/build.gradle /app/common/
COPY common/src /app/common/src/

# redis 모듈의 Gradle 파일 및 src 디렉토리 복사
COPY redis/build.gradle /app/redis/
COPY redis/src /app/redis/src/

# user 모듈의 Gradle 파일 및 src 디렉토리 복사
COPY user/build.gradle /app/user/
COPY user/src /app/user/src/

# coupon 모듈의 Gradle 파일 및 src 디렉토리 복사
COPY coupon/build.gradle /app/coupon/
COPY coupon/src /app/coupon/src/

# issue_coupon 모듈의 Gradle 파일 및 src 디렉토리 복사
COPY issue_coupon/build.gradle /app/issue_coupon/
COPY issue_coupon/src /app/issue_coupon/src/

# Gradle Wrapper에 실행 권한 부여
RUN chmod +x ./gradlew

# issue_coupon 모듈을 빌드 (테스트 제외)
RUN ./gradlew :issue_coupon:bootJar -x test --stacktrace

# 빌드된 JAR 파일을 최종 이미지로 복사
RUN cp issue_coupon/build/libs/issue_coupon-0.0.1-SNAPSHOT.jar /app/app.jar

# Step 2: 실행 단계
FROM openjdk:17-slim

# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=builder /app/app.jar /app.jar

# 애플리케이션이 사용하는 포트를 노출
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-Dspring.profiles.active=dev", "-jar", "/app.jar"]
