//package com.execute.protocol.core.mappers;
//
//import java.time.LocalDate;
//import java.time.Period;
//
//public class AgeTranslator {
//    public long birthdayToAge(LocalDate birthday) {
//        return Period.between(birthday, LocalDate.now()).getYears();
//    }
//    public LocalDate ageToBirthday(long age) {
//        return LocalDate.now().minusYears(age);
//    }
//}
