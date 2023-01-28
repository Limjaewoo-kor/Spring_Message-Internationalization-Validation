# study-thymeleaf-basic_part2

1. 스프링/타임리프 
- 저장 및 수정 폼

2. 화면내에 멘트(메세지) properties로 통합화 

3. properties이용하여 국제화 -> Http 헤더의 locale 정보에따라 다른 언어로 보여주는 기능

4.검증 기능 
V1~V4까지 점진적 테스트

V1
* Map<String,String> errors = new HashMap<>()를 사용한 검증

V2
* bindingResult 및 itemValidator을 이용한 검증

V3
* Bean Validation(어노테이션)을 이용한 검증 / 검증시 group화 적용

V4
* Bean Validation(어노테이션)을 이용한 검증 / Form 분리

5. Json방식 API 검증 기능 테스트
* 요청 실패 [bad request] - 타입오류로 ItemSaveForm형식과 달라서 controller 호출 조차 되지않음.
          ->   HTTP 메시지 컨버터 자체가 실패한것임
* 검증오류 - 필드의 수치가 틀려서 bean Validated에 걸려서 오류 발생
* 성공 - 성공
    
