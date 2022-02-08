# JPA SHOP 

* 주문 도메인 
  * 상품 주문 
  * 주문내역 조회
  * 주문 취소 




  

### JPA 성능 최적화 순서
1. 엔티티를 DTO로 변환하는 방법을 선택. (거이 필수)
2. 필요하면 페치 조인으로 성능을 최적화 
3. DTO로 직접 조회하는 방법을 사용한다. (new 오퍼레이션 사용 )
4. 최후의 방법은 네이티브 쿼리를 사용 



### JPA 키워드 
* JPQL => JPA DISTINCT의 의미
  * 쿼리에 DISTINCT 날려준다 
  + 조회하여 애플리케이션 레벨에서 키값으로 조합하여 중복 제거를 해준다
  > 주의 : 페이징 안됨 => 메모리에서 짤
  > 1대 다 단계에서 해결
  