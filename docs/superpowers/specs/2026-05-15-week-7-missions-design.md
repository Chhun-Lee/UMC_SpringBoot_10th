# UMC 10기 7주차 미션 — 설계 문서

작성일: 2026-05-15

## 목표

UMC 10기 7주차 필수 미션 3가지를 구현한다.

1. 내가 진행중인 미션 조회 — 오프셋 기반 페이지네이션, 사용자 ID는 Request Body
2. 내가 생성한 리뷰 조회 — 커서 기반 페이지네이션, ID 순/별점 순, 사진 제외
3. Request Body API에 검증 어노테이션 적용 + `GlobalExceptionHandler`에서 처리

## 아키텍처 변경 범위

| 영역 | 변경 |
|---|---|
| `build.gradle` | `spring-boot-starter-validation` 의존성 추가 |
| `mission` 도메인 | 진행중 미션 조회 API 추가 (Controller / Service / Repository / DTO / Converter) |
| `review` 도메인 | 내 리뷰 조회 API 추가 (Controller / Service / Repository / DTO / Converter / Enum) |
| `review` DTO | `CreateReviewDTO`에 검증 어노테이션 추가 |
| `GlobalExceptionHandler` | 변경 없음 (기존 `MethodArgumentNotValidException` 핸들러 재사용) |

기존 코드 컨벤션(BaseEntity, BaseErrorCode/BaseSuccessCode, CustomResponse, static Converter, DTO 내부 정적 클래스)을 그대로 따른다.

---

## Task 1 — 진행중인 미션 조회 (오프셋 페이지)

### 엔드포인트

`POST /api/member-missions/in-progress`

> 사용자 ID를 Request Body로 받는 요구사항을 반영해 `POST` 채택. 컨벤션상 "조회 POST"로 처리.

### Request Body — `MemberMissionReqDTO.InProgressMissionRequestDTO`

```json
{ "memberId": 1, "page": 0, "size": 10 }
```

| 필드 | 타입 | 검증 |
|---|---|---|
| memberId | Long | `@NotNull` `@Positive` |
| page | Integer | `@NotNull` `@Min(0)` |
| size | Integer | `@NotNull` `@Min(1)` `@Max(50)` |

### Response — `MemberMissionResDTO.InProgressMissionListDTO`

```json
{
  "missions": [
    { "missionId": 12, "shopName": "BHC", "condition": "1만원 이상 결제", "point": 500, "deadline": "2026-12-31" }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 23,
  "totalPages": 3,
  "hasNext": true
}
```

원소 DTO는 기존 `MissionResDTO.AvailableMissionDTO`와 같은 필드 구성을 사용 (재사용 또는 미러링; 도메인 분리를 위해 신규 DTO로 미러링 선호).

### Repository — `MemberMissionRepository`에 메서드 추가

```java
@EntityGraph(attributePaths = {"mission", "mission.store"})
Page<MemberMission> findByMemberIdAndIsCompletedFalse(Long memberId, Pageable pageable);
```

`@ManyToOne` LAZY로 인한 N+1 방지용 `@EntityGraph` 사용.

### Service 흐름

1. `memberRepository.existsById(memberId)` — 없으면 `MemberException(MEMBER_NOT_FOUND)`
2. `PageRequest.of(page, size)` 로 `Page<MemberMission>` 조회
3. Converter로 `InProgressMissionListDTO` 변환

### Controller

`MemberMissionController`에 메서드 추가:

```java
@PostMapping("/in-progress")
public ResponseEntity<CustomResponse<...InProgressMissionListDTO>> getInProgressMissions(
    @RequestBody @Valid InProgressMissionRequestDTO request
)
```

### Success Code

`MissionSuccessCode.MISSION_IN_PROGRESS_LIST_OK` (예: `MISSION200_3`) 추가.

---

## Task 2 — 내 리뷰 조회 (커서 페이지, ID/별점)

### 엔드포인트

`GET /api/reviews/my`

### Query Parameters

| 파라미터 | 타입 | 기본값 | 설명 |
|---|---|---|---|
| memberId | Long | 필수 | 작성자 ID |
| sort | ReviewSortType (enum) | `ID` | `ID` \| `STAR` |
| cursorId | Long | null | 마지막 review.id |
| cursorStar | Float | null | `sort=STAR`일 때 마지막 review.star |
| size | Integer | 10 | 페이지 크기 |

### Response — `ReviewResDTO.MyReviewListDTO`

```json
{
  "reviews": [
    { "reviewId": 42, "shopId": 7, "shopName": "BHC", "star": 4.5, "content": "맛있어요", "createdAt": "2026-05-14T10:00:00" }
  ],
  "nextCursorId": 31,
  "nextCursorStar": 4.5,
  "hasNext": true,
  "sort": "STAR"
}
```

원소 DTO `MyReviewDTO`: `reviewId`, `shopId`, `shopName`, `star`, `content`, `createdAt`. **사진 필드 없음.**

### Enum — `ReviewSortType`

`domain/review/enums/ReviewSortType.java` — `{ ID, STAR }`

### Repository — `ReviewRepository`에 2개 메서드 추가

```java
@Query("""
    SELECT r FROM Review r
    JOIN FETCH r.store s
    WHERE r.member.id = :memberId
      AND (:cursorId IS NULL OR r.id < :cursorId)
    ORDER BY r.id DESC
""")
List<Review> findMyReviewsOrderById(
    @Param("memberId") Long memberId,
    @Param("cursorId") Long cursorId,
    Pageable pageable);

@Query("""
    SELECT r FROM Review r
    JOIN FETCH r.store s
    WHERE r.member.id = :memberId
      AND (
        :cursorStar IS NULL
        OR r.star < :cursorStar
        OR (r.star = :cursorStar AND r.id < :cursorId)
      )
    ORDER BY r.star DESC, r.id DESC
""")
List<Review> findMyReviewsOrderByStar(
    @Param("memberId") Long memberId,
    @Param("cursorStar") Float cursorStar,
    @Param("cursorId") Long cursorId,
    Pageable pageable);
```

