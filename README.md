# 💸 Pay-Service - Kakaopay Prototype Project

> Spring Boot로 구현한 **카카오페이 프로토타입** 서비스
> 계좌 기반의 **송금, 충전, 결제, 통계 기능**을 포함한 **간단한 핀테크 서비스** 에 대한 객체지향적 구현을 목표

---

## 🛠️ Tech Stack

- **Backend**: Spring Boot, Spring Data JPA, Spring Security
- **Database**: MySQL
- **API 문서화**: Swagger (Springdoc OpenAPI)
- **Dev Tools**: Gradle, Git, GitHub

---

## 🔧 주요 기능

| 기능 | 설명 |
|------|------|
| 🔐 계좌 등록 | 유저의 계좌를 등록하고 관리 |
| 💸 송금 | 등록된 계좌 간 송금 기능 |
| 💳 충전 | 계좌에 금액을 충전 |
| 🧾 결제 | 결제 요청 및 잔액 차감 |
| 📊 통계 | 사용 내역 기반 통계 제공 |

---

## 📅 프로젝트 일정

| 주차 | 목표 |
|------|------|
| 1주차 | ✅ DB 설계 (ERD) <br> ✅ API 명세서 작성 <br> ✅ 프로젝트 초기 세팅 (Init) |
| 2주차 |  |
| 3주차 |  |
| 4주차 |  |
| 5주차 |  |

---

## 💻 1주차 결과물

### API 명세서
![API 명세서](https://velog.velcdn.com/images/rlaehddbs4521/post/0b0bb6b3-7907-42af-9736-9f572bf82211/image.png)

### ERD
![ERD](https://velog.velcdn.com/images/rlaehddbs4521/post/7374d6b0-2bbe-4dd8-94cf-26cb551c5624/image.png)

---

## 💻 2주차 결과물

### ERD
![ERD](https://velog.velcdn.com/images/rlaehddbs4521/post/84f8a62c-5ac3-4cd0-8a6a-b159866075b7/image.png)


- 1주차 결과물 피드백을 통해 ERD 수정
- Spring Security를 적용하여 인증/인가
- Java Mail Sender를 이용해서 이메일 인증 코드 전송
- Session 기반으로 인증/인가 진행
- Redis를 통해 이메일 인증 번호, Session Id 저장
- 로그인, 로그아웃, 인증 핸들러 등록
- 인증 인가 필터 등록