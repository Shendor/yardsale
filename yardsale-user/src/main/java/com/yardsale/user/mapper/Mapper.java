package com.yardsale.user.mapper;

public interface Mapper<From, To> {

    To map(From from);


    From mapReverse(To to);
}