복합 커서 `(star, id)`는 별점 동률 시 누락/중복을 방지한다.

### Service 흐름

1. `memberRepository.existsById(memberId)` — 없으면 `MemberException(MEMBER_NOT_FOUND)`
2. `sort` 분기 → 해당 Repository 메서드 호출 (`PageRequest.of(0, size + 1)`)
3. 결과 수 > size 이면 `hasNext = true`, 마지막 원소 제거
4. 남은 리스트의 마지막 원소에서 `nextCursorId`, `nextCursorStar` 산출 (비어있으면 null)
5. Converter로 변환

### Controller

```java
@GetMapping("/my")
public ResponseEntity<CustomResponse<MyReviewListDTO>> getMyReviews(
    @RequestParam Long memberId,
    @RequestParam(defaultValue = "ID") ReviewSortType sort,
    @RequestParam(required = false) Long cursorId,
    @RequestParam(required = false) Float cursorStar,
    @RequestParam(defaultValue = "10") Integer size
)
```

`/api/reviews` 경로가 신설되므로 `ReviewController`의 `@RequestMapping`을 `/api/shops`에서 분리하거나 별도 컨트롤러 추가가 필요. **결정: `MyReviewController` 신규 생성** (`/api/reviews`) — 기존 `ReviewController`(`/api/shops/{shopId}/reviews`)는 그대로 유지.

### Success Code

`ReviewSuccessCode.MY_REVIEW_LIST_OK` (예: `REVIEW200_1`) 추가.

---

## Task 3 — Request Body 검증

### 의존성

`build.gradle`:
```gradle
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

### 검증 어노테이션 적용

**`ReviewReqDTO.CreateReviewDTO`** (기존):
```java
@NotNull
@DecimalMin(value = "0.5") @DecimalMax(value = "5.0")
private Float star;

@NotBlank
@Size(max = 500)
private String content;
```

> `validateStar()`의 0.5 단위 검증은 어노테이션으로 표현이 어렵기 때문에 **서비스 단의 수동 검증은 유지**한다 (이중 검증).

**`MemberMissionReqDTO.InProgressMissionRequestDTO`** (신규):
```java
@NotNull @Positive private Long memberId;
@NotNull @Min(0)   private Integer page;
@NotNull @Min(1) @Max(50) private Integer size;
```

### `@Valid` 적용 지점

- `ReviewController.createReview`: `@RequestPart("request") @Valid ReviewReqDTO.CreateReviewDTO request`
- `MemberMissionController.getInProgressMissions`: `@RequestBody @Valid InProgressMissionRequestDTO request`

### 예외 처리

기존 `GlobalExceptionHandler.handleValidation(MethodArgumentNotValidException)`을 그대로 사용한다. 응답:

```json
{
  "isSuccess": false,
  "code": "COMMON422",
  "message": "요청 값 검증에 실패했습니다.",
  "result": { "star": "must be greater than or equal to 0.5", "content": "must not be blank" }
}
```

---

## 데이터 플로우 요약

```
[Client]
  └─POST /api/member-missions/in-progress  body { memberId, page, size }
      └─Controller (@Valid) ──검증 실패→ 422 (MethodArgumentNotValidException)
          └─Service: existsById → Page<MemberMission> 조회
              └─Converter → InProgressMissionListDTO
                  └─CustomResponse.ok

[Client]
  └─GET /api/reviews/my?memberId=&sort=&cursorId=&cursorStar=&size=
      └─Controller
          └─Service: existsById → sort 분기 → findMyReviewsOrderBy{Id|Star} (size+1)
              └─hasNext 판정, nextCursor 산출
                  └─Converter → MyReviewListDTO
                      └─CustomResponse.ok
```

## 에러 처리 매트릭스

| 상황 | HTTP | 코드 |
|---|---|---|
| Body 검증 실패 | 422 | `COMMON422` (+ 필드 에러 맵) |
| 잘못된 `sort` 값 | 400 | `COMMON400` (Spring enum 변환 실패) |
| `memberId` 존재 안 함 | 404 | `MEMBER404_*` |
| 별점 0.5 단위 위반 | 400 | `REVIEW400_1` (서비스 수동 검증) |

## 영향받는 파일 목록

신규:
- `domain/mission/dto/MemberMissionReqDTO.java` (또는 기존 `MissionReqDTO`에 추가)
- `domain/review/controller/MyReviewController.java`
- `domain/review/enums/ReviewSortType.java`

수정:
- `build.gradle`
- `domain/mission/controller/MemberMissionController.java`
- `domain/mission/service/MissionService.java` + `MissionServiceImpl.java`
- `domain/mission/repository/MemberMissionRepository.java`
- `domain/mission/dto/MissionResDTO.java`
- `domain/mission/converter/MissionConverter.java`
- `domain/review/service/ReviewService.java` + `ReviewServiceImpl.java`
- `domain/review/repository/ReviewRepository.java`
- `domain/review/dto/ReviewReqDTO.java` (검증 어노테이션)
- `domain/review/dto/ReviewResDTO.java`
- `domain/review/converter/ReviewConverter.java`
- `domain/review/controller/ReviewController.java` (`@Valid` 추가)
- `global/code/status/MissionSuccessCode.java`
- `global/code/status/ReviewSuccessCode.java`
