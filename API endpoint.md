# API endpoint

- **미션 기록**
    - 홈 화면 API
        - 홈 화면 조회
            
            ![image.png](image.png)
            
            - API Endpoint
            
            > GET /api/members/home
            > 
            - Request Header
            
            > Authorization: Bearer {accessToken} → 로그인 사용자 식별용
            > 
            - Query Parameter
            
            > locationId → 지역 필터
            > 
            - Path Variable
            
            > 없음
            > 
            - Request Body
            
            > GET 요청 → 없음
            > 
            - Response Body
                
                ```json
                {
                  "memberName": "홍길동",// member.name
                  "region": "안양동",  // location.location_name
                  "point": 930000,  // member.point
                  "missionProgress": {
                    "completed": 7, // member_mission에서 is_completed = true 개수
                    "total": 10   // member_mission 전체 개수
                  }
                }
                ```
                
        - 도전 가능한 미션 조회
            - API Endpoint
            
            > GET /api/missions
            > 
            - Request Header
            
            > Authorization: Bearer {accessToken}
            > 
            - Query Parameter
            
            > locationId, cursor, size → 지역별 미션, 페이징 기준 커서
            > 
            - Path Variable
            
            > 없음
            > 
            - Request Body
            
            > 없음
            > 
            - Response Body
            
            ```json
            {
              "missions": [
                {
                  "missionId": 5,
                  "shopName": "반의학생미라탕",
                  "condition": "10,000원 이상의 식사 시",
                  "point": 500,
                  "deadline": "2026-04-01"
                }
              ]
            }
            ```
            
    - 리뷰 작성 API
        - **API Endpoint**
        
        > POST /api/shops/{shopId}/reviews
        > 
        - **Request Body, Request Header, Query Parameter, Path Variable**
        
        | 구분 | 항목 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- | --- |
        | Request Header | Authorization | String | O | Bearer {accessToken} |
        | Request Header | Content-Type | String | O | multipart/form-data |
        | Path Variable | shopId | Long | O | 가게 ID (shop.id) |
        | Request Body | star | Float | O | 별점 (0.5 ~ 5.0, 0.5 단위) |
        | Request Body | content | String | O | 리뷰 내용 |
        | Request Body | images | List<MultipartFile> | X | 리뷰 사진 (다건 업로드) |
        | Query Parameter | - | - | - | 없음 |
        - Response Body
            
            ```json
            {
              "reviewId": 10,
              "shopId": 3,
              "shopName": "반의학생미라탕",
              "star": 5.0,
              "content": "너무 맛있어요",
              "photos": [
                {
                  "reviewPhotoId": 1,
                  "url": "https://s3.amazonaws.com/mumuk/reviews/10/photo1.jpg"
                }
              ],
            }
            ```
            
    - 미션 목록 조회
        - API Endpoint
        
        > GET /api/members/missions
        > 
        - Request Body, Request Header, Query Parameter, Path Variable
        
        | 구분 | 항목 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- | --- |
        | Request Header | Authorization | String | O | Bearer {accessToken} |
        | Request Body | - | - | - | 없음 (GET) |
        | Query Parameter | status | String | O | ACTIVE (진행중) / COMPLETED (진행완료) |
        | Query Parameter | cursor | Long | X | 마지막 member_mission.id (커서 페이지네이션) |
        | Query Parameter | size | Integer | X | 페이지 크기 (default: 10) |
        | Path Variable | - | - | - | 없음 |
        - Response Body
        
        ```json
        {
          "missions": [
            {
              "memberMissionId": 1,
              "missionId": 5,
              "shopName": "가게이름a",
              "point": 500,
              "condition": "12,000원 이상의 식사를 하세요!",
              "deadline": "2026-04-01",
              "isCompleted": false,
            }
          ],
        }
        ```
        
        ```json
        {
          "missions": [
            {
              "memberMissionId": 3,
              "missionId": 2,
              "shopName": "가게이름a",
              "point": 500,
              "condition": "12,000원 이상의 식사를 하세요!",
              "field": "JAPANESE",
              "deadline": "2026-03-20",
              "isCompleted": true,
            }
          ],
        }
        ```
        
    - 미션 성공 누르기
        - API Endpoint
        
        > PATCH /api/member-missions/{memberMissionId}/complete
        > 
        
        | 구분 | 항목 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- | --- |
        | Request Header | Authorization | String | O | Bearer {accessToken} |
        | Request Body | - | - | - | 없음 |
        | Query Parameter | - | - | - | 없음 |
        | Path Variable | memberMissionId | Long | O | member_mission.id |
        
    - 회원 가입 하기
        - API Endpoint
        
        > POST /api/auth/sign-up
        > 
        
        | 구분 | 항목 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- | --- |
        | Request Header | Content-Type | String | O | application/json |
        | Query Parameter | - | - | - | 없음 |
        | Path Variable | - | - | - | 없음 |
        - Request Body
        
        ```json
        {
          "name": "이창훈",
          "gender": "MALE",
          "birth": "2002-01-15",
          "address": "부천시",
          "addressDetail": "소사로~~",
          "email": "changhun@example.com",
          "phoneNumber": "01012345678",
          "nickname": "이훈",
          "preferFoodIds": [1, 3, 5],
          "termIds": [1, 2, 3]
        }
        ```