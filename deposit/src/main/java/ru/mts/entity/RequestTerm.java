//package ru.mts.entity;
//
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Getter
//@Setter
//@EqualsAndHashCode(exclude = "")
//@Entity
//@Table(name = "request_term", schema = "deposit")
//public class RequestTerm { //2.14
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_term_generator")
//    @SequenceGenerator(name = "request_term_generator", sequenceName = "deposit.id_request_term_sq", allocationSize = 1, initialValue = 1)
//    private Integer idRequestTerm;
//    @Column(name = "request_term_name")
//    private String requestTermName;
//
//    public RequestTerm() {
//    }
//
//    @Override
//    public String toString() {
//        return requestTermName;
//    }
//}