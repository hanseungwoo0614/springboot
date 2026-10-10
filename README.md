프로젝트 구조

src/main/java/com/example/ksl/wsd2025

├── Wsd2025Application.java  

├── api

│   ├── dto/ItemDto.java         # 상품 데이터 (id, name, price)

│   ├── request/                 # 요청 Body 형식

│   │   ├── ItemCreateRequest.java

│   │   └── ItemUpdateRequest.java

│   └── v1/ItemController.java   #  실습 API 8개

├── common
│   └── ApiResponse.java         #  표준 응답 형식

└── config

    ├── LoggingInterceptor.java  # 미들웨어 (요청 로그 + 처리 시간)
    
    └── WebConfig.java           # 미들웨어 등록

기본 주소: http://localhost:8080/api/v1/items로 수행했습니다.

A 수행
  1.get - 전체 상품 조회 - 기본주소
  
  2.get - 단일 상품 조회 - 기본 주소 뒤에 id를 붙여야합니다.
  
  3.post - 상품 1개 등록 - 기본주소
  
  4.post - 	상품 여러 개 한 번에 등록 - 기본 주소 뒤에 all을 붙여야합니
  
  5.put - 상품 수정 (보낸 값만 변경) - 기본 주소 뒤에 id를 붙여야합니다.
  
  6.put - 상품 가격만 수정- 기본 주소 뒤에 id와 price를 붙여야합니다.
  
  7.delete - 	상품 단건 삭제 -기본 주소 뒤에 id를 붙여야합니다.
  
  8.delete - 상품 전체 삭제 - 기본주소

  B 수행
  HandlerInterceptor를 이용해 LoggingInterceptor를 구현하고, WebConfig에서 경로를 등록했습니다. 모든 API 요청이 컨트롤러에 도착하기 전과 응답이 끝난 후에 실행됩니다.
  
  -preHandle (요청 전): 요청 메소드, URL, 쿼리 파라미터를 출력하고 요청 시작 시간을 기록
  
  -afterCompletion (응답 후): 응답 상태 코드와 처리 시간을 출력

  c 수행

  	200 OK -	성공 - 	조회, 수정, 삭제 성공
    201 Created -	생성됨	 -상품 등록 성공
    400 Bad Request -	잘못된 요청 -	이름이 비었거나 가격이 없거나 음수일 때, 빈 목록 등록 시
    404 Not Found -	찾을 수 없음 -	존재하지 않는 id로 조회·수정·삭제할 때
    500 Internal Server Error -	서버 내부 오류 -	GET /api/v1/items?simulate=500 
    503 Service Unavailable -	서비스 이용 불가 -	GET /api/v1/items?simulate=503 

  d 수행
  모든 응답은 common/ApiResponse 클래스로 감싸 동일한 형식으로 반환합니다. 값이 없는 항목은 응답에서 생략됩니다.

  수행 캡쳐 사진은 한글파일로 업로드했습니다.

    
