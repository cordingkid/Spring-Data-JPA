package jpabook.jpashop.domain;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID") // 요즘은 소문자로 많이씀 가급적이면 객체보고 쿼리 짤수 있도록 매핑을 다 적는게 좋다.
    private Long id;
    private String name;
    private String city;
    private String street;
    private String zipcode;


    // getter는 다 만들어 주고 세터는 고민할 필요가 있다 일단 예제니까 만듬
    // 아무데서나 set할수 있어서 코드 추적하기 별로 좋지 않다. 유지보수성이 떨어진다.
    // 가급적이면 생성자에서 세팅을 하고 세터의 사용을 최소한 하는게 유지보수성이 좋다.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}