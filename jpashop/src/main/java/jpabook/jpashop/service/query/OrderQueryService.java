package jpabook.jpashop.service.query;

import org.springframework.transaction.annotation.Transactional;

/**
 * 지금은 연습이라 패키지 구조 했는데 그건 나중에 분리해보자.
 * 만약 맴버, 오더 이런 식으로 있으면
 * 아예 그 쪽 안에다가 이제 쿼리용 서비스를 별도로 넣는게 좋을것 같다.
 */
@Transactional(readOnly = true)
public class OrderQueryService {

}
