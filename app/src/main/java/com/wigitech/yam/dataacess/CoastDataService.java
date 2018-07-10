package com.wigitech.yam.dataacess;

import com.wigitech.yam.BuildConfig;
import com.wigitech.yam.model.Coast;
import com.wigitech.yam.model.DetailedCoast;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by user on 07/05/2016.
 */


@Rest(rootUrl = BuildConfig.BACKEND_URL, converters = MappingJackson2HttpMessageConverter.class)
@Accept(MediaType.APPLICATION_JSON_VALUE)
public interface CoastDataService {

    @Get("/information/v2/beach/{id}")
    DetailedCoast getCoast(@Path int id);

    @Get("/information/v2/beach")
    Coast[] getAllCoasts();

}