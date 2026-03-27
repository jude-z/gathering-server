# Gathering Server

지역 기반 소모임 플랫폼의 백엔드 서버로, 모임 생성/참여부터 실시간 채팅, 일정 관리, 알림까지 소모임 운영에 필요한 핵심 기능을 제공합니다.

## 프로젝트 목표

- 단순 CRUD를 넘어 **대용량 트래픽을 고려한 서버 구조** 설계
- **Redis 캐싱 전략**(Soft/Hard TTL, 분산락, DB Fallback)을 통한 응답 속도 최적화
- **Kafka 기반 비동기 메시징**과 Outbox 패턴을 활용한 데이터 정합성 보장
- JPA, QueryDSL, JDBC Template을 상황에 맞게 사용하여 **쿼리 성능 튜닝**
- 멀티모듈 구조를 통한 **관심사 분리**와 유지보수성 확보
- 코드 리뷰를 통한 코드 품질 향상 및 이유 있는 기술 선택

## 사용 기술

<img src="docs/images/tech-stack.png" alt="Tech Stack" width="800"/>

## 프로젝트 구조

```
gathering-server/
├── api/              # REST API, Security, Service (port 8080)
├── domain/           # JPA Entity (순수 도메인, 외부 의존성 없음)
├── infra/            # Repository (JPA/QueryDSL/JDBC), Redis, Kafka
├── chat-server/      # 실시간 채팅 서버 (WebSocket STOMP, port 8081)
└── common/           # 페이지네이션, 유틸리티, 이벤트 공통 모듈
```

## 아키텍처

<img src="docs/images/system-architecture.png" alt="System Architecture" width="800"/>

## 핵심 기술적 고민

### 1. Redis 캐싱 전략 - Soft/Hard TTL + 분산락

<img src="docs/images/redis-caching-strategy.png" alt="Redis Caching Strategy" width="800"/>

- **문제**: 캐시 만료 시 다수 요청이 동시에 DB를 조회하는 Thundering Herd 문제
- **해결**: Soft TTL(55분)에 도달하면 Redisson 분산락을 획득한 1개 스레드만 DB 조회 후 캐시 갱신, 나머지는 기존 캐시 반환
- **Fallback**: Redis 장애 시 `gathering_cache` 테이블을 DB 기반 캐시로 활용
- **모니터링**: Prometheus 메트릭으로 캐시 히트/미스율 추적

### 2. 쿼리 성능 최적화 (v1 → v4)

<img src="docs/images/query-optimization.png" alt="Query Optimization" width="800"/>

### 3. Kafka + Outbox 패턴

<img src="docs/images/kafka-outbox-pattern.png" alt="Kafka + Outbox Pattern" width="800"/>

- **문제**: 채팅 메시지 발행과 DB 저장 간의 데이터 정합성
- **해결**: 트랜잭션 내에서 Outbox 테이블에 이벤트 저장 → 스케줄러가 10초마다 미발행 이벤트를 Kafka로 발행
- **비동기 처리**: 20~50 스레드풀로 Kafka 메시지 비동기 발행

### 4. 멀티모듈 설계
- `domain` 모듈은 외부 의존성 없이 순수 엔티티만 포함 → 도메인 오염 방지
- `infra` 모듈이 모든 데이터 접근 기술(JPA, QueryDSL, JDBC, Redis, Kafka)을 캡슐화
- `api` 모듈은 비즈니스 로직과 외부 인터페이스에만 집중

## 주요 기능

| 기능 | 설명 |
|------|------|
| 모임 관리 | 카테고리별 모임 생성/수정, 페이지네이션, 참여/탈퇴, 가입 승인 |
| 일정 관리 | 모임 내 일정 생성, 출석 체크 |
| 실시간 채팅 | WebSocket(STOMP) + Kafka 기반 채팅, 읽음 상태 추적 |
| 추천 시스템 | 일별 TOP 10 모임 추천 |
| 좋아요 | 모임 좋아요/취소 |
| 게시판 | 모임 내 게시글 작성, 이미지 첨부 |
| 알림 | 알림 조회/확인/삭제 |
| 인증 | JWT 기반 인증, 이메일 인증, 토큰 갱신 |
| 이미지 | AWS S3 업로드/다운로드 |

## Getting Started

### Prerequisites
- Java 21
- MySQL
- Redis
- Apache Kafka
- AWS S3 Bucket

### Run Locally

```bash
git clone https://github.com/<your-username>/gathering-server.git
cd gathering-server

# application.yml 설정 (DB, Redis, Kafka, AWS, JWT)
./gradlew clean build -x test
java -jar api/build/libs/*.jar
```

### Docker

```bash
docker build -t gathering-server .
docker run -p 80:80 gathering-server
```

## CI/CD

GitHub Actions를 통한 자동 배포:
1. `master` 브랜치 push 시 트리거
2. JDK 21 (Amazon Corretto) 빌드
3. AWS EC2로 SCP + SSH 배포
