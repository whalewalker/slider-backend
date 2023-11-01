package com.sliderbackend.data.repository.contract;

import org.springframework.data.domain.Page;

public interface IRepo<T> {

    T create(T product);

    Page<T> getAll(int pageNum, int pageSize);

    T getById(String  id);

    T update(T product);

    void delete(String  id);
}