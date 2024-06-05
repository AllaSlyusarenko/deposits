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
//@EqualsAndHashCode(exclude = "idTypePercentPayment")
//@Entity
//@Table(name = "request_types_percent_payment", schema = "deposit")
//public class RequestTypesPercentPayment { //2.15
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_types_percent_payment_generator")
//    @SequenceGenerator(name = "request_types_percent_payment_generator", sequenceName = "deposit.id_request_types_percent_payment_sq", allocationSize = 1, initialValue = 1)
//    private Integer idTypePercentPayment;
//    @Column(name = "type_percent_payment_period")
//    private String typePercentPaymentPeriod;
//
//    public RequestTypesPercentPayment() {
//    }
//
//    @Override
//    public String toString() {
//        return typePercentPaymentPeriod;
//    }
//}
